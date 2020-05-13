package com.gdu.cashbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	// 메인 홈 
   @GetMapping({"/","/index"})
   public String index() {
      return "index";
   }
}