<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	업로드할 파일
	<input type="file" multiple name="file1">
	<br>
	<button>Ajax전송</button>



	<script src="https://code.jquery.com/jquery-3.6.4.js"
		integrity="sha256-a9jBBRygX1Bh5lt8GZjXDzyOB+bWve9EiO7tROUtj/E="
		crossorigin="anonymous">
	</script>
	
	<script>
	$(document).ready(function(){   
		$("button").on("click",function(){
			console.log("전송버튼클릭");	
			//파일전송 가상으로 폼을 만들어서 전송
			var formData = new FormData();
			var inputFile=$("input[name='file1']"); 		 //입력한 파일 관련 내용읽기
			console.log("inputFile의 객체 정보",inputFile);
			console.log("파일 정보",inputFile[0].files);
			var files=inputFile[0].files;     				//파일들
			for(var i=0;i<files.length;i++){				//파일의 개수 만큼 반복
				//파일을 검사해서 문제있으면 그냥 끝내기
				if(!checkExtension(files[i].name,files[i].size)){
					return false;
				}									
				formData.append("uploadFile",files[i]);		//name, value  //보내는 파라미터이름, 그 값		
			};
			
			//ajax 이용해서 formData를 보냄
			$.ajax({
				url:"/uploadAjaxAction" ,
				type:"post" ,
				data:formData ,  //보내는 데이터
				success:function(result){
					console.log("result 타입 봐보자",typeof result);
					console.log("받아온 결과값 봐보자~",result);
					alert("ajax로 처리된 결과"+result);
				} ,
				//파일업로드 ajax 처리시 주의사항/파일로 순수하게 보내라고! 파일로 인식되라구~ 꼭 필요
				processData: false ,
				contentType: false				
			});//ajax			
		});//button	
		
		//파일의 확장자 제한, 파일 크기 제한 
		var maxSize=5242880; //5MB =1024*1024*5
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
			
		//확인을 위한 파일이름, 파일사이즈를 넣어주면 확인해서 true,false 리턴
		function checkExtension(fileName,fileSize){
				if(fileSize>maxSize){
					alert("큰파일이다!");
					return false;					
				}
				if(regex.test(fileName)){  //정규식이랑 매칭이 되면 true, 아니면 false
					alert("실행or압축파일은 금지다!");
					return false;
				}
				return true;				
			}
		
		
	});//document

	
	</script>


</body>
</html>