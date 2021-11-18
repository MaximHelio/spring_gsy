package aloha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aloha.domain.Board;
import aloha.mapper.BoardMapper;


@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardMapper mapper;
	
	@Override
	public List<Board> list() throws Exception {
		return mapper.list();
	}

	@Override
	public void insert(Board board) throws Exception {
		mapper.insert(board);
	}

	@Override
	public Board read(Integer boardNo) throws Exception {
		return mapper.read(boardNo);
	}

	@Override
	public void update(Board board) throws Exception {
		mapper.update(board);
		
	}

	@Override
	public void delete(Integer boardNo) throws Exception {
		mapper.delete(boardNo);
	}

	@Override
	public List<Board> list(String keyword) throws Exception {
		keyword = ( keyword == null ? "" : keyword);

		return mapper.search(keyword);
	}
	
	
}
