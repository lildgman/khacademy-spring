package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.board.service.BoardService;
import com.kh.spring.common.model.vo.PageInfo;
import com.kh.spring.common.template.Pagination;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	
	@RequestMapping("list.bo")
	public String selectList(@RequestParam(value="cpage", defaultValue="1") int currentPage, Model model) {
		int boardCount = boardService.selectListCount();
		
		PageInfo pi = Pagination.getPageInfo(boardCount, currentPage, 10, 5);
		ArrayList<Board> list = boardService.selectBoardList(pi);
		
		model.addAttribute("list", list);
		model.addAttribute("pi", pi);
		
		return "board/boardListView";
	}
	
	@RequestMapping("detail.bo")
	public String selectBoard(int bno, Model model) {
		
		int result = boardService.increaseCount(bno);
		
		if(result > 0) {
			Board board = boardService.selectBoard(bno);
			model.addAttribute(board);
			return "board/boardDetailView";
		} else {
			model.addAttribute("errorMsg", "게시글 조회 실패");
			return "common/errorPage";
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value = "rlist.bo", produces="application/json; charset-UTF-8")
	public String ajaxSelectReplyList(int bno) {
		ArrayList<Reply> list = boardService.selectReplyList(bno);
		return new Gson().toJson(list);
	}
	
	@RequestMapping("enrollForm.bo")
	public String enrollForm() {
		return "board/boardEnrollForm";
	}
	
	@RequestMapping("insert.bo")
	public String insertBoard(Board board, MultipartFile upfile, HttpSession session, Model model) {
		
		System.out.println(board);
		System.out.println(upfile);
		
		// 전달된 파일이 있을 경우 => 파일이름을 변경 => 서버에 저장 => 원본명, 서버에 업로드된 경로를 board객체에 담기
		if(!upfile.getOriginalFilename().equals("")) {
			String changeFileName = saveFile(upfile, session);
			
			// board객체에 set 해주자
			board.setOriginName(upfile.getOriginalFilename());
			board.setChangeName("resources/uploadFiles/"+changeFileName);
			
		}
		
		// board객체가 완성되어있다.
		// DB에 저장하자
		int result = boardService.insertBoard(board);
		
		if(result > 0) { // 성공 => list페이지로 이동하자
			session.setAttribute("alertMsg", "게시글 작성 성공");
			return "redirect:list.bo";
			
		} else { // 실패 => 에러페이지
			model.addAttribute("errorMsg","게시글 작성 실패");
			return "common/errorPage";
		}
		

	}
	
	// 실제 넘어온 파일의 이름을 변경해서 서버에 저장하는 메서드, 원래는 따로 빼서 만들어야함
	public String saveFile(MultipartFile upfile, HttpSession session) {
		// 파일명 수정 후 서버에 업로드하기("imgFile.jpg => 2024042310043505488.jpg")
		String originName = upfile.getOriginalFilename();
		
		// 년월일시분초
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	
		// 5자리랜덤값
		int ranNum =(int)(Math.random() * 90000) + 10000;
		// 확장자
		String ext = originName.substring(originName.lastIndexOf("."));
		
		// 수정된 첨부파일명
		String changeName = currentTime + ranNum + ext; // ex) 2024042310043505488.jpg
		
		// 첨부파일을 저장한 폴더의 물리적 경로 (session)
		String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/");
	
		try {
			upfile.transferTo(new File(savePath + changeName));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return changeName;
	}
	
	@RequestMapping("updateForm.bo")
	public String updateForm(int bno, Model model) {
		
		model.addAttribute("board",boardService.selectBoard(bno));
		return "board/boardUpdateForm";
	}
	
	@RequestMapping("update.bo") // @ModelAttribute: 객체가 넘어올때는 @ModelAttribute가 생략되어있다 
	public String updateBoard(Board board, MultipartFile reupfile, HttpSession session, Model model) {
		
		// 새로운 첨부파일이 넘어온 경우
		if(!reupfile.getOriginalFilename().equals("")) {
			// 기존파일이 있을수도, 없을수도 있다.
			if(board.getOriginName() != null) { // 기존 첨부파일이 있다.
				// 기존 파일 삭제
				new File(session.getServletContext().getRealPath(board.getChangeName())).delete();
				
			}
			// 새로 넘어온 첨부파일을 서버에 업로드시키기
			String changeName = saveFile(reupfile, session);
			// board객체에 새로운 파일로 변경해주자
			board.setOriginName(reupfile.getOriginalFilename());
			board.setChangeName("resources/uploadFiles/"+changeName);
			
		}
		
		/*
		 * board에 boardTitle, boardContent가 들어있다.
		 * 1. 새로운 첨부파일 x, 기존 첨부파일 x
		 * => originName : null, changeName : null
		 * 
		 * 2. 새로운 첨부파일 x, 기존 첨부파일 O
		 * => originName : 기존첨부파일 이름, changeName : 기존 첨부파일 경로
		 * 
		 * 3. 새로운 첨부파일 O, 기존 첨부파일 O
		 * => originName : 새로운 첨부파일 이름, changeName : 새로운 첨부파일 경로
		 * 
		 * 4. 새로운 첨부파일 O, 기존첨부파일 x
		 * => originName: 새로운첨부파일 이름, changeName: 새로운 첨부파일 경로
		 */
		
		int result = boardService.updateBoard(board);
		
		if(result > 0) { // 성공
			session.setAttribute("alertMsg", "게시글 수정 성공");
			return "redirect:detail.bo?bno=" + board.getBoardNo();
		} else { // 실패
			model.addAttribute("errorMsg","게시글 수정 실패");
			return "common/errorPage";
		}
		
	}
		
	@ResponseBody
	@RequestMapping("rinsert.bo")
	public String ajaxInsertReply(Reply reply) {
		//성공했을 때 => success, 실패했을 때 fail
		return boardService.insertReply(reply) > 0 ? "success" : "fail";		
	}
	
	@ResponseBody
	@RequestMapping(value="topList.bo", produces="application/json; charset=UTF-8")
	public String ajaxBoardList() {
		return new Gson().toJson(boardService.selectTopBoardList());
	}
}
