package com.kh.spring.board.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	
	@Autowired
	private BoardDao boardDao;
	
	
	@Override
	public int selectListCount() {
		return boardDao.selectListCount(sqlSession);
	}

	@Override
	public ArrayList<Board> selectBoardList(PageInfo pi) {

		return boardDao.selectList(sqlSession, pi);
	}

	@Override
	public int increaseCount(int bno) {
		
		return boardDao.increaseCount(sqlSession, bno);
	}

	@Override
	public Board selectBoard(int bno) {
		return boardDao.selectBoard(sqlSession, bno);
	}

	@Override
	public ArrayList<Reply> selectReplyList(int bno) {

		return boardDao.selectReplyList(sqlSession, bno);
	}

	@Override
	public int insertBoard(Board board) {
		return boardDao.insertBoard(sqlSession, board);
	}

	@Override
	public int updateBoard(Board board) {
		return boardDao.updateBoard(sqlSession, board);
	}

	@Override
	public int insertReply(Reply reply) {
		return boardDao.insertReply(sqlSession, reply);
	}

	@Override
	public ArrayList<Board> selectTopBoardList() {
		return boardDao.selectTopBoardList(sqlSession);
	}

	

	

}
