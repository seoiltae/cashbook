package com.gdu.cashbook.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.cashbook.service.BoardService;
@Controller
public class BoardController {
	@Autowired BoardService boardService;
	//게시판 목록
	@GetMapping("/boardList")
	public String getBoardList(HttpSession session, Model model, LocalDate day) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
			day = LocalDate.now();
		return "/boardList";
	}
}
