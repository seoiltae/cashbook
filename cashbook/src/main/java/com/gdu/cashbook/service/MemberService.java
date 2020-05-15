package com.gdu.cashbook.service;



import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.mapper.MemberidMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.Memberid;
@Service
@Transactional
public class MemberService {
	@Autowired private MemberMapper memberMapper;
	@Autowired private MemberidMapper memberidMapper;
	@Autowired private JavaMailSender javaMailSender; //자바메일보내는 객체
	//비밀번호 찾기
	public int getMemberPw(Member member) {
		//pw 추가
		UUID uuid = UUID.randomUUID(); //랜덤으로 문자열 생성
		
		String memberPw = uuid.toString().substring(0, 8);
		member.setMemberPw(memberPw);
		int row = memberMapper.updateMemberPw(member);
		if(row == 1) {
			System.out.println(memberPw+"<--update memberPw");
			// 메일로 update 성공한 랜덤 pw를 전송
			// 메일객체 new javaMailSender(); 자바메일샌더 클래스 
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setTo(member.getMemberEmail());
			System.out.println(member.getMemberEmail()+"-----------------------");
			simpleMailMessage.setFrom("iltaeseo07@gmail.com");
			simpleMailMessage.setSubject("cashbook 비밀번호 찾기 메일");
			simpleMailMessage.setText("변경된 비밀번호 : "+memberPw+"입니다.");
			javaMailSender.send(simpleMailMessage);
			
		}
		return row;
	}
	
	// 이이디 잃어버렸을 떄 찾기
	public String getMemberIdByMember(Member member) {
		return memberMapper.selectMemberIdByMember(member);
	}
	//로그인한 멤버수정 폼
	public Member selectMemberUpdate(LoginMember loginMember) {
		return memberMapper.selectMemberUpdate(loginMember);
	}
	
	//로그인한 멤버 수정 액션
	public void modifyMember(Member member) {
		memberMapper.updateMember(member);
	}
	
	//로그인한 회원탈퇴
	public int removeMember(LoginMember loginMember) {
		//1. 멤버아이디테이블에 아이디 추가
		Memberid memberid = new Memberid();
		if(memberMapper.deleteMember(loginMember) == 1) {
			//2. 회원정보 회원탈퇴
			memberid.setMemberId(loginMember.getMemberId());
			return memberidMapper.insertMemberid(memberid);
			} 
		return 0;
	}
	//로그인한 회원의 상세정보
	public Member getMemberOne(LoginMember loginMember) {
		return memberMapper.selectMemberOne(loginMember);
	}
	//중복 확인
	public String checkMemberId(String memberIdCheck) {
		return memberMapper.selectMemberId(memberIdCheck);
	}
	//로그인
	public LoginMember login(LoginMember loginMember) {
		return memberMapper.selectLoginMember(loginMember);
	}
	//회원가입
	public void addMember(Member member) {
		memberMapper.insertMember(member);
	}
}
