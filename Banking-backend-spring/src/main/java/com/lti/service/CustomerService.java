package com.lti.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lti.entity.AcceptedRegistrations;
import com.lti.entity.Account;
import com.lti.entity.AccountCredential;
import com.lti.entity.AccountDetail;
import com.lti.entity.GeneralDetail;
import com.lti.entity.Payee;
import com.lti.entity.Registration;
import com.lti.entity.Transaction;
import com.lti.enums.TransactionType;
import com.lti.model.Transactions;
import com.lti.repository.CustomerRepository;
import com.lti.repository.GenericRepository;

@Service
@Transactional
public class CustomerService {

	@Autowired
	EmailService emailService;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private GenericRepository genericRepository;

	//business logic to autogenerated id
	public long register(Registration customer) {
		if(customerRepository.isCustomerPresent(customer.getAadhaarNo()))
			throw new ServiceException("Customer already registered !");
		else {
			Registration updateCustomer = (Registration) customerRepository.save(customer);
			/*String subject = "Recieved Registration Request";
			String text = "Hi " + customer.getFirstName() + customer.getLastName()
						+ ", we have received a request from you for registering with our bank.\n" 
						+ "Your request will be approved when all the necessary documents are uploaded. \n"
						+ "This is your service reference number " + updateCustomer.getReferenceNo();

			emailService.sendEmailForNewRegistration(customer.getEmailId(),text,subject);*/
			return updateCustomer.getReferenceNo();
		}
	}

	public Account login(long customerId, String loginPassword) {
		try {
			long id = customerRepository.fetchIdByLoginIdAndPassword(customerId, loginPassword);
			Account account = customerRepository.find(Account.class, id);
			return account;
		}
		catch(EmptyResultDataAccessException e) {
			throw new ServiceException("Invalid email/password");
		}
	}

	public void deleteRow(Long ref) {
		try {
			Registration reg = (Registration) customerRepository.find(Registration.class, ref);
			customerRepository.deleteById(reg);

		}
		catch(EmptyResultDataAccessException e) {
			throw new ServiceException("No rows ");
		}


	}

	public List<AccountDetail> getDetailsOfPerticularCustomer(Long custId) {
		
		List<AccountDetail> details = customerRepository.fetchDetailsByCustomerId(custId);
		return details;
	}
	
	public List<AccountDetail> getDetailsForAdmin() {
		
		List<AccountDetail> details = customerRepository.fetchDetailsforAdmin();
		return details;
	}
	
