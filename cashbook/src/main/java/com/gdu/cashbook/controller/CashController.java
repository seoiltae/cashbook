package com.gdu.cashbook.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CashController {
	@Autowired CashService cashService;
	
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(HttpSession session, Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		//로그인 아이디
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		
		//오늘 날짜 
		Date today = new Date(); //날짜,시간,요일 전부를 출력하는 Date 클래스
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //원하는 날짜형태로 변경해주는 자바 클래스
		String strToday=sdf.format(today); // 원하는 날짜형태를 변수에 입력
		System.out.println(strToday+"<---strToday 오늘날짜 CashController");
		Cash cash = new Cash(); // +id, +date
		cash.setMemberId(loginMemberId); //로그인한 멤버아이디
		cash.setCashDate(strToday); // 입력한 날짜
		
		List<Cash> cashlist = cashService.getCashListByDate(cash);
		model.addAttribute("cashlist", cashlist);//cashlist(가계부목록) ->view 
		model.addAttribute("strToday", strToday); // today(오늘날짜) ->view
		System.out.println(cashlist+"<----cashList목록");
		return "getCashListByDate";
	}
}
