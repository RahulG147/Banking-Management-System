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
		
		
}
