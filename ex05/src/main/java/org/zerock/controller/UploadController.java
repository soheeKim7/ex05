package org.zerock.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		getFolder();
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
			
			//폴더 만들기
			File uploadPath = new File("c:/upload",getFolder());   // 그 폴더에서, 찾을 이름! // 즉 파일(폴더) 선택
			// 2023/03/09 -> 폴더 3개가 생기게 됨! 그래서 밑에 mkdirs 여러개라 s 붙여야함!
			if(!uploadPath.exists()) {    //파일이 존재하면 true 
				System.out.println("폴더없습니다");
				// 파일(디렉토리)이 없을때 폴더 만들기
				uploadPath.mkdirs();   //그 파일이 없기때문에, 그파일 이름으로 폴더를 만들어줌!  // 폴더를 만들어줌				
			}			
						
			//저장할 파일이름을 겹치지 않게 앞에 uuid를 붙여서 저장
			UUID uuid=UUID.randomUUID();  //실행할때마다 겹치지 않는 값 생성			
			
			File dest =new File(uploadPath, uuid.toString()+"_"+file.getOriginalFilename() );  //저장할 파일위치, 저장할 파일이름을 선택해라!
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
	
	//해당날짜 폴더 만들기위한 폴더 이름 리턴
	private String getFolder() {
		Date date = new Date();   //현재시간 가져오기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  //출력할 포맷만들기
		
//		String str=sdf.format(date);  //현재날짜 포맷형태로 가져오기		
//		System.out.println("현재날짜 : "+str);
//		System.out.println("폴더구조 : "+str.replace("-", File.separator));   //yyyy-MM-dd 일때 -> yyyy\MM\dd 로 찍힘
		return sdf.format(date);		
	}
	// 2023/03/09 -> 폴더 3개가 생기게 됨!

}
