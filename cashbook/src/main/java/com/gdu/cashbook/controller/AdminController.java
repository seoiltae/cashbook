package com.gdu.cashbook.controller;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.AdminService;
import com.gdu.cashbook.service.BoardService;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.LoginAdmin;
import com.gdu.cashbook.vo.Member;

@Controller
public class AdminController {
	@Autowired private AdminService adminService;
	//관리자 게시판 삭제
	//@GetMapping("/removeBoardByAdmin")
	public String removeBoardByAdmin(HttpSession session, Model model,
			@RequestParam("boardNo") int boardNo) { //넘버를 전송받아와 해당넘버의 데이터를 삭제
		if(session.getAttribute("loginAdmin") ==null) {
			return "redirect:/";
		}
		System.out.println(boardNo+"<-----------------------------------------removeBoardByAdmin GetMapping boardNo");
		adminService.removeBoardByAdmin(boardNo);
		return "redirect:/adminBoard";
	}
	//관리자 게시판 관리
	@GetMapping("/adminBoard")
	public String getBoardList(HttpSession session, Model model,
			@RequestParam(value="searchBo", defaultValue = "") String searchBo) {
		if(session.getAttribute("loginAdmin") == null) {
			return "redirect:/";
		}
		System.out.println(searchBo+"<-------------------------adminBoard GetMapping searchBo 게시판 검색 결과");
		List<Board> list = adminService.selectBoardList(searchBo);
		model.addAttribute("list", list); // 
		return "adminBoard";
	}
	//관리자 회원정보 삭제
	@GetMapping("/removeMemberByAdmin")
	public String removeMemberByAdmin(Member member, HttpSession session, Model model,
			@RequestParam("memberId") String memberId) {
		if(session.getAttribute("loginAdmin") ==null) {
			return "redirect:/";
		}
		System.out.println(memberId+"=================================멤버아이디값");
		adminService.removeMemberByAdmin(member, memberId);
		return "redirect:/adminMember";
	}
	//관리자 회원정보 관리
	@GetMapping("adminMember")
	public String getMemberList(HttpSession session, Model model,
			@RequestParam(value="search", defaultValue = "") String search) {
		if(session.getAttribute("loginAdmin") ==null) {
			return "redirect:/";
		}
		System.out.println(search+" <-----검색검색");
		List<Member> list = adminService.selectMemberList(search);
		model.addAttribute("list", list);
		return "adminMember";
	}
	// 관리자 로그인 폼
	@GetMapping("/admin")
	public String admin(HttpSession session) {
		//관리자 세션이 널이 아닐경우
		if(session.getAttribute("loginAdmin") !=null) {
			return "redirect:/";
		}
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
