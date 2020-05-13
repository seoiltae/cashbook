package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Controller
public class MemberController {
	@Autowired
	private MemberService MemberService;
	//로그인 폼으로 Form
	@GetMapping("/login") 
	public String login() {
		return "login";
	}
	
	// 로그인 완료 시  Action
	@PostMapping("/login") 
	public String login(LoginMember loginMember, HttpSession session) { //HttpSession 세션  model2 : request.getsession
		System.out.println(loginMember);
		LoginMember returnLoginMember = MemberService.login(loginMember);
		System.out.println(returnLoginMember+"returnLoginMember");
		if(returnLoginMember == null) { //로그인 실패시 로그인폼으로
			return "redirect:/login"; 
		} else { //로그인 성공 시 
			session.setAttribute("loginMember", returnLoginMember); //로그인 성공한 세션 값
			return "redirect:/";
		}
		
	}
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	
	
	//회원가입 폼으로 Form
	@GetMapping("/addMember")
	public String addMember() {
		return "addMember";
	}
	//회원가입완료 시 Action
	@PostMapping("/addMember")
	public String addMember(Member member) {
		//회원가입 폼에서 값이 정상적으로 넘어오는지 확인
		System.out.println(member.toString());
		MemberService.addMember(member);
		return "redirect:/index"; //완료 후 홈으로
	}
	
	
}
