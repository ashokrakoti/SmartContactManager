package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	
	/**
	 * fetches a User from Data base by using the email/userName in our project.
	 * @param email the registered email id of the User.
	 * @return User object that has the userName as the email passed.
	 */
	@Query("select u from User u where u.email = :email")
	public User getUserByUsername(@Param("email") String email);

}
