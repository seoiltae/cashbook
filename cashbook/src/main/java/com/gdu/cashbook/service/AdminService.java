package com.gdu.cashbook.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.AdminMapper;
import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.mapper.CashMapper;
import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.mapper.MemberidMapper;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.LoginAdmin;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.Memberid;

@Service
@Transactional
public class AdminService {
	@Autowired private AdminMapper adminMapper;
	@Autowired private MemberMapper memberMapper;
	@Autowired private CashMapper cashMapper;
	@Autowired private BoardMapper boardMapper;
	@Autowired private MemberidMapper memberidMapper;
	@Value("/seoit11/tomcat/webapps/cashbook/WEB-INF/classes/static/upload/")
	private String path;

	//관리자 게시판 삭제
	public int removeBoardByAdmin(int boardNo) {
		int row = adminMapper.deleteBoardByAdmin(boardNo);
		return row;
	}
	//관리자 게시판관리
	public List<Board> selectBoardList(String searchBo) {
		Map<String, Object> map = new HashMap<>();
		List<Board> list = boardMapper.selectBoardList(searchBo);
		System.out.println(searchBo+"<---------------------AdminService searchBo");
		map.put("searchBo", searchBo);
		return list;
	}
	//관리자의 회원정보 삭제
	public int removeMemberByAdmin(Member member, String memberId) {
		// 멤버아이디의 사진
		String memberPic = memberMapper.selectMemberPic(memberId);
		//파일지정
		File file = new File(path+memberPic);
		//멤버아이디타입 생성
		Memberid memberid = new Memberid();
		//보드타입 생성
		Board board = new Board();
		cashMapper.allDeleteByCash(memberId);
		boardMapper.allDeleteByBoard(memberId);
		if(adminMapper.deleteMemberByAdmin(member) == 1) {
			//파일 삭제
			file.delete();
			memberid.setMemberId(memberId);
			return memberidMapper.insertMemberid(memberid);
		}
		return 0;
	}
	//관리자의 회원정보 관리
	public List<Member> selectMemberList(String search) {
		Map<String, Object> map = new HashMap<>();
		List<Member> list = adminMapper.selectMemberList(search);
		map.put("search", search);
		return list;
	}
	//관리자 로그인
	public LoginAdmin admin(LoginAdmin loginAdmin) {
		return adminMapper.selectLoginAdmin(loginAdmin);
	}
}
