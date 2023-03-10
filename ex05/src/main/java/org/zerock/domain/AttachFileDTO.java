package org.zerock.domain;

public class AttachFileDTO {
	//원본파일 이름 (그 파일 자체 이름)
	private String fileName;
	//업로드 경로 ( 날짜별로 저장을 시켜서, 업로드 경로가 달라서)
	private String uploadPath;
	//uuid 값
	private String uuid;
	//이미지 여부
	private boolean image;
	
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUploadPath() {
		return uploadPath;
	}
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public boolean isImage() {
		return image;
	}
	public void setImage(boolean image) {
		this.image = image;
	}
	
	@Override
	public String toString() {
		return "AttachFileDTO [fileName=" + fileName + ", uploadPath=" + uploadPath + ", uuid=" + uuid + ", image="
				+ image + "]";
	}
	
	

}
