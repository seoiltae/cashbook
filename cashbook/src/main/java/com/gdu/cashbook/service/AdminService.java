package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.AdminMapper;
import com.gdu.cashbook.vo.LoginAdmin;
import com.gdu.cashbook.vo.Member;

@Service
@Transactional
public class AdminService {
	@Autowired private AdminMapper adminMapper;
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
