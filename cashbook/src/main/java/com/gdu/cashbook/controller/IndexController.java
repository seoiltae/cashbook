package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	// 메인 홈 
   @GetMapping({"/","/index"})
   public String index() {
      return "index";
   }
   // 로그인 완료 후 개인화면 관리창
   @GetMapping("/home")
   public String home(HttpSession session) {
	   if(session.getAttribute("loginMember") == null) {
		   return "redirect:/login";
	   }
	   return "home";
   }
}