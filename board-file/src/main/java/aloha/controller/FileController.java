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
	 * ���� �ٿ�ε�
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
		
		// �ٿ�ε��� ����
		File file = new File(fullName);
		
		//������ �о���� �������
		FileInputStream fileInputStream = null;
		ServletOutputStream servletOutputStream = null;
		
		try {
			String downloadFileName = null;
			
			// ������� ��û���� ������ ������� ex. windows os ������ ����...
			String browser = request.getHeader("User-Agent");
			
			//��������, ���ϸ� ���ڵ�
			if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){//������ Ȯ�� ���ϸ� encode  
				 downloadFileName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
		    }else{
		    	downloadFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		    }
			
		    // response ��� �ٿ�ε� ���ϸ�, ���� ���ڵ� �� ����
		    response.setHeader("Content-Disposition","attachment;filename=\"" + downloadFileName+"\"");
		    response.setContentType("text/html");
		    response.setHeader("Content-Transfer-Encoding", "binary;");
			
		    fileInputStream = new FileInputStream(file);
		    servletOutputStream = response.getOutputStream();
		    
		    //  ���� �ٿ�ε�
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
	 * ���� �ٿ�ε�
	 * @param request
	 * @param response
	 * @param boardNo
	 * @param title
	 * @throws Exception
	 */
//	void => ���� ����� ������ ���� // ��ΰ� ���� ������ ������ void�� �൵ ��
	@GetMapping("/zip")
	public void zipDownload(HttpServletRequest request, HttpServletResponse response,
							Integer boardNo, String title) throws Exception {
		
		// �۹�ȣ�� ���� ��� ÷������ ��� ��ȸ
		List<BoardFile> fileList = service.readFileList(boardNo);
		
		// �ٿ�ε� ���ϸ�
		String zipFile = "temp.zip";
		String downloadFileName = title;
		
		// ���� ���ڵ�
		String browser = request.getHeader("User-Agent");
		if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){//������ Ȯ�� ���ϸ� encode  
		   downloadFileName = URLEncoder.encode(downloadFileName,"UTF-8").replaceAll("\\+", "%20");
		    
		}else{
		   downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO-8859-1");
		}

		//������ ������ ���� ����
		response.setContentType("application/zip");
		response.addHeader("Content-Disposition", "attachment; filename=" + downloadFileName + ".zip");
		
		// ������ ������ �� �������� �����ϱ�
		try {
			
			FileOutputStream fout = new FileOutputStream(zipFile);
			ZipOutputStream zout = new ZipOutputStream(fout);
			
			for (int i = 0; i < fileList.size(); i++) {
				
				// �������� ���� ���� �׸��� �߰�
				ZipEntry zipEntry = new ZipEntry(fileList.get(i).getFileName());
				zout.putNextEntry(zipEntry);
				
				FileInputStream fin = new FileInputStream(fileList.get(i).getFullName());
				byte[] buffer = new byte[1024];
				int data =0;
				
				while( (data = fin.read(buffer)) > 0) {
					zout.write(buffer, 0, data);
				}
				
				//�������� �� �׸��� ���� �����͸� ����

				//FileCopyUtils.copy(fin, zout);
				
				zout.closeEntry();
				fin.close();
				
			}
			zout.close();
//			������� ������ ���������� ���� ��
			
			FileInputStream fis = new FileInputStream(zipFile);
			ServletOutputStream sos = response.getOutputStream();
			
			
			// �ٿ�ε�
			FileCopyUtils.copy(fis, sos);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ε� ���� ����
	 * @param fileNo
	 * @param fullName
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("")
	@ResponseBody
	public ResponseEntity<String> deleteFile(Integer fileNo) throws Exception {
		
		// ���� ����
		BoardFile boardFile = service.readFile(fileNo);
		String fullName = boardFile.getFileName();
		
		// ������ ���� ��ü
		File file = new File(fullName);
		
		// ������ ������ �����ϴ��� Ȯ��
		if (file.exists()) {
			if (file.delete()) {				
				log.info("������ ����: " + fullName);
				log.info("���� ���� ����");
			} else {
				log.info("���� ���� ����");
			}
		} else {
			log.info("����(����):" + fullName);
			log.info("������ �������� �ʽ��ϴ�");
		}
		
		service.deleteFile(fileNo);
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
}
