package com.gdu.cashbook.vo;

public class Page {
	private int nowPage; //현재페이지
	private int startPage; // 시작페이지
	private int endPage; // 끝페이지
	private int total; // 게시글 총 갯수
	private int nowPerPage; // 페이지당 글 갯수
	private int lastPage; // 마지막페이지
	private int beginRow; // 몇번쨰행부터 보여줄지
	public int getNowPage() {
		return nowPage;
	}
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getNowPerPage() {
		return nowPerPage;
	}
	public void setNowPerPage(int nowPerPage) {
		this.nowPerPage = nowPerPage;
	}
	public int getLastPage() {
		return lastPage;
	}
	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}
	public int getBeginRow() {
		return beginRow;
	}
	public void setBeginRow(int beginRow) {
		this.beginRow = beginRow;
	}
	@Override
	public String toString() {
		return "Page [nowPage=" + nowPage + ", startPage=" + startPage + ", endPage=" + endPage + ", total=" + total
				+ ", nowPerPage=" + nowPerPage + ", lastPage=" + lastPage + ", beginRow=" + beginRow + "]";
	}
	
}
