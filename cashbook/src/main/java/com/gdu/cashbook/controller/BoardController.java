package com.gdu.cashbook.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.BoardService;
import com.gdu.cashbook.vo.Board;
@Controller
public class BoardController {
	@Autowired BoardService boardService;
	//게시판 목록
	@GetMapping("/boardList")
	public String getBoardList(HttpSession session, Model model) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		List<Board> boardList = boardService.getBoardList();
		System.out.println(boardList);
		model.addAttribute("list", boardList);	
		return "boardList";
	}
	//게시판 상세보기
	@GetMapping("/boardInfo")
	public String getBoardInfo(HttpSession session, Model model, Board board) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		board = boardService.getBoardOne(board);
		System.out.println(board+"<---------------------------선택한 넘버");
		model.addAttribute("board", board);
		return "boardInfo";
	}
}
