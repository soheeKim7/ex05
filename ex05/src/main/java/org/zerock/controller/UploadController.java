package org.zerock.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		
	}
	
	@GetMapping("uploadAjax")
	public void uploadAjax() {
		
	}
	
	@PostMapping("uploadAjaxAction")
	public void uploadAjaxAction(MultipartFile[] uploadFile) {  //uploadFile 파라미터 받아옴
		for(MultipartFile file:uploadFile) {
			System.out.println("파일이름"+file.getOriginalFilename());
			System.out.println("파일사이즈"+file.getSize());
			System.out.println("파라미터이름"+file.getName());
//			System.out.println("이건몰까?"+file.getContentType());
			File dest =new File("c:/upload",file.getOriginalFilename() );
			try {
				file.transferTo(dest);  //파일 저장
			} catch (IOException e) {
				System.out.println("파일 저장오류 발생");
				e.printStackTrace();
			}  			
		}		
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
			} catch (IOException e) {
				System.out.println("파일 저장오류 발생");
				e.printStackTrace();
			}  			
		}		
	}
	

}