	public String impsTransaction(Transactions transaction) {

		Calendar cal =  Calendar.getInstance();
		try {
			AccountDetail acc1 = genericRepository.find(AccountDetail.class, transaction.getFromAccount());
			AccountDetail acc2 = genericRepository.find(AccountDetail.class, transaction.getToAccount());

			long id = Long.parseLong(transaction.getCustomerId());
			if(id!=acc1.getAccount().getCustomerId()) {
				throw new ServiceException("Please enter your correct Account Number");
			}
			else {
				Account account = genericRepository.find(Account.class, acc1.getAccount().getCustomerId());
				//System.out.println(account.getTransactionPassword()+" "+transaction.getPassword());
				if(!(transaction.getPassword().equals(account.getTransactionPassword()))) {
					throw new ServiceException("Please enter valid transaction password");
				}
				else {
					//setting the senders account 
					if(acc1.getBankBalance()<transaction.getAmount()) {
						throw new ServiceException("Insufficient Balance");
					}
					else {
						if(transaction.getAmount()<10000.00) {
							acc1.setBankBalance(acc1.getBankBalance()-transaction.getAmount()-2.50);
						}

						else if(transaction.getAmount()<40000.00) {
							acc1.setBankBalance(acc1.getBankBalance()-transaction.getAmount()-5.00);
						}
						else if(transaction.getAmount()<100000.00) {
							if(cal.get(Calendar.HOUR_OF_DAY)> 8 && cal.get(Calendar.HOUR_OF_DAY)< 19)
								acc1.setBankBalance(acc1.getBankBalance()-transaction.getAmount()-5.00);
							else
								throw new ServiceException("Transaction above 40000 can be done from 8AM to 7PM only");
						}
						else if(transaction.getAmount()<200000.00) {
							if(cal.get(Calendar.HOUR_OF_DAY)> 8 && cal.get(Calendar.HOUR_OF_DAY)< 19)
								acc1.setBankBalance(acc1.getBankBalance()-transaction.getAmount()-15.00);
							else
								throw new ServiceException("Transaction above 40000 can be done from 8AM to 7PM only");
						}
						else {
							throw new ServiceException("Amount more than 2 lacs cannot be transferred using IMPS");
						}

						genericRepository.save(acc1);

						Transaction trans1 = new Transaction();

						trans1.setFromAccount(acc1);
						trans1.setToAccount(acc2);
						trans1.setAmount(transaction.getAmount());
						trans1.setModeOfTransaction(TransactionType.IMPS);
						trans1.setRemarks("Debited for "+transaction.getRemarks());
						trans1.setTransactionDate(LocalDateTime.now());
						trans1.setMessage(transaction.getMessage());
						trans1.setStatus("Debited Successfully");

						List<Transaction> t1 = new ArrayList<Transaction>();
						t1.add(trans1);

						acc1.setFromTransactions(t1);
						acc2.setToTransactions(t1);

						Transaction trans1RefNo = (Transaction) genericRepository.save(trans1);

						//setting the receiver's account
						acc2.setBankBalance(acc2.getBankBalance()+transaction.getAmount());

						genericRepository.save(acc2);

						Transaction trans2 = new Transaction();
						trans2.setFromAccount(acc2);
						trans2.setToAccount(acc1);
						trans2.setAmount(transaction.getAmount());
						trans2.setModeOfTransaction(TransactionType.IMPS);
						trans2.setTransactionDate(LocalDateTime.now());
						trans2.setRemarks("Credited for "+transaction.getRemarks()+" from "+acc1.getAccount().getGeneralDetail().getFullName());
						trans2.setMessage(transaction.getMessage());
						trans2.setStatus("Credited Successfully");

						List<Transaction> t2 = new ArrayList<Transaction>();
						t2.add(trans2);

						acc2.setFromTransactions(t2);
						acc1.setToTransactions(t2);

						Transaction trans2RefNo = (Transaction)genericRepository.save(trans2);

						return "LTIBANK"+trans1RefNo.getTransactionId()+trans2RefNo.getTransactionId();
					}
				}
			}
		}
		catch(NullPointerException e) {
			throw new ServiceException("Invalid account number");
		}

	}

