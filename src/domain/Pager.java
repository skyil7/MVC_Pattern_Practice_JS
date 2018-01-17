package domain;

public class Pager {
	private int totalCnt;
	private int perPageNum;
	private int perChapterNum;
	private int startPage;
	private int endPage;
	private int nowPage;
	
	private int totalPage;
	
	private boolean next;
	private boolean prev;
	
	public Pager() {
		nowPage=1;
		perPageNum=10;
		perChapterNum=5;
	}
	
	public void calc() {
		totalPage = (int) Math.ceil(totalCnt/ (double)perPageNum);
		endPage = (int)Math.ceil(nowPage / (double)perChapterNum)*perChapterNum;
		startPage = endPage -perChapterNum+1;
		prev=true;
		next=true;
		
		if(totalPage<endPage) {
			endPage=totalPage;
			next=false;
		}
		if(startPage == 1) {
			prev=false;
		}
	}

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	public int getPerPageNum() {
		return perPageNum;
	}

	public void setPerPageNum(int perPageNum) {
		this.perPageNum = perPageNum;
	}

	public int getPerChapterNum() {
		return perChapterNum;
	}

	public void setPerChapterNum(int perChapterNum) {
		this.perChapterNum = perChapterNum;
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

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	
	
}
