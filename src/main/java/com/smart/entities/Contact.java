package com.smart.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name ="Contact")
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long contactId;
	private String name;
	private String secondName;
	private String work;
	private String email;
	private String imageUrl;
	 @Column(length = 500) 
	private String description; 
	private String phone;
	
	@ManyToOne
	private User user ;

	public long getContactId() {
		return contactId;
	}

	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
 
	public boolean equals(Object obj) {
		return this.contactId==((Contact) obj).getContactId();
	}
	/*
	 * @Override public String toString() { return "Contact [contactId=" + contactId
	 * + ", name=" + name + ", secondName=" + secondName + ", work=" + work +
	 * ", email=" + email + ", imageUrl=" + imageUrl + ", description=" +
	 * description + ", phone=" + phone + ", user=" + user + "]"; }
	 */
}
