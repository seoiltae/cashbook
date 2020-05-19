package com.gdu.cashbook.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

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
	//가계부 수정 폼
	@GetMapping("/modifyCash")
	public String modifyCash(Cash cash, Model model, HttpSession session) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		cash = cashService.modifyCash(cash);
		model.addAttribute("modiCash", cash);
		System.out.println(cash+"<--------------dddddddd");
		return "modifyCash";
	}
	//가계부 삭제 post
	@GetMapping("/removeCash")
	public String removeCash(Cash cash, @RequestParam("cashNo") int cashNo) {
		cash.setCashNo(cashNo);
		cashService.removeCash(cash);
		return "redirect:/getCashListByDate";
		}
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(HttpSession session, Model model, 
			@RequestParam(value = "day", required = false) //뷰에서 입력한 값을 받아옴
			@DateTimeFormat(pattern = "yyyy-MM-dd") 
			LocalDate day) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		if(day == null) {//day가 널일경우 로컬날짜로 한다
			day = LocalDate.now(); 
		}
		// 날짜 나오게 하는것
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //날짜나오는 양식
		String dday = day.format(formatter); //변수에 날짜의 값을 저장해줌
		//day = day.plusDays(1); day = day.plusMonths(1); day = day.minusDays(1); day = day.minusMonths(1); 직접 날짜의 일을 입력 시
		//로그인 아이디
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		//로그인 
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		
		Cash cash = new Cash(); // +id, +date
		cash.setMemberId(loginMemberId); //로그인한 멤버아이디
		cash.setCashDate(dday); // 입력한 날짜
		Map<String, Object> map = cashService.getCashListByDate(cash);
		model.addAttribute("cashKindSum", map.get("cashKindSum"));
		model.addAttribute("cashlist", map.get("cashList"));//cashlist(가계부목록) ->view 
		model.addAttribute("day", day); // today(오늘날짜) ->view
		return "getCashListByDate";
	}
}
