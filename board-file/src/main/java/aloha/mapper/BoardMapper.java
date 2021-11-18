package aloha.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import aloha.domain.Board;
import aloha.domain.BoardFile;

@Mapper
public interface BoardMapper {

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
	public List<Board> search(String keyword) throws Exception;
	
	// ���� ���ε�
	public void uploadFile(BoardFile boardFile) throws Exception;
	
	// ���� ���
	public List<BoardFile> readFileList(Integer boardNo) throws Exception;

	// ���� ��ȸ
	public BoardFile readFile(Integer fileNo) throws Exception;

	// ���� ����
	public void deleteFile(Integer fileNo) throws Exception;
}
