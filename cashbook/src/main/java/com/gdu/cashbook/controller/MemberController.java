package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	//로그인한 멤버수정 폼
	@GetMapping("/modifyMember") //<--회원 정보를 수정할 수 있는 창
	public String modify(Model model, HttpSession session) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		// LoginMember타입으로 형변환 후 로그인한 회원의 수정할 수 있는 정보를 보여준다
				Member member = memberService.selectMemberUpdate((LoginMember)(session.getAttribute("loginMember")));
				System.out.println(member+"<--로그인한 멤버의 정보");
				model.addAttribute("member", member);
				return "modifyMember";
	}
	//로그인한 회원 수정 액션 
	@PostMapping("/modifyMember")
	public String modify(LoginMember loginMember, HttpSession session, Member member) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		memberService.modifyMember(member);
		System.out.println(member+"회원수정할 시에 나오는 정보들");
		session.invalidate();
		return "redirect:/";
	}
		
	//로그인한 멤버의 탈퇴 및 수정 시 비밀번호 확인
	@GetMapping("/removeMember") //<--회원 탈퇴 및 회원수정 시 비밀번호 한번 더 입력
	public String removeMember(Model model, LoginMember loginMember, HttpSession session) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		memberService.login(loginMember);
		model.addAttribute("loginMember", loginMember);
		return "removeMember";
	}
	//회원 탈퇴 
	@PostMapping("/removeMember")
	public String removeMember(HttpSession session, @RequestParam("memberPw") String memberPw) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		LoginMember loginMember = (LoginMember)(session.getAttribute("loginMember"));
		loginMember.setMemberPw(memberPw);
		if(memberService.removeMember(loginMember)==1) {
			System.out.println(loginMember+"회원 탈퇴 시 값이 제대로 넘어가는 확인하는 디버깅");
			session.invalidate();
			return "redirect:/index";
		}
		return "removeMember";
	}
	// 로그인한 멤버의 상세정보
	@GetMapping("/memberInfo")
	public String memberInfo(Model model, HttpSession session) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		// LoginMember타입으로 형변환 후 로그인한 회원의 정보를 보여준다
		Member member = memberService.getMemberOne((LoginMember)(session.getAttribute("loginMember")));
		System.out.println(member+"<--로그인한 멤버의 정보");
		model.addAttribute("member", member);
		return "memberInfo";
	}
	
	//로그인 폼으로 Form
	@GetMapping("/login") 
	public String login(HttpSession session) {
		//로그인 중일떄
		if(session.getAttribute("loginMember") !=null) {
			return "redirect:/";
		}
		//로그인이  아니었을 경우
		return "login";
	}
	
	// 로그인 완료 시  Action
	@PostMapping("/login") 
	public String login(Model model, LoginMember loginMember, HttpSession session) { //HttpSession 세션  model2 : request.getsession
		System.out.println(loginMember);
		LoginMember returnLoginMember = memberService.login(loginMember);
		System.out.println(returnLoginMember+"returnLoginMember"); //디버깅
		if(returnLoginMember == null) { //로그인 실패시 로그인폼으로
			model.addAttribute("msg", "아이디와 비밀번호를 확인하세요");
			return "login"; 
		} else { //로그인 성공 시 
			session.setAttribute("loginMember", returnLoginMember); //로그인 성공한 세션 값
			return "redirect:/home";
		}
		
	}
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		//로그인이 완료인 경우
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/"; 
		}
		session.invalidate();
		return "redirect:/";
	}
	//회원 가입시 중복 체크
	@PostMapping("/checkMemberId")
	public String checkMemberId(Model model, @RequestParam("memberIdCheck") String memberIdCheck) {
		String confirmMemberId = memberService.checkMemberId(memberIdCheck); 
		if(confirmMemberId == null) { //아이디 중복체크 
			//아이디를 사용 가능
			System.out.println("memberIdCheck");
			model.addAttribute("msg2", "사용가능한 아이디입니다");
			model.addAttribute("memberIdCheck", memberIdCheck); 
			
		} else {
			// 아이디를 사용 불가
			model.addAttribute("msg", "사용중인 아이디입니다");		
		}
		return "addMember";
	}
	
	//회원가입 폼으로 Form
	@GetMapping("/addMember")
	public String addMember(HttpSession session) {
		//로그인 중일때
		if(session.getAttribute("loginMember") !=null) { //세션이 널이 아니면 홈으로
			session.invalidate();
			return "redirect:/"; 
		} 
		return "addMember";
	}
	//회원가입완료 시 Action
	@PostMapping("/addMember")
	public String addMember(Member member, HttpSession session) {
		//로그인 중일때
		if(session.getAttribute("loginMember") !=null) { //세션이 널이 아니면 홈으로
			return "redirect:/"; 
		}
		//회원가입 폼에서 값이 정상적으로 넘어오는지 확인
		System.out.println(member.toString());
		memberService.addMember(member);
		return "redirect:/index"; //완료 후 홈으로
	}
	
	
}
