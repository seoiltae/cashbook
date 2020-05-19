package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Cash;

@Mapper
public interface CashMapper {
	public List<Cash> selectCashListByDate(Cash cash); //오늘 날짜에 들어온 로그인한 회원의 수입지출목록

}
