package aloha.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import aloha.domain.BoardFile;
import aloha.service.BoardService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	private BoardService service;
	
	/**
	 * 파일 다운로드
	 * @param request
	 * @param response
	 * @param fileNo
	 * @throws Exception
	 */
	@GetMapping("")
	public void fileDownload(HttpServletRequest request
							, HttpServletResponse response, Integer fileNo) throws Exception {
		
		BoardFile boardFile = service.readFile(fileNo);
		
		String fullName = boardFile.getFullName();
		String fileName = boardFile.getFileName();
		
		// 다운로드할 파일
		File file = new File(fullName);
		
		//파일을 읽어오는 연결통로
		FileInputStream fileInputStream = null;
		ServletOutputStream servletOutputStream = null;
		
		try {
			String downloadFileName = null;
			
			// 헤더에는 요청자의 정보를 담고있음 ex. windows os 브라우저 정보...
			String browser = request.getHeader("User-Agent");
			
			//브라우저별, 파일명 인코딩
			if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){//브라우저 확인 파일명 encode  
				 downloadFileName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
		    }else{
		    	downloadFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		    }
			
		    // response 헤더 다운로드 파일명, 전송 인코딩 등 세팅
		    response.setHeader("Content-Disposition","attachment;filename=\"" + downloadFileName+"\"");
		    response.setContentType("text/html");
		    response.setHeader("Content-Transfer-Encoding", "binary;");
			
		    fileInputStream = new FileInputStream(file);
		    servletOutputStream = response.getOutputStream();
		    
		    //  파일 다운로드
		    FileCopyUtils.copy(fileInputStream, servletOutputStream);
		    
//		    byte b [] = new byte[1024];
//		    int data = 0;
//		    
//		    while ((data=(fileInputStream.read(b, 0, b.length))) != -1) {
//		    	servletOutputStream.write(b, 0, data);
//		    }
//		    
//		    servletOutputStream.flush();
		    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (servletOutputStream != null) {
				try {
					servletOutputStream.close();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 압축 다운로드
	 * @param request
	 * @param response
	 * @param boardNo
	 * @param title
	 * @throws Exception
	 */
//	void => 같은 경로의 파일을 응답 // 경로가 같은 파일이 있으면 void를 줘도 됨
	@GetMapping("/zip")
	public void zipDownload(HttpServletRequest request, HttpServletResponse response,
							Integer boardNo, String title) throws Exception {
		
		// 글번호에 따른 모든 첨부파일 목록 조회
		List<BoardFile> fileList = service.readFileList(boardNo);
		
		// 다운로드 파일명
		String zipFile = "temp.zip";
		String downloadFileName = title;
		
		// 파일 인코딩
		String browser = request.getHeader("User-Agent");
		if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){//브라우저 확인 파일명 encode  
		   downloadFileName = URLEncoder.encode(downloadFileName,"UTF-8").replaceAll("\\+", "%20");
		    
		}else{
		   downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO-8859-1");
		}

		//전송할 파일의 형식 지정
		response.setContentType("application/zip");
		response.addHeader("Content-Disposition", "attachment; filename=" + downloadFileName + ".zip");
		
		// 파일을 전송할 때 압축파일 생성하기
		try {
			
			FileOutputStream fout = new FileOutputStream(zipFile);
			ZipOutputStream zout = new ZipOutputStream(fout);
			
			for (int i = 0; i < fileList.size(); i++) {
				
				// 압축파일 내에 파일 항목을 추가
				ZipEntry zipEntry = new ZipEntry(fileList.get(i).getFileName());
				zout.putNextEntry(zipEntry);
				
				FileInputStream fin = new FileInputStream(fileList.get(i).getFullName());
				byte[] buffer = new byte[1024];
				int data =0;
				
				while( (data = fin.read(buffer)) > 0) {
					zout.write(buffer, 0, data);
				}
				
				//압축파일 내 항목의 파일 데이터를 복사

				//FileCopyUtils.copy(fin, zout);
				
				zout.closeEntry();
				fin.close();
				
			}
			zout.close();
//			여기까지 서버에 압축파일을 만든 것
			
			FileInputStream fis = new FileInputStream(zipFile);
			ServletOutputStream sos = response.getOutputStream();
			
			
			// 다운로드
			FileCopyUtils.copy(fis, sos);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 업로드 파일 삭제
	 * @param fileNo
	 * @param fullName
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("")
	@ResponseBody
	public ResponseEntity<String> deleteFile(Integer fileNo) throws Exception {
		
		// 파일 삭제
		BoardFile boardFile = service.readFile(fileNo);
		String fullName = boardFile.getFileName();
		
		// 삭제할 파일 객체
		File file = new File(fullName);
		
		// 실제로 파일이 존재하는지 확인
		if (file.exists()) {
			if (file.delete()) {				
				log.info("삭제한 파일: " + fullName);
				log.info("파일 삭제 성공");
			} else {
				log.info("파일 삭제 실패");
			}
		} else {
			log.info("삭제(실패):" + fullName);
			log.info("파일이 존재하지 않습니다");
		}
		
		service.deleteFile(fileNo);
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
}
