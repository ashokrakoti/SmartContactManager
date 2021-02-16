package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/smartcontactmanager")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/smartcontactmanager/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/smartcontactmanager/signup")
	public String signUp(Model model) {
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	/**
	 * this is the handler for user registration
	 */
	@RequestMapping(value="/smartcontactmanager/do_register", method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			                  @RequestParam(value="agreement", defaultValue="false")boolean agreement,
			                 Model model,
			                 HttpSession session) {
		
		
		try {
			
			if(!agreement) {
				System.out.println("You have not agreed to the terms and conditions");
				throw new Exception("You have not agreed to the terms and conditions");
			}
			
			if(result.hasErrors()) {
				model.addAttribute("user",user);
				System.out.println("Error:"+ result.toString());
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setStatus(true);
			user.setImageUrl("default.jpg");
			
			System.out.println("agreement: "+agreement);
			System.out.println("User: "+user);
			
			User userResult = this.userRepository.save(user);
			
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered!!", "alert-success"));
			
			//model.addAttribute("user", userResult);
			
			return "signup";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong!!!"+ e.getMessage(), "alert-danger"));
			return "signup";
		}
        		
		
	}	
}













