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

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import net.coobird.thumbnailator.Thumbnailator;

@RestController
public class ApiController {
	
	@GetMapping("display")
	//파일을  그림(썸네일)보내주는 컨트롤러
	public ResponseEntity<byte[]> getFile(String fileName) throws IOException {  //fileName 업로드된 파일네임과 매칭이 되서 보내줄거야, 즉 썸까지 붙은 파일네임이 보내질겨
		File file = new File("c:/upload/"+fileName);  
		//ex> c:/upload/2023/03/10/thumb_uuid_파일이름 
		//ex> c:/upload/2023/03/10/thumb_36582bdf-2ed8-444a-abf5-b6d87784a568_register.jpg
		
		//파일 보내는 방법
		HttpHeaders header = new HttpHeaders();
		header.add("content-Type", Files.probeContentType(file.toPath()));
//		byte[] result=FileCopyUtils.copyToByteArray(file);
		ResponseEntity<byte[]> result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK );
		
		return result;
		
	}
	
	
	
	
	@PostMapping("uploadAjaxAction")
	//@ResponseBody   //jsp파일을 여는게 아니라 데이터로 응답을 하겠다(즉 restcontroller 와 같은 기능)
	public List<AttachFileDTO> uploadAjaxAction(MultipartFile[] uploadFile) {  //uploadFile 파라미터 받아옴
		List<AttachFileDTO> list = new ArrayList<>();		
		
		for(MultipartFile file:uploadFile) {       //서버에서 갖고있는 MultipartFile, 컴퓨터에 저장하기 전 파일
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
			
			String uploadFileName=uuid.toString()+"_"+file.getOriginalFilename();
			File dest =new File(uploadPath, uploadFileName );  //저장할 파일위치, 저장할 파일이름을 선택해라!
			boolean isImage=checkImageType(dest);
			try {
				file.transferTo(dest);  // 실제로 파일 저장
				System.out.println("확인 : "+checkImageType(dest));   //이미지 파일인지 확인  
				//저장 전에 확인해도 상관없음, 파일이름에 확장자이름 있어서
				//하지만 썸네일일때 저장한 파일을 가지고 하는거라서 저장후 확인하는게 더 맞아
				
				//올린 파일을 이미지 파일이 있는지 검사
				if(isImage) {  			//이미지 파일이면 썸네일 파일 만들자
					FileOutputStream thumbnailFile=new FileOutputStream (new File(uploadPath, "thumb_"+uploadFileName ));
					//(원래는 10mb 파일을 한꺼번에 받아서 저장하는 개념인데) 
					//스트림이라는 개념이 그 10mb를 물흐르듯이 조금씩 조금씩 흘러가면서 받아서 바로바로 보여주도록 처리하는것
					//썸네일라이브러리 메소드에서, 이 스트림이라는 개념을 이용해서 처리하기때문에 이게 필요
					//그래서 그 스트림으로 받아온 객체에, 서버에 있는 file을 이용해서 처리
					
					//썸네일 라이브러리를 이용해서 썸네일 파일 만들기
//					Thumbnailator.createThumbnail( 원본파일(MultipartFile,서버에 갖고있는 file) , 저장할 파일이름, 가로, 세로)
					Thumbnailator.createThumbnail(file.getInputStream(), thumbnailFile, 100, 100);
					thumbnailFile.close();	 //스트림처리하고 있는걸 닫는거! 							
				}			
				
			} catch (Exception e) {
				System.out.println("파일 저장오류 발생");
				e.printStackTrace();
			}  		
			
			AttachFileDTO dto =new AttachFileDTO();
			dto.setFileName(file.getOriginalFilename());
			dto.setUploadPath(getFolder());  //그 선택한 폴더이름 
			dto.setUuid(uuid.toString());
			dto.setImage(isImage);
			list.add(dto);					
		}//for			
		return list;
	}		
	
	
	//해당날짜 폴더 만들기위한 폴더 이름 리턴
	private String getFolder() {
		Date date = new Date();   //현재시간 가져오기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  //출력할 포맷만들기
		
//			String str=sdf.format(date);  //현재날짜 포맷형태로 가져오기		
//			System.out.println("현재날짜 : "+str);
//			System.out.println("폴더구조 : "+str.replace("-", File.separator));   //yyyy-MM-dd 일때 -> yyyy\MM\dd 로 찍힘
		return sdf.format(date);		
	}
	// 2023/03/09 -> 폴더 3개가 생기게 됨!

	
	//이미지파일 검사 후 true/false 리턴(true=이미지파일)
	private boolean checkImageType(File file) {
		try {
			String contentType=Files.probeContentType(file.toPath());  //확장자 명으로 종류파악(그래서 현재는 임의로 확장자 바꾸면 jpg 도 읽어옴)
			System.out.println("파일 종류는 : "+contentType);
//				if(contentType==null)
//					return false;
//				else
				return contentType.startsWith("image"); 				 //image 글자로 시작하면 true 리턴
		} catch (IOException e) {   									//IO 이셉션으로 null 말고, 이미지파일 분석일때만 오류일때만 찍고
			System.out.println("이미지 파일 분석 오류");    			//시스템이나 파일시스템(파일이 깨질때) 즉, 이미지파일일때 깨지면 발생
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("null 값! 없는 파일 종류~!!");
		}
		return false;
	}

}
