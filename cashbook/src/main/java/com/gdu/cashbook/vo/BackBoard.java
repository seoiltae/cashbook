package com.gdu.cashbook.vo;

public class BackBoard {
	private String boardTitle;
	private String bonardContents;
	private String memberId;
	private String boardDate;
	public String getBoardTitle() {
		return boardTitle;
	}
	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}
	public String getBonardContents() {
		return bonardContents;
	}
	public void setBonardContents(String bonardContents) {
		this.bonardContents = bonardContents;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getBoardDate() {
		return boardDate;
	}
	public void setBoardDate(String boardDate) {
		this.boardDate = boardDate;
	}
	@Override
	public String toString() {
		return "BackBoard [boardTitle=" + boardTitle + ", bonardContents=" + bonardContents + ", memberId=" + memberId
				+ ", boardDate=" + boardDate + "]";
	}
	
}
