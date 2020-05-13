package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Mapper
public interface MemberMapper {
	public  LoginMember selectLoginMember(LoginMember loginMember); //로그인
	public void insertMember(Member member); // 회원가입
}
