package com.gdu.cashbook.service;



import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.mapper.MemberidMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;
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
	public int addMember(MemberForm memberForm) {// 멤버폼을 멤버 타입으로 변환 시켜야한다
		MultipartFile multif = memberForm.getMemberPic();
		String originName = multif.getOriginalFilename();
		if(multif.getContentType().equals("image/png") || multif.getContentType().equals("image/jpeg")) {
			// png와 jpeg 업로드 가능
		} else {
			// 그외에 업로드 불가
		}
		
		System.out.println(originName+"<--originName");
		int lastDot = originName.lastIndexOf("."); // 마지막끝자를 찾는 위치
		String extension = originName.substring(lastDot); //섭스트링으로 자른 값
		
		// 새로운 이름 생성기능 : UUID
		// 1. db에서 저장
		String memberPic = memberForm.getMemberId()+extension; 
		// 멤버 타입에 id, pw, addr, name, phone에 폼타입을 넣는다
		Member member = new Member();
		member.setMemberId(memberForm.getMemberId());
		member.setMemberPw(memberForm.getMemberPw());
		member.setMemberAddr(memberForm.getMemberAddr());
		member.setMemberEmail(memberForm.getMemberEmail());
		member.setMemberName(memberForm.getMemberName());
		member.setMemberPhone(memberForm.getMemberPhone());
		member.setMemberPic(memberPic);
		System.out.println(member+"<-------------addmemberService");
		int row = memberMapper.insertMember(member);
		
		// 2. 파일저장 /업로드 위치에 파일저장
		String path = "D:\\it\\git-cashbook\\cashbook\\src\\main\\resources\\static\\upload";
		File file = new File(path+"\\"+memberPic); //새로운 파일 생성
		try {
			multif.transferTo(file);
		} catch (Exception e) {
			//1번 에러를 2번에러로 변환했다
			e.printStackTrace();
			throw new RuntimeException(); // 또다른 예외 
			// Exception <-모든 에러의 부모 
			//1. 예외처리를 해야만 문법적으로 이상없는 예외
			//2. RuntimeException 예외처리를 코드에서 구현하지 않아도 아무문제없는 예외
		}  
		return row;
	}
}
