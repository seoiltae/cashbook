package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	// 메인 홈 
   @GetMapping({"/","/index"})
   public String index(HttpSession session) {
     //로그인 되있을때 홈에서 인덱스 오는거 방지
	   if(session.getAttribute("loginMember") !=null || session.getAttribute("loginAdmin") !=null) {
		   return "redirect:/home";
	  }
	   return "index";
   }
   // 로그인 완료 후 개인화면 관리창
   @GetMapping("/home")
   public String home(HttpSession session) {
	   if(session.getAttribute("loginMember") == null && session.getAttribute("loginAdmin") ==null) {
		   return "redirect:/login";
	   }
	   return "home";
   }
}