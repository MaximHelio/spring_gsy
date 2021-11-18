package aloha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aloha.domain.Board;
import aloha.domain.BoardFile;
import aloha.mapper.BoardMapper;
import aloha.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {
	
	// 업로드 경로
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private BoardMapper mapper;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Override
	public List<Board> list() throws Exception {
		return mapper.list();
	}

	@Override
	public void insert(Board board) throws Exception {
		
		MultipartFile[] files = board.getFile();
		

		//	글 등록 		
		mapper.insert(board);
		
		//	실제 파일 업로드
		List<BoardFile> fileList =  fileUtils.uploadFiles(files, uploadPath);

		//	DB 파일 업로드
		for (BoardFile boardFile : fileList) {
			mapper.uploadFile(boardFile);
		}
		
//		for (MultipartFile file : files) {
//			log.info("originalFileName : " + file.getOriginalFilename());
//			log.info("contentType : " + file.getContentType());
//			log.info("size : " + file.getSize());
//		}
		
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

	@Override
	public List<BoardFile> readFileList(Integer boardNo) throws Exception {
		return mapper.readFileList(boardNo);
	}

	@Override
	public BoardFile readFile(Integer fileNo) throws Exception {
		return mapper.readFile(fileNo);
	}

	@Override
	public void deleteFile(Integer fileNo) throws Exception {
		mapper.deleteFile(fileNo);
		
	}

}
