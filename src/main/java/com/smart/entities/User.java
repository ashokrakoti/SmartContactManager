package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "USER")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotBlank(message="Name is required for registration!!!")
	@Size(min=2, max=30 , message= "Name should be between 2-20 characters!!")
	private String name;
	private String role;
	private boolean status; 
	
	//@Pattern(regexp="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
	@Email(regexp="^[a-zA-Z0-9+_.-]+@[a-zA-Z.-]+$")// we can use both annotations but @Email is more suited.
	@NotBlank(message="email is required for registration!!!")
	@Column(unique = true)
	private String email;
	
	@NotBlank(message="password can't be blank.")
	private String password;
	private String imageUrl;
	
	@NotBlank(message="please.......tell something about yourself....")
	//@Pattern(regexp="^[a-zA-Z0-9+_.,-=(){}]$", message="invalid characters used in details.. please only use: a-zA-Z0-9+_.,-'=(){}")
	@Column(length = 500)
	private String about;
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user", orphanRemoval=true)
	private List<Contact> contacts = new ArrayList<>();
	
	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", "
				+ "name=" + name + ","
				+ " role=" + role + 
				", status=" + status + 
				", email=" + email
				+ ", password=" + password + 
				", imageUrl=" + imageUrl + ", "
						+ "about=" + about + ", "
								+ "contacts=" + contacts+ "]";
	}
	
	
	

}
