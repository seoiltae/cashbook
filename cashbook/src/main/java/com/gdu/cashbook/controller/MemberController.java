package com.gdu.cashbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.Member;

@Controller
public class MemberController {
	@Autowired
	private MemberService MemberService;
	//회원가입 폼으로
	@GetMapping("/addMember")
	public String addMember() {
		return "addMember";
	}
	//회원가입완료 시 
	@PostMapping("/addMember")
	public String addMember(Member member) {
		//회원가입 폼에서 값이 정상적으로 넘어오는지 확인
		System.out.println(member.toString());
		MemberService.addMember(member);
		return "redirect:/index"; //완료 후 홈으로
	}
}