	public String neftTransaction(Transactions transaction) {

		Calendar cal =  Calendar.getInstance();

		try {
			AccountDetail acc1 = genericRepository.find(AccountDetail.class, transaction.getFromAccount());
			AccountDetail acc2 = genericRepository.find(AccountDetail.class, transaction.getToAccount());

			long id = Long.parseLong(transaction.getCustomerId());
			if(id!=acc1.getAccount().getCustomerId()) {
				throw new ServiceException("Please enter your correct Account Number");
			}
			else {
				//setting the senders account 
				if((cal.get(Calendar.HOUR_OF_DAY)< 8 || cal.get(Calendar.HOUR_OF_DAY)>19) || (cal.get(Calendar.DAY_OF_WEEK)==0 || cal.get(Calendar.DAY_OF_WEEK)==6))
					throw new ServiceException("Transaction could not be carried out during this time.");
				else {
					if(acc1.getBankBalance()<transaction.getAmount()) {
						throw new ServiceException("Insufficient Balance");
					}
					else {
						if(transaction.getAmount()<10000.00) {
							acc1.setBankBalance(acc1.getBankBalance()-transaction.getAmount()-2.50);
						}
						else if(transaction.getAmount()<100000.00) {
							acc1.setBankBalance(acc1.getBankBalance()-transaction.getAmount()-5.00);
						}
						else if(transaction.getAmount()<200000.00) {
							acc1.setBankBalance(acc1.getBankBalance()-transaction.getAmount()-15.00);
						}
						else {
							acc1.setBankBalance(acc1.getBankBalance()-transaction.getAmount()-25.00);
						}

						genericRepository.save(acc1);

						Transaction trans1 = new Transaction();

						trans1.setFromAccount(acc1);
						trans1.setToAccount(acc2);
						trans1.setAmount(transaction.getAmount());
						trans1.setModeOfTransaction(TransactionType.NEFT);
						trans1.setRemarks("Debited for "+transaction.getRemarks());
						trans1.setTransactionDate(LocalDateTime.now());
						trans1.setMessage(transaction.getMessage());
						trans1.setStatus("Debited Successfully");

						List<Transaction> t1 = new ArrayList<Transaction>();
						t1.add(trans1);

						acc1.setFromTransactions(t1);
						acc2.setToTransactions(t1);

						Transaction trans1RefNo = (Transaction) genericRepository.save(trans1);

						//setting the receiver's account
						acc2.setBankBalance(acc2.getBankBalance()+transaction.getAmount());

						genericRepository.save(acc2);

						Transaction trans2 = new Transaction();
						trans2.setFromAccount(acc2);
						trans2.setToAccount(acc1);
						trans2.setAmount(transaction.getAmount());
						trans2.setModeOfTransaction(TransactionType.NEFT);
						trans2.setTransactionDate(LocalDateTime.now());
						trans2.setRemarks("Credited for "+transaction.getRemarks()+" from "+acc1.getAccount().getGeneralDetail().getFullName());
						trans2.setMessage(transaction.getMessage());
						trans2.setStatus("Credited Successfully");

						List<Transaction> t2 = new ArrayList<Transaction>();
						t2.add(trans2);

						acc2.setFromTransactions(t2);
						acc1.setToTransactions(t2);

						Transaction trans2RefNo = (Transaction)genericRepository.save(trans2);

						return "LTIBANK"+trans1RefNo.getTransactionId()+trans2RefNo.getTransactionId();
					}
				}
			}
		}
		catch(NullPointerException e) {
			throw new ServiceException("Invalid account number");
		}
	}

	public String rtgsTransaction(Transactions transaction) {
		try {
			AccountDetail acc1 = genericRepository.find(AccountDetail.class, transaction.getFromAccount());
			AccountDetail acc2 = genericRepository.find(AccountDetail.class, transaction.getToAccount());

			long id = Long.parseLong(transaction.getCustomerId());
			if(id!=acc1.getAccount().getCustomerId()) {
				throw new ServiceException("Please enter your correct Account Number");
			}
			else {
				if(transaction.getAmount()<200000) {
					throw new ServiceException("Amount greater than 2 lacs can only be transferred using RTGS");
				}
				else {
					//setting the senders account 
					if(acc1.getBankBalance()<transaction.getAmount()) {
						throw new ServiceException("Insufficient Balance");
					}
					else {

						acc1.setBankBalance(acc1.getBankBalance()-transaction.getAmount());

						genericRepository.save(acc1);

						Transaction trans1 = new Transaction();

						trans1.setFromAccount(acc1);
						trans1.setToAccount(acc2);
						trans1.setAmount(transaction.getAmount());
						trans1.setModeOfTransaction(TransactionType.RTGS);
						trans1.setRemarks("Debited for "+transaction.getRemarks());
						trans1.setTransactionDate(LocalDateTime.now());
						trans1.setMessage(transaction.getMessage());
						trans1.setStatus("Debited Successfully");

						List<Transaction> t1 = new ArrayList<Transaction>();
						t1.add(trans1);

						acc1.setFromTransactions(t1);
						acc2.setToTransactions(t1);

						Transaction trans1RefNo = (Transaction) genericRepository.save(trans1);

						//setting the receiver's account
						acc2.setBankBalance(acc2.getBankBalance()+transaction.getAmount());

						genericRepository.save(acc2);

						Transaction trans2 = new Transaction();
						trans2.setFromAccount(acc2);
						trans2.setToAccount(acc1);
						trans2.setAmount(transaction.getAmount());
						trans2.setModeOfTransaction(TransactionType.RTGS);
						trans2.setTransactionDate(LocalDateTime.now());
						trans2.setRemarks("Credited for "+transaction.getRemarks()+" from "+acc1.getAccount().getGeneralDetail().getFullName());
						trans2.setMessage(transaction.getMessage());
						trans2.setStatus("Credited Successfully");

						List<Transaction> t2 = new ArrayList<Transaction>();
						t2.add(trans2);

						acc2.setFromTransactions(t2);
						acc1.setToTransactions(t2);

						Transaction trans2RefNo = (Transaction)genericRepository.save(trans2);

						return "LTIBANK"+trans1RefNo.getTransactionId()+trans2RefNo.getTransactionId();
					}

				}
			}
		}
		catch(NullPointerException e) {
			throw new ServiceException("Invalid account number");
		}
	}

