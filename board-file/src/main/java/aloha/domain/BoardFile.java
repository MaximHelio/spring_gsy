package aloha.domain;

import java.util.Date;

import lombok.Data;

@Data
public class BoardFile {
	
	private int fileNo;
	private int boardNo;
	private String fullName;
	private String fileName;
	private Date regDate;
}
