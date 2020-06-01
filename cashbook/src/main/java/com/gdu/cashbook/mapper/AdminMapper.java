package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.LoginAdmin;
import com.gdu.cashbook.vo.Member;

@Mapper
public interface AdminMapper {
	//관리자 로그인
	public LoginAdmin selectLoginAdmin(LoginAdmin loginAdmin);
	//관리자의 회원정보 관리 및 검색
	public List<Member> selectMemberList(String search);
	//관리자의 회원삭제
	public int deleteMemberByAdmin(Member member);
	//관리자의 게시판목록 및 검색
	public List<Board> selectBoardList(String searchBo);
	//관리자의 게시판 삭제
	public int deleteBoardByAdmin(Board board);
}	
