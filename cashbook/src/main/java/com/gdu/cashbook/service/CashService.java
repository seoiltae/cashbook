package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.CashMapper;
import com.gdu.cashbook.vo.Cash;

@Service
@Transactional
public class CashService {
	@Autowired private CashMapper cashMapper;
	//가계부 리스트
	public Map<String, Object> getCashListByDate(Cash cash) {
		List<Cash> cashList = cashMapper.selectCashListByDate(cash);
		int cashKindSum = cashMapper.selectCashKindSum(cash);
		Map<String, Object> map = new HashMap<>();
		map.put("cashList", cashList);
		map.put("cashKindSum", cashKindSum);
		return map;
	}
	//가계부 삭제(혹시모를 트랜잭션 처리를 위해 void대신 int)
	public int removeCash(Cash cash) {
		int row = cashMapper.deleteCashOne(cash);
		return row;
	}
}
