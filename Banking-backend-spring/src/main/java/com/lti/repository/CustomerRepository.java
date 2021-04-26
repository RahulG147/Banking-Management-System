package com.lti.repository;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lti.entity.Account;
import com.lti.entity.AccountDetail;
import com.lti.entity.Registration;
import com.lti.entity.Transaction;

@Repository
public class CustomerRepository  extends GenericRepository{

	public boolean isCustomerPresent(long aadhaar) {
		return (Long)
				entityManager
				.createQuery( "select count(r.aadhaarNo) from Registration r where r.aadhaarNo = :ad")
				.setParameter("ad",aadhaar)
				.getSingleResult() == 1 ? true : false;

	}

	public long fetchIdByLoginIdAndPassword(long customerId, String loginPassword) {
		return (Long)
				entityManager
				.createQuery("select a.customerId from Account a where a.customerId = :ci and a.loginPassword= :pw")
				.setParameter("ci", customerId)
				.setParameter("pw", loginPassword)
				.getSingleResult();
	}
	//admin part...later on change to AdminRepository	
	public List<Transaction> fetchTransactionAdmin(Long fromAccount,Long toAccount) {
			List<Transaction> resultList = (List<Transaction>)
				entityManager
				.createQuery("select t from Transaction t where t.fromAccount.accountNumber = :from or t.toAccount.accountNumber = :to")
				.setParameter("from", fromAccount)
				.setParameter("to", toAccount)
				.getResultList();
		return resultList;
	}


	public List<Registration> fetchRegistrationRequestForAdmin() {
		List<Registration> resultList = (List<Registration>)
				entityManager
				.createQuery("select r from Registration r")
				.getResultList();
		return resultList;
	}

	public Registration fetchRegistrationFileForAdmin(Long serviceNo) {
		Registration resultList = (Registration)
				entityManager
				.createQuery("select r from Registration r where r.referenceNo = :ref ")
				.setParameter("ref",serviceNo)
				.getSingleResult() ;
		return resultList;
	}

	public void deleteById(Registration reg) {
		entityManager.remove(reg);
	}

	public void insertIntoAccount(long registration,String loginPassword,String transactionPassword,long customerId) {

		entityManager
		.createNativeQuery("insert into tbl_account_detail (customer_id,service_reference_no,login_password,transaction_password) values (?,?,?,?)")
		.setParameter(1, customerId)
		.setParameter(2, registration)
		.setParameter(3, loginPassword)
		.setParameter(4, transactionPassword)
		.executeUpdate();
	}
	public void insertIntoAccontType(long accountNumber,String accountType,double balance,long customerId) {

		entityManager
		.createNativeQuery("insert into tbl_account_type (customer_id,account_type,balance,account_number) values (?,?,?,?)")
		.setParameter(1, customerId)
		.setParameter(2, accountType)
		.setParameter(3, balance)
		.setParameter(4, accountNumber)
		.executeUpdate();
	}

	public boolean isCustomerIdPresent(long customerId) {
		return (Long)
				entityManager
				.createQuery( "select a.customerId from Account a where a.customerId = :cid")
				.setParameter("cid",customerId)
				.getSingleResult() == 1 ? true : false;

	}
	public boolean isReferenceIdPresent(long referenceId) {
		return (Long)
				entityManager
				.createQuery( "select count(r.referenceNo) from AcceptedRegistrations r where r.referenceNo = :sid")
				.setParameter("sid",referenceId)
				.getSingleResult() == 1 ? true : false;

	}
	
	public long fetchAccNoByOtp(String otp) {
		return (Long)
				entityManager
				.createQuery("select o.fromAccount from Otp o where o.otp = :otp")
				.setParameter("otp", otp)
				.getSingleResult();
	}

	public void updateNewPassword(long customerId, String loginPassword, String transactionPassword) {
		entityManager
		.createNativeQuery("update tbl_account_detail set login_password=?, transaction_password=? where customer_id=?")
		.setParameter(1, loginPassword)
		.setParameter(2, transactionPassword)
		.setParameter(3, customerId)
		.executeUpdate();
	}

	public List<Long> fetchAccounts(long custId){
		List<Long> resultList = (List<Long>)
				entityManager
				.createQuery("select acc.accountNumber from AccountDetail acc where acc.account.customerId= :id")
				.setParameter("id", custId)
				.getResultList();
		return resultList;
	}
	
	public List<AccountDetail> fetchDetailsByCustomerId(long custId) {
		List<AccountDetail> detail = (List<AccountDetail>)
				entityManager
				.createQuery("SELECT a.accountNumber,a.accountType,a.bankBalance FROM AccountDetail a  WHERE a.account.customerId = :cust ")
				.setParameter("cust",custId)
				.getResultList();
				return detail;
	}
	
