package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Cash;

@Mapper
public interface CashMapper {
	public List<Cash> selectCashListByDate(Cash cash); //오늘 날짜에 들어온 로그인한 회원의 수입지출목록
	public int deleteCashOne(Cash cash); // 로그인한 가계부 중 원하는 가게부 삭제
	public int selectCashKindSum(Cash cash);// 오늘 가계부 총합계
	public Cash selectCashNoByCash(Cash cash); // 로그인한 회원의 수정폼
	public int updateCashOne(Cash cash); // 로그인한 회원의 수정 액션 
}