	//admin part below
	public List<Transaction> transactionViewByAdmin(Long fromAccount,Long toAccount) {
		try {
			List<Transaction> list = customerRepository.fetchTransactionAdmin(fromAccount,toAccount);
			return list;
		}
		catch(EmptyResultDataAccessException e) {
			throw new ServiceException("No rows !!");
		}
	}

	public List<Registration> RegisterRequestAction() {
		try {
			List<Registration> list = customerRepository.fetchRegistrationRequestForAdmin();
			return list;
		}
		catch(EmptyResultDataAccessException e) {
			throw new ServiceException("No rows !!");
		}
	}

	public long updateCredential(AccountCredential account) {

		AccountCredential updateAccount = (AccountCredential) customerRepository.save(account);	
		Registration reg =(Registration) customerRepository.find(Registration.class, account.getRegistration());
		//AcceptedRegistrations accReg = (Registration) customerRepository.save(reg);
		AcceptedRegistrations accReg = new AcceptedRegistrations();
		accReg.setReferenceNo(reg.getReferenceNo());
		accReg.setTitle(reg.getTitle());
		accReg.setFirstName(reg.getFirstName());
		accReg.setLastName(reg.getLastName());
		accReg.setMiddleName(reg.getMiddleName());
		accReg.setFatherName(reg.getFatherName());
		accReg.setMobileNo(reg.getMobileNo());
		accReg.setEmailId(reg.getEmailId());
		accReg.setAadhaarNo(reg.getAadhaarNo());
		accReg.setPanCard(reg.getPanCard());
		accReg.setDateOfBirth(reg.getDateOfBirth());
		accReg.setResidentialAddress(reg.getResidentialAddress());
		accReg.setPermanent(reg.getPermanent());
		accReg.setOccupation(reg.getOccupation());
		accReg.setIncomeSource(reg.getIncomeSource());
		accReg.setRevenueRegisterNo(reg.getRevenueRegisterNo());
		accReg.setGstNumber(reg.getGstNumber());
		accReg.setAnnualIncome(reg.getAnnualIncome());
		accReg.setAadharPic(reg.getAadharPic());
		accReg.setPanPic(reg.getPanPic());
		accReg.setLightBill(reg.getLightBill());
		accReg.setGstProof(reg.getGstProof());
		customerRepository.save(accReg);

		customerRepository.insertIntoAccount(reg.getReferenceNo(),updateAccount.getLoginPassword(),updateAccount.getTransactionPassword(),updateAccount.getCustomerId());
		customerRepository.insertIntoAccontType(updateAccount.getAccountNumber(),updateAccount.getAccountType(),updateAccount.getBalance(),updateAccount.getCustomerId());

		Account acc =(Account) customerRepository.find(Account.class,updateAccount.getCustomerId());
		AccountDetail ad =(AccountDetail) customerRepository.find(AccountDetail.class,updateAccount.getAccountNumber());
		System.out.println(acc.getCustomerId());
		GeneralDetail details = new GeneralDetail();
		details.setAadhaarNo(reg.getAadhaarNo());//pk
		details.setAccount(acc);//fk
		details.setFullName(reg.getFirstName()+" "+reg.getLastName());
		details.setDateOfBirth(reg.getDateOfBirth());
		details.setMailingAddress(reg.getResidentialAddress());
		details.setPanCard(reg.getPanCard());
		details.setOccupation(reg.getOccupation());
		details.setGrossIncome(reg.getAnnualIncome());
		details.setBalance(ad.getBankBalance());
		//acc.setGeneralDetail(details);
		customerRepository.save(details);
		customerRepository.deleteById(reg);
		System.out.println(accReg.getReferenceNo());

		/*if(customerRepository.isReferenceIdPresent(accReg.getReferenceNo())){
			String subject = "Registration Confirmation";
			String text = "Hi " + accReg.getFirstName() + accReg.getLastName()
						+ " Here are your login credentials\n" 
						+ "Customer Id: " + updateAccount.getCustomerId()
						+ "\nAccount Number: " + account.getAccountNumber()
						+ "\nLogin Password: " + account.getLoginPassword()
						+ "\nTransaction Password: " + account.getTransactionPassword()
						+ "\nThank you!";

			emailService.sendEmailForNewRegistration(accReg.getEmailId(),text,subject);*/
		return updateAccount.getCustomerId();
		/*}

		else {

			throw new ServiceException("No such customer registered");

		}*/
	}


