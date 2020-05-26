package com.gdu.cashbook.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.service.BoardService;
import com.gdu.cashbook.vo.BackBoard;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.LoginMember;
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
	//게시판 추가하기 폼
	@GetMapping("/addBoard")
	public String addBoard(HttpSession session, Model model) {
		if(session.getAttribute("loginMember") ==null) { 
			session.invalidate();
			return "redirect:/"; 
		}
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		model.addAttribute("loginMember", memberId);
		return "addBoard";
	}
	//게시판 추가하기 액션
	@PostMapping("/addBoard")
	public String addBoard(Board board, HttpSession session) {
		if(session.getAttribute("loginMember") ==null) { 
			return "redirect:/"; 
		}	
		boardService.addBoard(board);
		return "redirect:/boardList";
	} 
	//게시판 삭제하기 폼
	@GetMapping("/removeBoard")
	public String removeBoard(Board board) {
	if(session.getAttribute("loginMember") ==null) { 
		return "redirect:/"; 
		}	
	}
	//게시글 삭제 액션
	@PostMapping("/removeBoard")
	public String removeBoard(Board board, BackBoard backBoard) {
		boardService.removeBoard(board, backBoard);
	}
}

