package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.BackBoardMapper;
import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.vo.Board;
@Service
@Transactional
public class BoardService {
	@Autowired private BoardMapper boardMapper;
	@Autowired private BackBoardMapper backBoardMapper;
	//게시판 목록|검색페이징필수
	public List<Board> getboardList(String seach) {
		Map<String, Object> map = new HashMap<>();
		List<Board> list = this.boardMapper.selectBoardList(seach);
		map.put("seach", seach);
		return list;
	}
	//게시글 상세보기
	public Board getBoardOne(Board board) {
		return boardMapper.selectBoardOne(board);
	}
	//게시글 추가하기
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
	}
	//나의 게시글 보기 |검색페이징필수
	public List<Board> getBoardMyList(String myseach, String memberId) {
		Map<String, Object> map = new HashMap<>();
		List<Board> list = boardMapper.selectMyBoardList(myseach, memberId);
		map.put("memberId", memberId);
		map.put("myseach", myseach);
		return list;
	}
	//게시글 수정 폼
	public Board getBoardUpdate(Board board) {
		return boardMapper.selectByUpdate(board);
	}
	//게시글 수정액션
	public int modifyBoard(Board board) {
		return boardMapper.updateBoard(board);
	}
	//게시글 삭제하기
	public int removeBoard(Board board) {
		int row = boardMapper.deleteBoard(board);
		if(row == 1) { //게시글 삭제 시 백업게시판에 게시글 추가	
		return	backBoardMapper.insertBackBoard(board);
		}
		System.out.println(row+"<===================가게부 삭제 후 백업테이블에 입력");
		return row;
	}
	/*//회원 탈퇴 시 작성한 게시글 전부 삭제하기
		public int alldeleteByBoard(Board board) {
			int row = boardMapper.alldeleteByBoard(board);
			if(row == 1) { //게시글 삭제 시 백업게시판에 게시글 추가	
			return	backBoardMapper.insertBackBoard(board);
			}
			System.out.println(row+"<===================가게부 삭제 후 백업테이블에 입력");
			return row;
		}*/
}