	public List<AccountDetail> fetchDetailsforAdmin() {
		List<AccountDetail> detail = (List<AccountDetail>)
				entityManager
				.createQuery("SELECT a.accountNumber,a.accountType,a.bankBalance FROM AccountDetail a ")
				.getResultList();
				return detail;
	}
	public List<Long> getAccountNumber(Long customerId){
		List<Long> accNumbers = (List<Long>)
				entityManager
				.createQuery("SELECT a.accountNumber FROM AccountDetail a where a.account.customerId =:cust")
				.setParameter("cust", customerId)
				.getResultList();
		return accNumbers;
	}
	
	//Check Query ...including date is missing 
	public List<Transaction> allTransactionsByDate(Long fromAccount,String from,String to) {
		/*System.out.println(from);
		LocalDateFormater formatter = new SimpleDateFormat("yyyy/mm/dd", Locale.ENGLISH);
            try {
				LocalDate fDate = formatter.parse(from);
				Date tDate = formatter.parse(to);
			*/
		List<Transaction> transaction = (List<Transaction>)
				entityManager
				.createQuery("SELECT t.fromAccount.accountNumber,t.toAccount.accountNumber,t.transactionDate,t.message,t.amount,t.modeOfTransaction,t.status from Transaction t where t.transactionDate between to_timestamp( '2001/05/31 00:00:00'  , 'YYYY/MM/DD HH24:MI:SS') and to_timestamp( :to , 'YYYY/MM/DD HH:MI:SS') and t.fromAccount.accountNumber = :fromAccount  ")
				.setParameter("fromAccount",fromAccount)
				//.setParameter("from", from)
				.setParameter("to",to)
				//.setParameter("from", new java.util.Date(), TemporalType.TIMESTAMP)
				//.setParameter("to", new java.util.Date(), TemporalType.TIMESTAMP)
				.setMaxResults(2) 
				.getResultList();
		return transaction;
		/*} catch (java.text.ParseException e) {
				e.printStackTrace();
				return null;
				}*/
	}
	//.createQuery("SELECT t.fromAccount.accountNumber,t.toAccount.accountNumber, to_date(to_char(TRANSACTION_DATE,'yyyy/mm/dd')),t.message,t.amount,t.modeOfTransaction,t.status from Transaction t where to_date(to_char(TRANSACTION_DATE,'yyyy/mm/dd')) between TO_DATE(  :from,'yyyy/mm/dd') and  TO_DATE( :to,'yyyy/mm/dd') and t.fromAccount.accountNumber = :fromAccount  ")
	//.createQuery("SELECT t.fromAccount.accountNumber,t.toAccount.accountNumber,t.transactionDate,t.message,t.amount,t.modeOfTransaction,t.status from Transaction t where  t.transactionDate >= to_timestamp( :from, 'yyyy/mm/dd hh24:mi:ss') and t.transactionDate <= to_timestamp( :to, 'yyyy/mm/dd hh24:mi:ss') and t.fromAccount.accountNumber = :fromAccount  ")
	
	
	public List<Transaction> allTransactionByMonth(Long fromAccount,YearMonth from,YearMonth to){
		List<Transaction> transaction = (List<Transaction>)
				entityManager
				.createQuery("SELECT t.fromAccount.accountNumber,t.toAccount.accountNumber,t.transactionDate,t.message,t.amount,t.modeOfTransaction,t.status from Transaction t where t.fromAccount.accountNumber = :fromAccount  ")
				.setParameter("fromAccount",fromAccount)
				//.setParameter("from", from)
				//.setParameter("to",to)
				.getResultList();
		return transaction;
	}
	//and  t.transactionDate  between to_date(:from, 'yyyy/mm/dd') and to_date(:to, 'yyyy/mm/dd')

	public List<Transaction> getAllTransactions(Long acc){
		List<Transaction> transaction = (List<Transaction>)
				entityManager
				.createQuery("SELECT t.fromAccount.accountNumber,t.toAccount.accountNumber,t.transactionDate,t.message,t.amount,t.modeOfTransaction,t.status from Transaction t where t.fromAccount.accountNumber = :fromAccount  order by t.transactionDate desc ")
				.setParameter("fromAccount",acc)
				.getResultList();
		return transaction;
	}
	
	
	public List<Long> fetchAccountNumberByCustomerId(Long custId){
		List<Long> accNumber = (List<Long>)
				entityManager
				.createQuery("SELECT a.accountNumber from AccountDetail a where a.account.customerId = :cust ")
				.setParameter("cust",custId)
				.getResultList();
		return accNumber;
	}
	


	
	public Account findCustomerByCustomerId(long customerId){
		return entityManager.find(Account.class, customerId);
	}
	

}
