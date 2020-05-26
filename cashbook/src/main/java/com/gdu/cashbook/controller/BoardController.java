package com.gdu.cashbook.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.service.BoardService;
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
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		model.addAttribute("loginMember", memberId);
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
	//나의 게시글 보기
	@GetMapping("/boardMyInfo")
	public String myBoard(HttpSession session, Model model, Board board) {
		if(session.getAttribute("loginMember") ==null) { 
			session.invalidate();
			return "redirect:/"; 
		}
		List<Board> myBoardList = boardService.getBoardMyList(board);
		System.out.println(myBoardList+"<-------------------나의 게시글 목록");
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		model.addAttribute("memberId", memberId);
		model.addAttribute("myBoardList", myBoardList);
		return "boardMyInfo";
	}
	//게시글 삭제 액션
	@GetMapping("/removeBoard")
	public String removeBoard(Board board, HttpSession session, Model model,
			@RequestParam("boardNo") int boardNo) {
		if(session.getAttribute("loginMember") ==null) { 
			return "redirect:/"; 
		}
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		model.addAttribute("memberId", memberId);
		boardService.removeBoard(board);
		return "redirect:/boardMyInfo?memberId="+memberId;
	}
	//게시글 수정 폼
	@GetMapping("/modifyBoard")
	public String modifyBoard(Board board, HttpSession session, Model model) {
		if(session.getAttribute("loginMember") ==null) { 
			return "redirect:/"; 
		}
		board = boardService.getBoardUpdate(board);
		model.addAttribute("board", board);
		System.out.println();
		return "modifyBoard";
	}
	//게시글 수정 액션
	@PostMapping("/modifyBoard")
	public String modifyBoard(Board board, HttpSession session) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		boardService.modifyBoard(board);
		return "redirect:/boardMyInfo?memberId="+memberId;
	}
}

