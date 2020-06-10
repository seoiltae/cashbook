package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Comment;
@Mapper
public interface CommentMapper {
	//게시판 마다 댓글 정보
	public List<Comment> boardByComment(Comment comment, int boardNo); 
	//댓글 작성
	public int insertComment(Comment comment);
	//댓글 수정 폼
	public Comment updateSelectComment(Comment comment);
	//댓글 수정 액션
	public int updateComment(Comment comment);
	//댓글 삭제
	public int deleteComment(Comment comment);
	//회원 탈퇴시 댓글 삭제
	public int deleteAllComment(String memberId);
}
