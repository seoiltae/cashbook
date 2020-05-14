package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Mapper //객체를 만듬 @=타입
public interface MemberMapper {
	public Member selectMemberOne(LoginMember loginMember); //로그인 멤버의 신상정보
	public String selectMemberId(String memberIdCheck); //중복확인
	public LoginMember selectLoginMember(LoginMember loginMember); //로그인
	public void insertMember(Member member); // 회원가입
}
