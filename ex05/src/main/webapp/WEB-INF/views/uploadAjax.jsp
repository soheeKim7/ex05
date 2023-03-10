<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>uploadAjax</title>
</head>
<body>
	업로드할 파일 <span><input type="file" multiple name="file1"></span>	  <!-- 여기서 span의 html이 그사이에 있는 input 태그들! -->
	<br>
	<!-- 여기서 span의 html이 그사이에 있는 input 태그들! -->
	<!-- 그래서 만약 <input type="file" multiple name="file1">ffff 이렇게 있어서 text파일을 읽어오면 fff만 읽힘 -->	
	<button>Ajax전송</button> <br>
	<div id="uploadResult">
		<ul>		
		
		</ul>	
		<ol>
		
		</ol>
	</div>



	<script src="https://code.jquery.com/jquery-3.6.4.js"
		integrity="sha256-a9jBBRygX1Bh5lt8GZjXDzyOB+bWve9EiO7tROUtj/E="
		crossorigin="anonymous">
	</script>
	
	<script>
	$(document).ready(function(){   
		//html에서 input타입들 같은거 다 "객체"로 정보를 가지고 있음
		//<> 태그를 객체로서 정보를 가지고 있음 DOM (document object model) 그 객체들 정보
		var cloneObj=$("span").clone();    //버튼 클릭전, input타입의 모든 정보
		console.log("DOM개체정보",cloneObj);
		console.log("추가 전 HTML내용",cloneObj.html());   //html은 <input type="file" multiple name="file1">이거 읽어옴
		//만약 여기서 <input type="file" multiple name="file1">fff 되어 있는데
		//cloneObj.text(); 이렇게 하면 text인 fff만 읽어온다
		
		//input에 파일첨부할때 그 파일들을 input의 값으로 가지고 있는 개념이 아니라!! html의 개념으로 가지고 있는거라서
		//그 객체의 정보를 복사해와서 그안에 html로 이용해서 초기화 해야한다!!! 
		//input의 값을 초기화 하면 안됨!! 개념이 달라!! 파일첨부는!!
		
		
		$("button").on("click",function(){
			console.log("전송버튼클릭");	
			console.log("파일을 추가했을때 HTML내용",$("span").html());
			//파일전송 가상으로 폼을 만들어서 전송
			var formData = new FormData();     				//가상폼 만들기
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
					//alert("ajax로 처리된 결과"+result);
					//받아온 내용을 가지고 작업
					
					var str2 ="";    //선언안하면 undefined 문자가 합쳐져서 나오게 됨!
					for(var i=0;i<result.length;i++){
						console.log("일반for문 이용시!!!!");
						console.log(i,result[i].fileName);
						//var name=result[i].fileName;
						//$("#uploadResult").append(name);
						var str = "<img src='/resources/list.jpg' width='25'>"+ "<li>"+ result[i].fileName + "</li>";
						$("ul").append(str);
						$("ol").append(str);						
						
						str2 += "<img src='/resources/list.jpg' width='100px'>"+ (i+1)+". "+result[i].fileName + "<br>";
						$("#uploadResult").html(str2);
						
					}
					//자바스크립트의 forEach
					result.forEach(function(data,index){	// 객체, index
						console.log("JS(자바스크립트)의 forEach 이용시!!!!");
						console.log(index,data.fileName);
					});
					
					//ajax에서 forEach
					$(result).each(function(index,data){	//  index, 객체
						console.log("ajax의 each 이용시!!!!");
						console.log(index,data.fileName);
					});
					
					
					
					//선택한 파일 초기화(원래대로 돌리기)
					$("span").html(cloneObj.html());					
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
		}//checkExtension
		
		
	});//document

	
	</script>


</body>
</html>