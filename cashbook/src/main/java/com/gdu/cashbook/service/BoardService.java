package com.gdu.cashbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.BackBoardMapper;
import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.vo.BackBoard;
import com.gdu.cashbook.vo.Board;
@Service
@Transactional
public class BoardService {
	@Autowired private BoardMapper boardMapper;
	@Autowired private BackBoardMapper backBoardMapper;
	//게시판 목록
	public List<Board> getBoardList() {
		//Map<String, Object> map = new HashMap<>(); //map.put() <-페이징 완료하면 맵에 페이징넣기
		return boardMapper.selectBoardList();
	}
	//게시글 상세보기
	public Board getBoardOne(Board board) {
		return boardMapper.selectBoardOne(board);
	}
	//게시글 추가하기
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
	}
	//게시글 삭제하기
	public int removeBoard(Board board, BackBoard backBoard) {
		int row = boardMapper.deleteBoard(board);
		if(row == 1) { //게시글 삭제 시 백업게시판에 게시글 추가	
		return	backBoardMapper.insertBackBoard(backBoard);
		}
		return row;
	}
}
