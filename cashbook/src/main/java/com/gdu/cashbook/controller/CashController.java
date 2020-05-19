package com.gdu.cashbook.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CashController {
	@Autowired CashService cashService;
	
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(HttpSession session, Model model, 
			@RequestParam(value = "day", required = false) //뷰에서 입력한 값을 받아옴
			@DateTimeFormat(pattern = "yyyy-MM-dd") 
			LocalDate day) {
		
		if(day == null) {//day가 널일경우 로컬날짜로 한다
			day = LocalDate.now(); 
		}
		// 날짜 나오게 하는것
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //날짜나오는 양식
		String dday = day.format(formatter); //변수에 날짜의 값을 저장해줌
		
		//day = day.plusDays(1); 
		//day = day.plusMonths(1);
		//day = day.minusDays(1);
		//day = day.minusMonths(1); 
		
		//로그인 아이디
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		//로그인 
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		
		Cash cash = new Cash(); // +id, +date
		cash.setMemberId(loginMemberId); //로그인한 멤버아이디
		cash.setCashDate(dday); // 입력한 날짜
		List<Cash> cashlist = cashService.getCashListByDate(cash);
		model.addAttribute("cashlist", cashlist);//cashlist(가계부목록) ->view 
		model.addAttribute("day", day); // today(오늘날짜) ->view
		
		/*
		//오늘 날짜 
		Date day = new Date(); //날짜,시간,요일 전부를 출력하는 Date 클래스
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //원하는 날짜형태로 변경해주는 자바 클래스
		String strToday=sdf.format(day); // 원하는 날짜형태를 변수에 입력
		System.out.println(strToday+"<---strToday 오늘날짜 CashController"); 
		
		Cash cash = new Cash(); // +id, +date
		cash.setMemberId(loginMemberId); //로그인한 멤버아이디
		cash.setCashDate(strToday); // 입력한 날짜
		
		List<Cash> cashlist = cashService.getCashListByDate(cash);
		model.addAttribute("cashlist", cashlist);//cashlist(가계부목록) ->view 
		model.addAttribute("day", strToday); // today(오늘날짜) ->view
		System.out.println(cashlist+"<----cashList목록");
		*/
		return "getCashListByDate";
	}
}
