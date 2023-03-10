package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import net.coobird.thumbnailator.Thumbnailator;

@Controller
public class UploadController {
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		//getFolder();
	}
	
	@GetMapping("uploadAjax")
	public void uploadAjax() {
		
	}
		
	
	@PostMapping("uploadFormAction")
	public void uploadFormAction(MultipartFile[] file1) {  //uploadFile 이렇게 하면 보낸 이름이 file1이라서 오류뜸! 같은 이름으로 파라미터 받아와야함
		for(MultipartFile file:file1) {
			System.out.println("파일이름"+file.getOriginalFilename());
			System.out.println("파일사이즈"+file.getSize());
			System.out.println("파라미터이름"+file.getName());
//			System.out.println("이건몰까?"+file.getContentType());
			File dest =new File("c:/upload",file.getOriginalFilename() );
			try {
				file.transferTo(dest);  //파일 저장
			} catch (Exception e) {
				System.out.println("파일 저장오류 발생");
				e.printStackTrace();
			}  			
		}		
	}
	
	
	
	
}
