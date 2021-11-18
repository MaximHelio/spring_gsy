package aloha.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import aloha.domain.Board;

@Mapper
public interface BoardMapper {

	// 게시글 목록
	public List<Board> list() throws Exception;

	// 게시글 쓰기
	public void insert(Board board) throws Exception;

	//게시글 읽기
	public Board read(Integer boardNo) throws Exception;

	// 게시글 수정
	public void update(Board board) throws Exception;

	// 게시글 삭제
	public void delete(Integer boardNo) throws Exception;

	// 게시글 검색
	public List<Board> search(String keyword) throws Exception;
	
}
