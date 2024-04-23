package com.kh.spring.board.service;

import java.util.ArrayList;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

public interface BoardService {

	// 게시글 총 개수 가져오기
	int selectListCount();
	
	// 게시글 리스트 조회
	ArrayList<Board> selectBoardList(PageInfo pi);

	// 조회수 증가
	int increaseCount(int bno);
	
	// 게시글 select
	Board selectBoard(int bno);

	// 댓글 목록 조회
	ArrayList<Reply> selectReplyList(int bno);

	// 게시글 추가
	int insertBoard(Board board);

	// 게시글 수정
	int updateBoard(Board board);

}
