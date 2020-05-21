package com.gdu.cashbook.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Category;
@Mapper
public interface CategoryMapper {
	public List<Category> selectCategoryList(); //가계부 카테고리 리스트
}
