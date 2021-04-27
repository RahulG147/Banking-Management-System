package com.lti.repository;

import java.time.LocalDate;
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
	//admin part...
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

	public List<Transaction> allTransactionsByDate(Long fromAccount,String from,String to) {
		LocalDate fdate = LocalDate.parse(from);
		LocalDate tdate = LocalDate.parse(to);
		List<Transaction> transaction = (List<Transaction>)
		entityManager
        .createNativeQuery("SELECT  FROM_ACCOUNT, TO_ACCOUNT, TRANSACTION_DATE, MESSAGE, TR_AMOUNT, TR_MODE, TR_STATUS from tbl_transaction_detail where TO_DATE(TO_CHAR(TRANSACTION_DATE, 'YYYY-MM-DD'),'YYYY-MM-DD') between  ?  and ?  and FROM_ACCOUNT = ?  ")
        .setParameter(3, fromAccount)
        .setParameter(1, fdate)
        .setParameter(2, tdate)
        .setMaxResults(2)
        .getResultList();
        return transaction;
	}

	public List<Transaction> allTransactionByMonth(Long fromAccount,String from,String to){
        List<Transaction> transaction = (List<Transaction>)
                entityManager
                .createNativeQuery("SELECT  FROM_ACCOUNT, TO_ACCOUNT, TRANSACTION_DATE, MESSAGE, TR_AMOUNT, TR_MODE, TR_STATUS from tbl_transaction_detail where  FROM_ACCOUNT = ?  ")
                .setParameter(1, fromAccount)
                .getResultList();
        return transaction;
    }
	
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

	public void savePayee(Long userAcc, Long beneAcc, String beneName, String nickName) {
		entityManager.createNativeQuery("insert into tbl_payee (BENEFICIARY_NAME,NICK_NAME,USER_ACCOUNT_NO, BENEFICIARY_ACCOUNT_NO) values (?,?,?,?)")
		.setParameter(1, beneName)
		.setParameter(2, nickName)
		.setParameter(3, userAcc)
		.setParameter(4, beneAcc)
		.executeUpdate();
	}
}
