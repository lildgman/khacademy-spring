package com.kh.spring.board.service;

import java.util.ArrayList;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.common.model.vo.PageInfo;

public interface BoardService {

	// 게시글 총 개수 가져오기
	int selectListCount();
	
	// 게시글 리스트 조회
	ArrayList<Board> selectBoardList(PageInfo pi);

}
