package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Mapper //객체를 만듬 @=타입
public interface MemberMapper {
	public int updateMemberPw(Member member); //비밀번호 찾기
	public String selectMemberIdByMember(Member member); //아이디 찾기
	
	public Member selectMemberUpdate(LoginMember loginMember); //로그인한 신상정보 수정 폼
	public void updateMember(Member member); // 로그인한 멤버의 수정 액션
	public int deleteMember(LoginMember loginMember); //회원 삭제
	public Member selectMemberOne(LoginMember loginMember); //로그인 멤버의 상세정보
	public String selectMemberId(String memberIdCheck); //중복확인
	public LoginMember selectLoginMember(LoginMember loginMember); //로그인
	public void insertMember(Member member); // 회원가입
}
