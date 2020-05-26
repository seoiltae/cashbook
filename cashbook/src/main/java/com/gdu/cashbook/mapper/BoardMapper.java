package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Board;
@Mapper
public interface BoardMapper {
	public List<Board> selectBoardList(); //게시글 목록(페이징,검색)
	public Board selectBoardOne(Board board); //게시글 상세보기
	public int insertBoard(Board board); // 게시글 추가
	public int deleteBoard(Board board); // 게시글 삭제
	public List<Board> selectMyBoardList(Board board); //나의 게시글 보기(페이징,검색)
	public Board selectByUpdate(Board board); // 게시글 수정 폼
	public int updateBoard(Board board); // 게시글 수정 액션
}
