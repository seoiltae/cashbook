package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Board;
@Mapper
public interface BoardMapper {
	public List<Board> selectBoardList(); //게시글 목록(페이징,검색)
	public Board selectBoardOne(Board board); //게시글 상세보기
}
