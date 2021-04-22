package com.lti.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lti.model.AdminGetRegisterStatus;
import com.lti.model.AdminTransactionView;
import com.lti.model.CredentialStatus;
import com.lti.model.Login;
import com.lti.model.LoginStatus;
import com.lti.model.NewPasswordStatus;
import com.lti.model.Picture;
import com.lti.model.RegisterStatus;
import com.lti.model.Status;
import com.lti.model.TransactionStatus;
import com.lti.model.Transactions;
import com.lti.entity.Account;
import com.lti.entity.AccountCredential;
import com.lti.entity.Registration;
import com.lti.entity.Transaction;
import com.lti.service.CustomerService;
import com.lti.service.ServiceException;


@RestController
@CrossOrigin
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/register")
	public RegisterStatus register(@RequestBody Registration customer) {
	
		try {
			
			long id = customerService.register(customer);
			RegisterStatus status = new RegisterStatus();
			status.setStatus(true);
			status.setMessage("Registration successfull!!!");
			status.setReferenceId(id);
			return status;
		}
		catch(ServiceException e) {
			RegisterStatus status = new RegisterStatus();
			status.setStatus(false);
			status.setMessage(e.getMessage());
			return status;
		}
	}
	
	@PostMapping("/userlogin")
	public LoginStatus login(@RequestBody Login login) {
		try {
			Account account = customerService.login(login.getCustomerId(), login.getLoginPassword());
			LoginStatus loginStatus = new LoginStatus();
			loginStatus.setStatus(true);
			loginStatus.setMessage("Login successful!");
			loginStatus.setCustomerId(account.getCustomerId());
			Registration registration = new  Registration();
			loginStatus.setName(registration.getFirstName());
			loginStatus.setName(registration.getMiddleName());
			loginStatus.setName(registration.getLastName());
			
			
			return loginStatus;
		}
		catch(ServiceException e) {
			LoginStatus loginStatus = new LoginStatus();
			loginStatus.setStatus(false);
			loginStatus.setMessage(e.getMessage());		
			return loginStatus;
		}
	}
	
	@PostMapping("/impstransaction")
	public TransactionStatus imps(@RequestBody Transactions transaction) {
		try {
			String referenceId = customerService.impsTransaction(transaction);
			
			TransactionStatus transactionStatus = new TransactionStatus();
			transactionStatus.setStatus(true);
			transactionStatus.setRefernceNo(referenceId);
			transactionStatus.setMessage("Amount has been debited from your account and will be credited to the receipent's account");
			
			return transactionStatus;
		}
		catch (ServiceException e) {
			TransactionStatus transactionStatus = new TransactionStatus();
			transactionStatus.setStatus(false);
			transactionStatus.setMessage(e.getMessage());
			return transactionStatus;
		}
	}
	
	@PostMapping("/nefttransaction")
	public TransactionStatus neft(@RequestBody Transactions transaction) {
		try {
			String referenceId = customerService.neftTransaction(transaction);
			
			TransactionStatus transactionStatus = new TransactionStatus();
			transactionStatus.setStatus(true);
			transactionStatus.setRefernceNo(referenceId);
			transactionStatus.setMessage("Amount has been debited from your account and will be credited to the receipent's account");
			
			return transactionStatus;
		}
		catch (ServiceException e) {
			TransactionStatus transactionStatus = new TransactionStatus();
			transactionStatus.setStatus(false);
			transactionStatus.setMessage(e.getMessage());
			return transactionStatus;
		}
	}
	
	@PostMapping("/rtgstransaction")
	public TransactionStatus rtgs(@RequestBody Transactions transaction) {
		try {
			String referenceId = customerService.rtgsTransaction(transaction);
			
			TransactionStatus transactionStatus = new TransactionStatus();
			transactionStatus.setStatus(true);
			transactionStatus.setRefernceNo(referenceId);
			transactionStatus.setMessage("Amount has been debited from your account and will be credited to the receipent's account");
			
			return transactionStatus;
		}
		catch (ServiceException e) {
			TransactionStatus transactionStatus = new TransactionStatus();
			transactionStatus.setStatus(false);
			transactionStatus.setMessage(e.getMessage());
			return transactionStatus;
		}
	}
	
	//below codes are for admin part...change to Admin Controller later on
	
		@GetMapping("/TransactionViewAdmin")
		public List<AdminTransactionView> transaction() {
			try {
				List<Transaction> list =  customerService.transactionViewByAdmin();
				List<AdminTransactionView> viewList = new ArrayList<AdminTransactionView>();
			
				for(Transaction t :list) {
					System.out.println(t.getTransactionId()+" ,"+t.getFromAccount().getAccountNumber()+" -> "+t.getToAccount().getAccountNumber()+" , "+t.getAmount());
					AdminTransactionView view1 = new  AdminTransactionView();
					view1.setStatus(true);
					view1.setMessage("Retrieved Transactions!");
					view1.setTransactionId(t.getTransactionId());
					view1.setFromAccount(t.getFromAccount().getAccountNumber());
					view1.setToAccount(t.getToAccount().getAccountNumber());
					view1.setAmount(t.getAmount());
					view1.setMode(t.getModeOfTransaction().name());
					view1.setRemark(t.getRemarks());
					view1.setTransactionDate(t.getTransactionDate().toLocalDate());
					viewList.add(view1);
				}
				
				return viewList;
			}
			catch(ServiceException e) {
				AdminTransactionView view = new  AdminTransactionView();
				List<AdminTransactionView> viewList = new ArrayList<AdminTransactionView>();
				view.setStatus(false);
				view.setMessage(e.getMessage());		
				return viewList;
				
				
			}
		}
		
		@GetMapping("/RequestViewByAdmin")
		public List<AdminGetRegisterStatus> requestView() {
			try {
				List<Registration> list =  customerService.RegisterRequestAction();
				List<AdminGetRegisterStatus> viewList = new ArrayList<AdminGetRegisterStatus>();
			
				for(Registration t :list) {
					//System.out.println(t.getTransactionId()+" ,"+t.getFromAccount().getAccountNumber()+" -> "+t.getToAccount().getAccountNumber()+" , "+t.getAmount());
					AdminGetRegisterStatus view1 = new  AdminGetRegisterStatus();
					view1.setStatus(true);
					view1.setMessage("Retrieved Account Request!");
					view1.setReferenceId(t.getReferenceNo());
					view1.setTitle(t.getTitle());
					view1.setFirstName(t.getFirstName());
					view1.setMiddleName(t.getMiddleName());
					view1.setLastName(t.getLastName());
					view1.setFatherName(t.getFatherName());
					view1.setMobileNo(t.getMobileNo());
					view1.setEmailId(t.getEmailId());
					view1.setAadhaarNo(t.getAadhaarNo());
					view1.setPanCard(t.getPanCard());
					view1.setDateOfBirth(t.getDateOfBirth());
					view1.setResidentialAddress(t.getResidentialAddress());
					view1.setOccupation(t.getOccupation());
					view1.setIncomeSource(t.getIncomeSource());
					view1.setAnnualIncome(t.getAnnualIncome());
					view1.setRevenueRegisterNo(t.getRevenueRegisterNo());
					view1.setGstNumber(t.getGstNumber());
					viewList.add(view1);
				}
				
				return viewList;
			}
			catch(ServiceException e) {
				AdminGetRegisterStatus status = new AdminGetRegisterStatus();
				List<AdminGetRegisterStatus> viewList = new ArrayList<AdminGetRegisterStatus>();
				status.setStatus(false);
				status.setMessage(e.getMessage());	
				viewList.add(status);
				return viewList;
			}
		}
		
		@PostMapping("/setcredential")
		public CredentialStatus setCredential(@RequestBody AccountCredential account ) {
			
			try {
				long id= customerService.updateCredential(account);
				CredentialStatus status = new CredentialStatus();
				status.setStatus(true);
				status.setMessage("updation successful !");
				status.setCustId(id);
				return status;
			}
			catch(ServiceException e) {
				CredentialStatus status = new CredentialStatus();
				status.setStatus(false);
				status.setMessage(e.getMessage());
				return status;
			}
			
		}
		
		@GetMapping("/FileViewByAdmin")
		public List<AdminGetRegisterStatus> requestFileView(@RequestParam Long ref) {
			try {
				List<AdminGetRegisterStatus> viewList = new ArrayList<AdminGetRegisterStatus>();
				Registration reg = (Registration) customerService.registerFileView(ref);
				AdminGetRegisterStatus view1 = new  AdminGetRegisterStatus();
				view1.setStatus(true);
				view1.setMessage("Retrieved Account Request!");
				view1.setAadhaarNo(reg.getAadhaarNo());
				view1.setRevenueRegisterNo(reg.getRevenueRegisterNo());
				view1.setGstNumber(reg.getGstNumber());
				view1.setPanCard(reg.getPanCard());
				view1.setAadharPic(reg.getAadharPic());
				view1.setPanPic(reg.getPanPic());
				view1.setGstProof(reg.getGstNumber());
				view1.setLightBill(reg.getLightBill());
				viewList.add(view1);
				
				return viewList;
			}
			catch(ServiceException e) {
				AdminGetRegisterStatus status = new AdminGetRegisterStatus();
				List<AdminGetRegisterStatus> viewList = new ArrayList<AdminGetRegisterStatus>();
				status.setStatus(false);
				status.setMessage(e.getMessage());	
				viewList.add(status);
				return viewList;
			}
		}
		
		@PostMapping("/SetPassword")
		public NewPasswordStatus setPassword(@RequestBody Account customer) {
		
			try {
				long id = customerService.addPassword(customer);
				NewPasswordStatus status = new NewPasswordStatus();
				status.setStatus(true);
				status.setMessage("Registration successfull!!!");
				status.setCustomerId(id);;
				return status;
			}
			catch(ServiceException e) {
				NewPasswordStatus status = new NewPasswordStatus();
				status.setStatus(false);
				status.setMessage(e.getMessage());
				return status;
			}
		}
		
		@PostMapping("/pic-upload")
		public Status upload(Picture picDetails) {
			
			long referenceId = picDetails.getReferenceId();
			//long referenceId = (long)62;
			
			String imgUploadLocation = "e:/uploads/";
			
			String uploadedFileName1 = picDetails.getAadharPic().getOriginalFilename();
			String newFileName1 = referenceId + "-" + uploadedFileName1;
			String targetFileName1 = imgUploadLocation + newFileName1;
			
			String uploadedFileName2 = picDetails.getPanPic().getOriginalFilename();
			String newFileName2 = referenceId + "-" + uploadedFileName2;
			String targetFileName2 = imgUploadLocation + newFileName2;
			
			String uploadedFileName3 = picDetails.getLightBill().getOriginalFilename();
			String newFileName3 = referenceId + "-" + uploadedFileName3;
			String targetFileName3 = imgUploadLocation + newFileName3;
			
			String uploadedFileName4 = picDetails.getGstProof().getOriginalFilename();
			String newFileName4 = referenceId + "-" + uploadedFileName4;
			String targetFileName4 = imgUploadLocation + newFileName4;
			
			try {
				FileCopyUtils.copy(picDetails.getAadharPic().getInputStream(), new FileOutputStream(targetFileName1));
				FileCopyUtils.copy(picDetails.getPanPic().getInputStream(), new FileOutputStream(targetFileName2));
				FileCopyUtils.copy(picDetails.getLightBill().getInputStream(), new FileOutputStream(targetFileName3));
				FileCopyUtils.copy(picDetails.getGstProof().getInputStream(), new FileOutputStream(targetFileName4));
			}
			catch(IOException e) {
				e.printStackTrace(); //hope no error would occur
				Status status = new Status();
				status.setStatus(false);
				status.setMessage("Picture upload failed!");
			}
			System.out.println(newFileName1+newFileName2+newFileName3+newFileName4);
			customerService.updatePicture(referenceId, newFileName1, newFileName2, newFileName3, newFileName4);
			
			Status status = new Status();
			status.setStatus(true);
			status.setMessage("Profilepic uploaded successfully!");
			return status;
		}
		
		@GetMapping("/profile")
		//public Customer profile(@RequestParam("customerId")int id) {
		//we need to take of help of HttpServletRequest object in below code
		public Registration profile(@RequestParam("referenceId") long id, HttpServletRequest request) {
			
			//HttpSession session = request.getSession();
			//session.setAttribute("otp", 123);
			
			Registration registration = customerService.get(id);
			
			//the problem is the image is in some folder outside this project
			//because of this, on the client we will not be able to access the same
			//we need to write code to copy image from d:/uploads folder to a folder inside our project
			
			String projPath = request.getServletContext().getRealPath("/");
			System.out.println(projPath);
			
			String tempDownloadPath = projPath + "/downloads/";
			File f = new File(tempDownloadPath);
			if(!f.exists())
				f.mkdir();
			
			String targetFile1 = tempDownloadPath + registration.getAadharPic();
			String targetFile2 = tempDownloadPath + registration.getPanPic();
			String targetFile3 = tempDownloadPath + registration.getLightBill();
			String targetFile4 = tempDownloadPath + registration.getGstProof();
			
			//reading the original location where the image is present
			String uploadedImagesPath = "E:\\uploads\\";
			String sourceFile1 = uploadedImagesPath + registration.getAadharPic();
			String sourceFile2 = uploadedImagesPath + registration.getPanPic();
			String sourceFile3 = uploadedImagesPath + registration.getLightBill();
			String sourceFile4 = uploadedImagesPath + registration.getGstProof();
			
			try {
				FileCopyUtils.copy(new File(sourceFile1), new File(targetFile1));
				FileCopyUtils.copy(new File(sourceFile2), new File(targetFile2));
				FileCopyUtils.copy(new File(sourceFile3), new File(targetFile3));
				FileCopyUtils.copy(new File(sourceFile4), new File(targetFile4));
			}
			catch (IOException e) {
				e.printStackTrace(); //hoping for no error will occur
			}
			
			return registration;
		}

		
}
