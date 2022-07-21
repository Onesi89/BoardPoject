package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Page {
	// 현재 페이지
	private int presentPage;
	// 총 페이지
	private int totalPage;
	// 총 컨텐츠
	private int MaxRownum;

	// 페이지 번호 나열
	private List<Integer> pageArray;

	public List<Integer> getPageArray() {
		return pageArray;
	}

	// 클라이언트에서 전송 받은 현재 페이지 수
	public void setPageArray(int presentPage, int totalPage) {
		List<Integer> list = new ArrayList<Integer>();
		//스타트 페이지 정하기 boolean 값 true 앞으로 가기 눌렀을 때
			//첫번째 페이지 구하기
			int firstPage = 0;
			
		
			if(presentPage % 5 != 0) {
				firstPage = (int)(presentPage/5)*5+1;
			}else {
				firstPage = (int)((presentPage/5)-1)*5+1;
			}
		
			//마지막 페이지 구하기
			int lastPage;
			if(firstPage + 4 > totalPage) {
				lastPage = totalPage;
			}else {
				lastPage = firstPage + 4 ;
			}
			
			for(int i = firstPage; i<=lastPage;i++) {
				 list.add(i);	
			
		
			this.pageArray = list;
	}
			}

	// 클라이언트에서 전송 받은 현재 페이지 수
	public int getPresentPage() {
		return presentPage;
	}

	public void setPresentPage(int presentPage) {
		this.presentPage = presentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	// 게시글 최대 줄 수 에서 15로 나눈 값으로 페이지 설정(15가 보기 이쁨)
	public void setTotalPage(int MaxRownum) {
		int totalPage;
		if (MaxRownum % 15 != 0) {
			totalPage = (int) (MaxRownum / 15);
		} else {
			totalPage = (int) (MaxRownum / 15) + 1;
		}
		this.totalPage = totalPage;
	}

	public int getMaxRownum() {
		return MaxRownum;
	}

	// DB에서 처리하고 나올 예정
	public void setMaxRownum(int maxRownum) {
		MaxRownum = maxRownum;
	}

}
