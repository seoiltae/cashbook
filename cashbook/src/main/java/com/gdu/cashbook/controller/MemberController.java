package com.gdu.cashbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.vo.Member;

@Controller
public class MemberController {
	@GetMapping("/addMember")
	public String addMember() {
		return "addMember";
	}
	
	@PostMapping("/addMember")
	public String addMember(Member member) { //Comand 객체 , 도메인 객체
		System.out.println(member.toString());
		//System.out.println(member);
		return "redirect:/index";
	}
}
