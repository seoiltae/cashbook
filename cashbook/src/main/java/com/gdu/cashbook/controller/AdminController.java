package com.gdu.cashbook.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.service.AdminService;
import com.gdu.cashbook.vo.LoginAdmin;
import com.gdu.cashbook.vo.Member;

@Controller
public class AdminController {
	@Autowired private AdminService adminService;
	//관리자 회원정보 관리
	@GetMapping("memberListAdmin")
	public List<Member> getMemberList(String search) {
		Map<String, Object> map = new HashMap<>();
		List<Member> list = adminService.selectMemberList(search);
		map.put("search", search);
		return list;
	}
	// 관리자 로그인 폼
	@GetMapping("/admin")
	public String admin(HttpSession session) {
		//관리자 로그인 중일떄
		if(session.getAttribute("loginAdmin") !=null) {
			return "redirect:/";
		}
		//관리자가 아닐경우
		return "admin";
	}
	//관리자 로그인 액션
	@PostMapping("/admin")
	public String admin(Model model, LoginAdmin loginAdmin, HttpSession session) {
		System.out.println(loginAdmin+"loginAdmin-------------------------------------");
		LoginAdmin returnLoginAdmin = adminService.admin(loginAdmin);
		System.out.println(returnLoginAdmin+"returnLoginAdmin--------------------------------------------------------"); //디버깅값
		if(returnLoginAdmin == null) {//관리자 로그인 실패시
			model.addAttribute("msg", "관리자 로그인 오류");
			 return "admin";
		} else { //관리자 로그인 성공 시
			session.setAttribute("loginAdmin", returnLoginAdmin);
			return "redirect:/home";
		}
 	}
}
