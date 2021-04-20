package com.lti.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lti.model.AdminGetRegisterStatus;
import com.lti.model.AdminTransactionView;
import com.lti.model.CredentialStatus;
import com.lti.model.Login;
import com.lti.model.LoginStatus;
import com.lti.model.NewPasswordStatus;
import com.lti.model.RegisterStatus;
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
}
