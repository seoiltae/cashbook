package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.vo.Board;
@Service
public class BoardService {
	@Autowired private BoardMapper boardMapper;
	//게시판 목록
	public List<Board> getBoardList() {
		Map<String, Object> map = new HashMap<>(); //map.put() <-페이징 완료하면 맵에 페이징넣기
		return boardMapper.selectBoardList(map);
	}
}