	public long addPassword(Account customer) {
		if(customerRepository.isCustomerPresent(customer.getCustomerId()))
			throw new ServiceException("Error!! Try forget password");
		else {
			Account addNewEntry = (Account) customerRepository.save(customer);
			return addNewEntry.getCustomerId();
		}
	}

	public void updatePicture(long referenceId, String newFileName1, String newFileName2, String newFileName3, String newFileName4) {
		System.out.println(newFileName1+newFileName2+newFileName3+newFileName4);
		Registration registration = customerRepository.find(Registration.class, referenceId);
		registration.setAadharPic(newFileName1);
		registration.setPanPic(newFileName2);
		registration.setLightBill(newFileName3);
		registration.setGstProof(newFileName4);

		customerRepository.save(registration);

	}

	public Registration get(long id) {
		return customerRepository.find(Registration.class, id);
	}

	public Registration registerFileView(Long ref) {
		try {
			Registration reg = customerRepository.fetchRegistrationFileForAdmin(ref);
			return reg;
		}
		catch(EmptyResultDataAccessException e) {
			throw new ServiceException("No rows !!");
		}
	}

	public long addPassword(long customerId, String loginPassword, String transactionPassword) {
		//if(customerRepository.isCustomerPresent(customer.getCustomerId())) {
		//throw new ServiceException("Error!! Try forget password");
		//}
		//if(customerRepository.isCustomerIdPresentInAdminTable(admin.getCustomerId())){
		//Account acc = (Account) customerRepository.find(Account.class, customerId);
		//String loginPassword = acc.getLoginPassword();
		//String transactionPassword = acc.getTransactionPassword();
		customerRepository.updateNewPassword(customerId, loginPassword, transactionPassword);
		//			Account addNewEntry = (Account) customerRepository.save(customer);
		//			AccountDetail addNewTypeEntry = new AccountDetail();
		//			AccountCredential adminTable = new AccountCredential();
		//			addNewTypeEntry.setAccountNumber(adminTable.getAccountNumber());
		//			addNewTypeEntry.setAccountType(adminTable.getAccountType());;
		//			addNewTypeEntry.setBankBalance(adminTable.getBalance());
		//			addNewTypeEntry.setCustomerId(adminTable.getCustomerId());
		//			customerRepository.save(addNewTypeEntry);
		return customerId;
	}

	public AccountDetail viewAccountDetails(long accountNumber) {
		// TODO Auto-generated method stub
		AccountDetail accDetail = genericRepository.find(AccountDetail.class, accountNumber);
		return accDetail;
	}

	public long addBeneficiary(Payee addPayee) {

		//		Payee newPayee = new Payee();
		//		PayeeCompound compKey = new PayeeCompound();
		//		newPayee.setBeneficiaryName(beneficiaryName);
		//		newPayee.setNickName(nickName);
		//		newPayee.setCompoundKey(beneficiaryAccount);
		//		compKey.setBeneficiaryAccount(beneficiaryAccount);
		//		newPayee.setCompoundKey(compKey);

		Payee newPayee = (Payee) customerRepository.save(addPayee);
		//Payee 
		return addPayee.getCompoundKey().getBeneficiaryAccount().getAccountNumber();
	}

	public List<Long> getAccounts(long custId){
		return customerRepository.fetchAccounts(custId);
	}
}
