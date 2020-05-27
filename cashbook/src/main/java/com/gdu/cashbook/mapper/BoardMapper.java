package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.LoginMember;
@Mapper
public interface BoardMapper {
	public List<Board> selectBoardList(String seach); //게시글 목록(페이징,검색)
	public Board selectBoardOne(Board board); //게시글 상세보기
	public int insertBoard(Board board); // 게시글 추가
	public int deleteBoard(Board board); // 게시글 삭제
	public List<Board> selectMyBoardList(String myseach, String memberId); //나의 게시글 보기(페이징,검색)
	public Board selectByUpdate(Board board); // 게시글 수정 폼
	public int updateBoard(Board board); // 게시글 수정 액션
	public int alldeleteByBoard(LoginMember loginMember); // 회원탈퇴시 작성한 게시글 전부 삭제
}
