package com.gdu.cashbook.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.BoardService;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.Comment;
import com.gdu.cashbook.vo.LoginAdmin;
import com.gdu.cashbook.vo.LoginMember;
@Controller
public class BoardController {
	@Autowired BoardService boardService;
	// 게시판 댓글 입력 폼
	@GetMapping("/addComment")
	public String addComment(HttpSession session, Model model,
			Board board) {
		if(session.getAttribute("loginMember") ==null) { 
			session.invalidate();
			return "redirect:/"; 
		}
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		//로그인한 아이디
		model.addAttribute("memberId", memberId);
		//현재 게시글의 번호
		model.addAttribute("boardNo", board.getBoardNo());
		return "addComment";
	}
	//게시판 댓글 입력 액션
	@PostMapping("/addComment") 
	public String addComment(HttpSession session, Model model, Comment comment,
			@RequestParam("boardNo") int boardNo) 	{
		if(session.getAttribute("loginMember") ==null) { 
			return "redirect:/"; 
		}	
		boardService.addComment(comment);
		System.out.println(boardNo+"<----------------------댓글 입력 시 선택된 게시글 번호");
		//댓글 작성 후 해당 게시글로 이동
		return "redirect:/boardInfo?boardNo="+boardNo;
	}
	// 게시판 댓글 수정 폼
	@GetMapping("/modifyComment")
	public String modifyComment(HttpSession session, Model model, Comment comment) {
		if(session.getAttribute("loginMember") ==null) { 
			return "redirect:/"; 
		}	
		comment = boardService.getUpdateSelectComment(comment);
		System.out.println(comment.getBoardNo()+"<------------수정할 댓글 정보");
		System.out.println(comment.getCommentContents()+"<------------수정할 댓글 정보");
		System.out.println(comment.getCommentNo()+"<------------수정할 댓글 정보");
		System.out.println(comment.getMemberId()+"<------------수정할 댓글 정보");
		System.out.println(comment.getCommentDate()+"<------------수정할 댓글 정보");
		model.addAttribute("comment", comment);
		return "modifyComment";
	}
	//게시판 댓글 수정 액션
	@PostMapping("/modifyComment")
	public String modifyComment(HttpSession session, Model model, Comment comment,
			@RequestParam("boardNo") int boardNo, @RequestParam("commentNo") int commentNo) {
		if(session.getAttribute("loginMember") ==null) { 
			return "redirect:/"; 
		}	
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		boardService.modifyComment(comment);
		System.out.println(memberId+"<--------------------댓글 수정 시 memberId");
		System.out.println(boardNo+"<---------------------댓글 수정 시 boardNo");
		System.out.println(commentNo+"<-------------------댓글 수정 시 commentNo");
		return "redirect:/boardInfo?boardNo="+boardNo;
	}
	//게시판 댓글 삭제
	@GetMapping("/removeComment")
	public String removeComment(HttpSession session, Comment comment,
			@RequestParam("boardNo") int boardNo, @RequestParam("commentNo") int commentNo) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		boardService.removeComment(comment);
		return "redirect:/boardInfo?boardNo="+comment.getBoardNo();
	}
	//게시판 목록
	@GetMapping("/boardList")
	public String getBoardList(HttpSession session, Model model,
			@RequestParam(value="seach", defaultValue = "") String seach) {
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		List<Board> list = this.boardService.getboardList(seach);
		System.out.println(seach+"<<-------------검색검색");
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		model.addAttribute("loginMember", memberId);
		model.addAttribute("list", list);
		return "boardList";
	}
	//게시판 상세보기
	@GetMapping("/boardInfo")
	public String getBoardInfo(HttpSession session, Model model, Board board,
			@RequestParam("boardNo") int boardNo) {
		if(session.getAttribute("loginMember") ==null || session.getAttribute("loginAdmin")==null) {
			return "redirect:/";
		}
		Comment comment = new Comment();
		//댓글 목록
		List<Comment> list = boardService.getboardByComment(comment, boardNo);
		System.out.println(list+"<---------Comment 댓글댓글");
		//게시판 상세보기 
		board = boardService.getBoardOne(board);
		System.out.println(board+"<---------------------------선택한 넘버");
		model.addAttribute("list", list);
		model.addAttribute("board", board);
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		model.addAttribute("loginMember", memberId);
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
	public String myBoard(HttpSession session, Model model,
			@RequestParam(value="myseach", defaultValue="", required = false) String myseach) {
		if(session.getAttribute("loginMember") ==null) { 
			session.invalidate();
			return "redirect:/"; 
		}
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		
		System.out.println(memberId+"<-----------------------------------------------------------현재 로그인");
		List<Board> myBoardList = boardService.getBoardMyList(myseach, memberId);
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

