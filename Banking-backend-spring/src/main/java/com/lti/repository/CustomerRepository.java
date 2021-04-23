package com.lti.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

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
		public List<Transaction> fetchTransactionAdmin() {
			List<Transaction> resultList = (List<Transaction>)
					entityManager
					.createQuery("select t from Transaction t")
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
		
		public void updateNewPassword(long customerId, String loginPassword, String transactionPassword) {
			entityManager
			.createNativeQuery("update tbl_account_detail set login_password=?, transaction_password=? where customer_id=?")
			.setParameter(1, loginPassword)
			.setParameter(2, transactionPassword)
			.setParameter(3, customerId)
			.executeUpdate();
}
		
}
