package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("smartcontactmanager/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	
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
	public String processContact(@ModelAttribute Contact contact,
			                     @RequestParam("profileImage") MultipartFile file,
			                     Principal principal,
			                     HttpSession session) {
		
		try {
			
			String name = principal.getName();
			//using the user controller instance to get the user details from userRepository.
			User user = this.userRepository.getUserByUsername(name);
			
			//processing and uploading image file.
			
			if(file.isEmpty()) {
				// if file  is empty show a message.
				System.out.println("No file choosen!!");
				contact.setImageUrl("contact.png");
				session.setAttribute("message", new Message(" Your Contact is saved without image.", "alert-success"));
				
			}else {
				//file  is present so upload it and update the imageUrl field in contact table with the name of image.
				//uploading the image
				contact.setImageUrl(file.getOriginalFilename());
				
				// these lines of code uploads the image at the specified file path......"static/img"  is the location where we wanted to store our images. 
				File saveFile = new ClassPathResource("static/img").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				
				System.out.println("pathValue: "+ path.toString());
				
				//Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename()+ principal.getName());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				//this line can be used to remove the file present in the path.
				//Files.delete(path);
				
				System.out.println("Image uploaded successfully.!!!");
				
				//success message to web page.
				session.setAttribute("message", new Message(" Your Contact is saved.....add more", "alert-success"));
								
			}
			
			
			//now we need to do two things. 
			//1.set the user to whom the contact list will be added.
			//2.then set the contact to be stored into the contacts list of the particular user we retrieved earlier in this method.
			
			//this is setting the contact list to a user object.
			contact.setUser(user);
			
			//adding contact details to the user object
			user.getContacts().add(contact);
			
			this.userRepository.save(user);//this updates the user details in the database.
			
			//System.out.println("contact data: "+contact);
			System.out.println("contact added");			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DATA" + e.getMessage());
			session.setAttribute("message", new Message(" Something wrong try again!!!", "alert-danger"));
		}
		
		return "normal/add_contact_form";
	}
	
	
	//this is show contacts handler
	//per page we will have to show 3 because to see the pagination implementation as we have a less number of contacts now.
	// we will hanle the case by passing the path varaible in the uri. in our case. we can pass it by different methods also.
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable ("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title", "View Contacts");
		
		//retrieve the contacts of the user from data base.
		//method 1
		//use the "principal" object and then use the username from it and then use the userRepository to call getUserByUsername() in the repository
		/*the following snippet of code to obtain a list of contacts stored.
		 * String username = principal.getName(); 		 * 
		 * User user = userRepository.getUserByUsername(username); user.getContacts();		 * 
		 * 
		 */
		
		//method2
		String userName = principal.getName();
		User user = this.userRepository.getUserByUsername(userName);
		
		//making a pageable object a PageRequest is a child implementation of pageable interface.
		 Pageable pageRequest = PageRequest.of(page, 3);
		
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageRequest);
		
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());
		
		return "normal/show_contacts";
	}
	
	//showing each contact details.
	@RequestMapping(value="/show-contacts/contact/{contactId}", method=RequestMethod.GET)
	public String ShowContactDetails(@PathVariable("contactId") Long contactId, Model model, Principal principal) {
		
		Optional<Contact> contactOptional = this.contactRepository.findById(contactId);
		Contact contact  = contactOptional.get();
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUsername(userName);
		
		if(user.getId()== contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title" , contact.getName());
		}
		return "normal/contact_detail";
	}
	
	/**
	 * @param contactId of the contact to be deleted.
	 * @param principal is the currently logged in User.
	 * @return redirects to the user contacts page.
	 */
	@RequestMapping(value="/delete-contact/{contactId}", method=RequestMethod.GET)
	public String DeleteContactDetails(@PathVariable("contactId") Long contactId, Principal principal, HttpSession session ) {
	    
		//fetching the contact from teh contact Repository using the contactId.
		Contact contact = this.contactRepository.findById(contactId).get();
		
		if(!contact.getImageUrl().isEmpty() && !contact.getImageUrl().equals("contact.png")) {
			System.out.println("inside deleteContact() - contact image URL"+contact.getImageUrl());
			 try { //delete the existing image
				  File deleteFile = new ClassPathResource("static/img").getFile(); 
				   File file1 = new File(deleteFile, contact.getImageUrl());
				   file1.delete(); 
				   } 
			  catch (Exception e) {
			        System.out.println("Error occured while deleting image : "+ e.getMessage());
			        e.printStackTrace();
			      }
			 System.out.println("DELETED USER PROFILE PIC IMAGE");
		}
		
		
		//check for not allowing a non linked user of this contact to delete the contact.
		//for this we get the userId of teh user from the Principal and cross check if the UserId matches with one linked with the contact.
		// other wise we should not allow that un authorised user to delete the contact.
		
		User user = this.userRepository.getUserByUsername(principal.getName());
		
		if(user.getId()== contact.getUser().getId()) {
			
			user.getContacts().remove(contact);
			
			this.userRepository.save(user);
			 
			System.out.println("DELETED CONTACT");
		}	
		//we use this session object to send the messages from backend to front end.
		session.setAttribute("message", new Message("Successfully Deleted the Contact!!", "alert-success"));
		return "redirect:/smartcontactmanager/users/show-contacts/0";
	}	

	// handler for update contact form opener
	@PostMapping("/update-contact/{contactId}")
	public String openUpdateContactForm(@PathVariable("contactId") Long contactId, Model model) {
		model.addAttribute("title", "Update Contact");
		// first we need to fetch the existing contact details from the contacts
		// repository.
		System.out.println(" in form open () ID of the contact to be updated: " + contactId);
		Contact contact = this.contactRepository.findById(contactId).get();
		// send it to the form using the model object.
		model.addAttribute("contact", contact);
		System.out.println(" in form open () contact id: "+contact.getContactId());
		System.out.println(" in form open () contact image is:" + contact.getImageUrl());
          
		return "normal/update_contact";
	}
	
	//update contact handler
	@RequestMapping(value="/process-update/{contactId}", method=RequestMethod.POST)
	public String processContactUpdate(@ModelAttribute Contact contact, @PathVariable("contactId") Long contactId, 
			                         @RequestParam("profileImage") MultipartFile file,
			                         Model model, HttpSession session, Principal principal) {
		
		try {
			//checking if image file is upated or not
			//getting previous contact details 
			System.out.println("in processContactUpdate()- iD of the contact:"+ contactId) ;
			Contact oldContactDetails = this.contactRepository.findById(contactId).get();
			System.out.println("in processContactUpdate()- old detailsID : "+oldContactDetails.getContactId());
			
			if(!file.isEmpty()) {  
				//new photo detected update the photo
				//delete the existing
				if(!oldContactDetails.getImageUrl().isEmpty() && !oldContactDetails.getImageUrl().equals("contact.png")) {
					File deleteFile = new ClassPathResource("static/img").getFile();
					File file1 = new File(deleteFile, oldContactDetails.getImageUrl());
					file1.delete();
				}
				
				
				
				//update the new photo
				// these lines of code uploads the image at the specified file path......"static/img"  is the location where we wanted to store our images. 
				File saveFile = new ClassPathResource("static/img").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				
				System.out.println(" in processContactUpdate()- pathValue: "+ path.toString());
								
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImageUrl(file.getOriginalFilename());
				
			}else {
				//because the update has no photo change we are updating the same imageUrl/imageName for the updated contact.
				contact.setImageUrl(oldContactDetails.getImageUrl());
			}
			//this update process is done like this because our contact is mapped under user and we are storing it as 
			// a property list item of the User in our DB. check the method processContact() to know how we created the contacts under user 
			//so if we update directly without update in the user also it will be updated but  
			//will not get linked to the user and we wont be able to fetch the user contacts list and display it.
			User user = this.userRepository.getUserByUsername(principal.getName());
			contact.setUser(user);		
			this.contactRepository.save(contact);
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
		System.out.println(" in processContactUpdate()- contact name: "+contact.getName());
		System.out.println(" in processContactUpdate()- contact Id: "+contact.getContactId());
		
		
		return "redirect:/smartcontactmanager/users/show-contacts/contact/"+contact.getContactId();
	}
	
	
}
	
