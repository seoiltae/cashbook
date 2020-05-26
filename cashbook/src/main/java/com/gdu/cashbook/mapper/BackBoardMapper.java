package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.BackBoard;

@Mapper
public interface BackBoardMapper {
	public int insertBackBoard(BackBoard backBoard); //게시판 삭제 시 백업 게시판 추가
}
