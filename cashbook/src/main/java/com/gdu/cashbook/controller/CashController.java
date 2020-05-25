package com.gdu.cashbook.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.Category;
import com.gdu.cashbook.vo.DayAndPrice;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CashController {
	@Autowired CashService cashService;
	@GetMapping("/getCashListByMonth")
	public String getCashListByMonth(HttpSession session, Model model, 
			@DateTimeFormat(pattern = "yyyy-MM-dd") 
			LocalDate day) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		Calendar cDay = Calendar.getInstance(); //오늘
		if(day == null) {//day가 널일경우 로컬날짜로 한다
			day = LocalDate.now();
		} else {
			/*
			 *  LocalDate -> Calendar x
			 *  LocalDate -> Date -> Calendar
			 *  LocalDate -> String -> Calendar
			 *  LocalDate -> Calendar 사용
			 */
			cDay.set(day.getYear(), day.getMonthValue()-1, day.getDayOfMonth()); //오늘날짜에서 day값으로 설정
		}
	
		//일별 가계부 총합계리스트 
		String memberId =((LoginMember)session.getAttribute("loginMember")).getMemberId();
		int year = cDay.get(Calendar.YEAR);
		int month = cDay.get(Calendar.MONTH)+1;
		List<DayAndPrice> dayAndPriceList = cashService.getCashAndPriceList(memberId, year, month);
		System.out.println(dayAndPriceList+"<----------dayAndPriceList(year, month)");
		System.out.println(memberId+"<-----------memberId");
		for(DayAndPrice dp : dayAndPriceList) {
	         System.out.println(dp);
	      }
		model.addAttribute("dayAndPriceList", dayAndPriceList); //월별 가계부 총합계리스트
		model.addAttribute("day", day); // 오늘년도,월,일
		model.addAttribute("month", cDay.get(Calendar.MONTH)+1); //월
		model.addAttribute("lastDay", cDay.getActualMaximum(Calendar.DATE)); //마지막 일
		Calendar firstDay = cDay;
		firstDay.set(Calendar.DATE, 1); // 일만 1일로 변경
		firstDay.get(Calendar.DAY_OF_WEEK);//1이면 일요일, 2이면 월요일
		model.addAttribute("firstDayOfWeek", firstDay.get(Calendar.DAY_OF_WEEK)); // 이번달에 1일이 무슨요일
		return "getCashListByMonth";
	}
	//일별 가계부 목록
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
	//가계부 입력 폼
	@GetMapping("/addCash")
	public String addCash(Cash cash, HttpSession session, Model model, 
			LocalDate day) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		if(day == null) {//day가 널일경우 로컬날짜로 한다
			day = LocalDate.now(); 
		}
		model.addAttribute("day", day);
		
		//로그인 아이디
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		List<Category> categoryList = cashService.getSelectCategoryList();
		model.addAttribute("cList", categoryList); //카테고리목록
		model.addAttribute("memberId", loginMemberId); //멤버아이디
		return "addCash";
	}
	//가계부 입력 액션
	@PostMapping("/addCash")
	public String addCash(Cash cash, Model model, Category category,
			@RequestParam(value = "day", required = false )
			@DateTimeFormat(pattern = "yyyy-MM-dd")
			LocalDate day) {
		cashService.addCash(cash, category);
		return "redirect:/getCashListByDate?day="+cash.getCashDate();
	}
	//가계부 수정폼
	@GetMapping("/modifyCash")
	public String modifyCash(Cash cash, Model model, HttpSession session) {
		//로그인 세션
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		//카테고리 리스트
		List<Category> categoryList = cashService.getSelectCategoryList();
		model.addAttribute("cList", categoryList);
		
		System.out.println(categoryList+"<-----categoryList");
		
		cash = cashService.selectCashOneUp(cash);
		model.addAttribute("modiCash", cash);
		System.out.println(cash+"<--------------dddddddd"); //디버깅
		return "modifyCash";
	}
	//가계부 수정액션
	@PostMapping("/modifyCash")
	public String modifyCash(Cash cash, Model model, Category category,
			@RequestParam("categoryName") String categoryName,
			@RequestParam(value = "day", required = false) //날짜 LocalDate
			@DateTimeFormat(pattern = "yyyy-MM-dd") // 받아올 날짜 형식
			LocalDate day) {
		cashService.modifyCash(cash, category);
		cash.setCategoryName(categoryName);
		System.out.println(cash+"<---------------수정결과");
		return "redirect:/getCashListByDate?day="+cash.getCashDate();
	}
	//가계부 삭제 post
	@GetMapping("/removeCash")
	public String removeCash(Cash cash, @RequestParam("cashNo") int cashNo) {
		cash.setCashNo(cashNo);
		cashService.removeCash(cash);
		return "redirect:/getCashListByDate";
		}
}
