package com.gdu.cashbook.service;



import java.io.File;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	@Value("D:\\it\\git-cashbook\\cashbook\\src\\main\\resources\\static\\upload\\")
	private String path;
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
	public int modifyMember(MemberForm memberForm) {
		//로그인한 멤버의 아이디의 이미지
		String originNamePic = memberMapper.selectMemberPic(memberForm.getMemberId());
		MultipartFile multif = memberForm.getMemberPic(); //입력한 멤버의 이미지를 저장
		String originName = multif.getOriginalFilename();
		System.out.println(originNamePic+"<--MemberService 수정의 originNamePic ");
		String memberPic = null;
		if(originName.equals("")) { //파일값이 null일 경우 원래 있던 파일의 이름이랑 ==
			memberPic = originNamePic;
		}else {	
			File ofile = new File(path+"\\"+originNamePic); //새로운 파일생성
			if(ofile.exists() && !originNamePic.equals("default.jpg")) {//원래파일이 아닐경우
				ofile.delete(); //파일 삭제
		}
			int lastDot = originName.lastIndexOf("."); // 마지막끝자를 찾는 위치
			String extension = originName.substring(lastDot); //섭스트링으로 자른 값
			System.out.println(extension); 
			// 1. db에서 저장
			memberPic = memberForm.getMemberId()+extension;	
			System.out.println(memberPic); // 삭제 후 이미지 저장 확인
		}
		//멤버 타입으로 형변환
		Member member = new Member();
		member.setMemberId(memberForm.getMemberId());
		member.setMemberPw(memberForm.getMemberPw());
		member.setMemberAddr(memberForm.getMemberAddr());
		member.setMemberEmail(memberForm.getMemberEmail());
		member.setMemberName(memberForm.getMemberName());
		member.setMemberPhone(memberForm.getMemberPhone());
		member.setMemberPic(memberPic);
		System.out.println(member+"MemberSerivce 수정 member");
		//file = new File(path+"\\"+memberPic);
		int row =memberMapper.updateMember(member);
		
		if(!originName.equals("")) {
			File file = new File(path+"\\"+originNamePic);
			try {
					multif.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(); // 예외처리 안해도되는 예외
			}
		}
		return row;
	}
		
	//로그인한 회원탈퇴
	public int removeMember(LoginMember loginMember) {
		//1. 멤버 이미지 파일 삭제
		//1_1 파일이름 select member_pic from member
		String memberPic = memberMapper.selectMemberPic(loginMember.getMemberId());
		//1_2 파일삭제
		File file = new File(path+memberPic); //파일지정
		
		// 2
		//1. 멤버아이디테이블에 아이디 추가
		Memberid memberid = new Memberid();
		if(memberMapper.deleteMember(loginMember) == 1 ) {	
			
			//2. 회원정보 회원탈퇴
			file.delete(); //파일 삭제
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
			String memberPic = null;
			//originName이 공백일 경우 default.jpg추가 
			if(originName.equals("")) {
				memberPic = "default.jpg";
			} else {
			System.out.println(originName+"<--originName");
			int lastDot = originName.lastIndexOf("."); // 마지막끝자를 찾는 위치
			String extension = originName.substring(lastDot); //섭스트링으로 자른 값
			// 1. db에서 저장
			memberPic = memberForm.getMemberId()+extension;
			}
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
			if(!originName.equals("")) { //originName이 공백이 아닐 경우 새로 파일 생성
				// 2. 파일저장 /업로드 위치에 파일저장
				// / linux | \\ windows <--파일경로 지정시
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
			}
			return row;
	}
}
