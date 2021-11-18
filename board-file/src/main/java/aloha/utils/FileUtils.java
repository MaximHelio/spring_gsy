package aloha.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import aloha.domain.BoardFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
//Component는 bean을 등록하는 것
// 개발자가 직접 작성한 클래스를 Bean으로 등록
public class FileUtils {
	
	/**
	 * 파일 업로드
	 * @param files
	 * @param uploadPath
	 * @return
	 * @throws Exception
	 */
	
	public List<BoardFile> uploadFiles(MultipartFile[] files, String uploadPath) throws Exception {
		
		ArrayList<BoardFile> fileList = new ArrayList<BoardFile>();
		
		// 업로드 경로에 파일 복사
		for (MultipartFile file: files) {
			
			//파일 정보 확인
			log.info("originalFileName : " + file.getOriginalFilename());
			log.info("contentType : " + file.getContentType());
			log.info("size : " + file.getSize());
			
			// 파일 존재여부 확인 / 덮어쓰기 될 수 있음 /  윈도우 같은 경우 (1) 이런 식으로 달라지게 됨
			if (file.isEmpty())
				continue;
			// 파일명 중복 방지를 위한 고유 ID 생성
			UUID uid = UUID.randomUUID();
			
			// 실제 원본 파일 이름
			String originalFileName = file.getOriginalFilename();

			// UID_강아지.png
			String uploadFileName = uid.toString() + "_" + originalFileName;
			
			// 업로드 폴더에 업로드할 파일 복사(upload)
			// 파일 데이터
			byte[] fileData = file.getBytes();
			
			// 파일객체: ~/upload/UID_강아지.png
			File target = new File(uploadPath, uploadFileName);
			
			// fileData : 요청된 파일
			// target 	: 업로드할 파일 객체
			// 파일 복사
			FileCopyUtils.copy(fileData, target);
			
			// 업로드된 파일 전체 경로(경로 + 이름)
			String uploadedPath = uploadPath + "/" + uploadFileName;
			
			BoardFile f = new BoardFile();
			f.setFullName(uploadedPath);
			f.setFileName(originalFileName);
			fileList.add(f);
		}
		return fileList;
	}
}
