package com.lti.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

//GenericDao is same as GenericRepository
//@Component can be replaced with @Repository 
@Repository //recommended for Dao Classes
public class GenericRepository { //repository is another name for Dao

	@PersistenceContext
	public EntityManager entityManager;
	
	public Object save(Object obj) {
		Object updatedObj = entityManager.merge(obj);//creates and return the pk
		return updatedObj;
		
	}
	
	//If we go with previous version of Dao it would type cast any object to any other type object so this can be handled at compile time 
	//rather than solving it at runtime
	//eg:Customer c = dao.fetch(Customer.class,123);
	//eg: Product p =(Product) dao.fetch(Order.class,111);--->bad practice-->Compilation error
	//E is a place holder can be anything eg :<ABC> ABC(Class<ABC> clazz ,Object pk)
	public <E> E find(Class<E> clazz , Object pk) {
		E e =entityManager.find(clazz, pk);
		return e;
	}
}
