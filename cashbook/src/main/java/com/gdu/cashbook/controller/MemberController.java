package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	// 비밀번호 찾기 폼
	@GetMapping("/findMemberPw")
	public String findMemberPw(HttpSession session) {
		if(session.getAttribute("loginMember") !=null) {
			return "redirect:/";
		}
		return "findMemberPw";
	}
	// 비밀번호 찾기 액션
	@PostMapping("/findMemberPw")
	public String findMemberPw(HttpSession session, Model model, Member member) {
		if(session.getAttribute("loginMember") !=null) {
			return "redirect:/";
		}
		int row = memberService.getMemberPw(member);
		String msg ="아이디와 메일을 확인하세요";
		if(row == 1) {
			System.out.println(msg);
			msg ="비밀번호를 입력한 메일로 전송하였습니다";
		}
		model.addAttribute("msg", msg);
		return "memberPwView";
	}
	//아이디찾기 폼
	@GetMapping("/findMemberId")
	public String findMemberId(HttpSession session) {
		if(session.getAttribute("loginMember") !=null) {
			return "redirect:/";
		}
		return "findMemberId";
	}
	//아이디 찾기 액션
	@PostMapping("/findMemberId")
	public String findMemberId(HttpSession session, Model model, Member member) {
		if(session.getAttribute("loginMember") !=null) {
			return "redirect:/";
		}
		String memberId = memberService.getMemberIdByMember(member);
		model.addAttribute("memberIdSub", memberId);
		return "memberIdView";
	}
	
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
	public String modify(LoginMember loginMember, HttpSession session, MemberForm memberForm) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		MultipartFile multif = memberForm.getMemberPic();//이미지 파일 입력한 값
		//입력한 파일이 null이 아니고 공백이 아닐경우
		if(memberForm.getMemberPic() != null && !multif.getOriginalFilename().equals("")) {
			if(!memberForm.getMemberPic().getContentType().equals("image/png") && //png타입
					!memberForm.getMemberPic().getContentType().equals("image/jpeg") && //jpge타입
					!memberForm.getMemberPic().getContentType().equals("image/gif")) { //gif타입
				return "redirect:/modifyMember";//3가지 타입이 충족하지 못하면 수정화면으로 다시 호출
			}
		}
		memberService.modifyMember(memberForm);
		System.out.println(memberForm+"회원수정할 시에 나오는 정보들");
		return "redirect:/memberInfo";
	}
		
	//로그인한 멤버의 탈퇴 시 비밀번호 확인
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
			model.addAttribute("msg", "중복 된 아이디입니다");		
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
	public String addMember(MemberForm memberForm, HttpSession session) {
		//로그인 중일때
		if(session.getAttribute("loginMember") !=null) { //세션이 널이 아니면 홈으로
			return "redirect:/"; 
		}	
		MultipartFile multif = memberForm.getMemberPic();
		String originName = multif.getOriginalFilename();
		System.out.println(originName+"-----------------------originName-----------");
		System.out.println(memberForm.toString()+"<-------MemberController"); //이미지 파일이 정상적으로 받아오는지 확인
			if(memberForm.getMemberPic() !=null && !originName.equals("")) {
				if(!memberForm.getMemberPic().getContentType().equals("image/png") && 
						!memberForm.getMemberPic().getContentType().equals("image/jpeg") &&
						!memberForm.getMemberPic().getContentType().equals("image/gif")) {
					return "redirect:/addMember"; 
				} 
			}
		//회원가입 폼에서 값이 정상적으로 넘어오는지 확인
		//System.out.println(member.toString());
		memberService.addMember(memberForm);
		return "redirect:/index"; //완료 후 홈으로
		}
}
