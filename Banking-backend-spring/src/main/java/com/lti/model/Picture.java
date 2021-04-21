package com.lti.model;

import org.springframework.web.multipart.MultipartFile;

public class Picture {

	private long referenceId;
	private MultipartFile aadharPic;
	private MultipartFile panPic;
	private MultipartFile lightBill;
	private MultipartFile gstProof;
	
	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}
	public long getReferenceId() {
		return referenceId;
	}
	public MultipartFile getAadharPic() {
		return aadharPic;
	}
	public void setAadharPic(MultipartFile aadharPic) {
		this.aadharPic = aadharPic;
	}
	public MultipartFile getPanPic() {
		return panPic;
	}
	public void setPanPic(MultipartFile panPic) {
		this.panPic = panPic;
	}
	public MultipartFile getLightBill() {
		return lightBill;
	}
	public void setLightBill(MultipartFile lightBill) {
		this.lightBill = lightBill;
	}
	public MultipartFile getGstProof() {
		return gstProof;
	}
	public void setGstProof(MultipartFile gstProof) {
		this.gstProof = gstProof;
	}
}
