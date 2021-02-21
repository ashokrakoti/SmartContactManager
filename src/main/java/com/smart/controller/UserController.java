package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
@RequestMapping("smartcontactmanager/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	
	/**
	 * method to send the user object(common Data) to all the methods response (for UI rendering).
	 * @param model
	 * @param principal
	 * @return
	 */
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		
		String userName = principal.getName();
		System.out.println("UserName:"+ userName);
		
		//we are fetching the user details of the user by userName for using in other places like ui. 
		User user = userRepository.getUserByUsername(userName);
		
		System.out.println("USER"+user);
		
		//sending the user details into dash board UI.
		model.addAttribute("user", user);
		
	}
	
	
	
	// handler for dash board home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal ) {
		
		return "normal/user_dashboard";
	}
	
	/**
	 * handler for adding the user contact.
	 */
	@RequestMapping("/add-contact")
	public String openAddContactForm(Model model) {
		
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
    
	/**
	 * processing add contact form 
	 */
	@RequestMapping(value="/process-contact", method = RequestMethod.POST)
	public String processContact(@ModelAttribute Contact contact) {
		
		System.out.println("contact data: "+contact);
		
		return "normal/add_contact_form";
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
