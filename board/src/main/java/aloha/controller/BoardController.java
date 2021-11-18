package aloha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import aloha.domain.Board;
import aloha.service.BoardService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService service;

	//	게시글 목록-조회

	//	Spring 4.3 이전
	//	@RequestMapping(value = "/board/list", method = RequestMethod.GET)
	@GetMapping("/list")
	public String list(Model model, String keyword) throws Exception {
		
		keyword = ( keyword == null ? "" : keyword);
		
		//		게시글 목록 요청
		List<Board> list = service.list(keyword);
		
		//		게시글 목록 모델에 추가
		model.addAttribute("list", list);
		
		return "board/list";
	}
	
	//	게시글 쓰기-화면
	@GetMapping("/insert")
	public String insertForm(Model model, Board board) throws Exception {
		return "board/insert";
	}
	
	//	게시글 쓰기-처리
	@PostMapping("/insert")
	public String insert(Model model, Board board) throws Exception {
		log.info(board.toString());
		
		// 게시글 쓰기 요청
		service.insert(board);

		model.addAttribute("msg", "등록이 완료되었습니다");
		
		return "board/success";
	}
	
//	게시글 읽기-화면
	@GetMapping("/read")
	public String read(Model model, Integer boardNo) throws Exception {
		// 게시글 읽기 요청
		Board board = service.read(boardNo);
		model.addAttribute("board", board);
		
		return "board/read";
	}
	
// 	게시글 수정-화면
	@GetMapping("/update")
	public String updateForm(Model model, Integer boardNo) throws Exception {
		
		// 게시글 읽기 요청
		Board board = service.read(boardNo);
		model.addAttribute("board", board);
		return "board/update";
	}
	
//  게시글 수정 - 처리
	@PostMapping("/update")
	public String update(Model model, Board board) throws Exception {
		
		//게시글 수정 요청
		service.update(board);
		
		model.addAttribute("msg", "수정이 완료되었습니다.");
		
		return "board/success";
	}
//	게시글 삭제 - 처리
	@PostMapping("/delete")
	public String delete(Model model, Integer boardNo) throws Exception {
		
		// 게시글 삭제 요청
		service.delete(boardNo);
		
		model.addAttribute("msg", "삭제가 완료되었습니다.");
		
		return "board/success";
	}
}
