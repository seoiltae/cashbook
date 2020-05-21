package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.CashMapper;
import com.gdu.cashbook.mapper.CategoryMapper;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.Category;
import com.gdu.cashbook.vo.DayAndPrice;

@Service
@Transactional
public class CashService {
	@Autowired private CashMapper cashMapper;
	@Autowired private CategoryMapper categoryMapper;
	//일별 가계부 총합계리스트
	public List<DayAndPrice> getCashAndPriceList(String memberId, int year, int month) {
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("year", year);
		map.put("month", month);
		return cashMapper.selectDayAndPriceList(map);
	}
	//가계부 리스트
	public Map<String, Object> getCashListByDate(Cash cash) {
		List<Cash> cashList = cashMapper.selectCashListByDate(cash);
		int cashKindSum = cashMapper.selectCashKindSum(cash);
		Map<String, Object> map = new HashMap<>();
		map.put("cashList", cashList);
		map.put("cashKindSum", cashKindSum);
		return map;
	}
	// 가계부 추가 액션
	public int addCash(Cash cash) {
		int row = cashMapper.insertCashOne(cash);
		return row;
	}
	//가계부 삭제(혹시모를 트랜잭션 처리를 위해 void대신 int)
	public int removeCash(Cash cash) {
		int row = cashMapper.deleteCashOne(cash);
		return row;
	}
	
	//가계부 수정 폼
	public Cash modifyCash(Cash cash) {
		return cashMapper.selectCashNoByCash(cash);
	}
	//가계부 수정 액션
	
	//카테고리 목록 //가계부 입력 시 가계부 수정 시 카테고리 선택
	public List<Category> getSelectCategoryList() {
		return categoryMapper.selectCategoryList();
	}
}
