package com.smart.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entities.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	@Query("Select c from Contact c where c.user.id =:userId")
	//@Query("from Contact as c where c.user.Id=:userId)
	
	public Page<Contact> findContactsByUser(@Param("userId")Long userId, Pageable pageable);
	
	/*public List<Contact> findContactsByUser(@Param("userId")Long userId);*/
    //using the page we can operate on a part of a list of objects. look docs for more info.
	//pageable has current page information.
	//no of contacts per page.
}
