package com.kh.spring.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PageInfo {

	private int listCount; // 현재 총 게시글 수
	private int currentPage; // 현재 페이지 (사용자가 요청한 페이지)
	private int pageLimit; // 하단에 보여질 페이징바의 페이지 최대개수
	private int boardLimit; // 한 페이지 내에 보여질 게시글 최대개수
	
	private int maxPage; //가장 마지막 페이지(총 페이지 수)
	private int startPage; // 페이징 바의 시작 수
	private int endPage; // 페이징 바의 마지막 끝 수

}
