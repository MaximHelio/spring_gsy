package aloha.service;

import java.util.List;

import aloha.domain.Board;
import aloha.domain.BoardFile;

public interface BoardService {
	// �Խñ� ���
	public List<Board> list() throws Exception;
	
	// �Խñ� ����
	public void insert(Board board) throws Exception;
	
	//�Խñ� �б�
	public Board read(Integer boardNo) throws Exception;
	
	// �Խñ� ����
	public void update(Board board) throws Exception;
	
	// �Խñ� ����
	public void delete(Integer boardNo) throws Exception;

	// �Խñ� �˻�
	public List<Board> list(String keyword) throws Exception;
	
	// ���� ���
	public List<BoardFile> readFileList(Integer boardNo) throws Exception;

	// ���� ��ȸ
	public BoardFile readFile(Integer fileNo) throws Exception;

	// ���� ����
	public void deleteFile(Integer fileNo) throws Exception;
}
