package com.app.controller;


import org.springframework.beans.factory.annotation.Autowired;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.entity.Contect;
import com.app.entity.User1;
import com.app.helper.Messanger;
import com.app.repositary.UserRepo;
import com.app.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	@Autowired
	private UserRepo repo;
	@Autowired
	private UserService service;
	
	@Autowired
	private PasswordEncoder  passwordEncoder;
	//home handler
	@GetMapping("/")
	public String test(Model model)
	{
		model.addAttribute("tital", "Wellcome To Smart Contec Manager");
		return "home";
	}
	//about handler
	@GetMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("tital", "About");
		return "about";
	}
	//signup handler
	@GetMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("tital", "Register Here !!");
		model.addAttribute("user", new User1());
		return "signup";
	}
	@GetMapping("/signin")
	public String dosignin(Model model)
	{
		model.addAttribute("tital","Sign In");
		return "login";
	}
	//register_form handler
	@PostMapping("/register_form")
	public String registerForm(@Valid @ModelAttribute("user") User1 user
			,BindingResult result1
			,@RequestParam(value = "enable",defaultValue = "false") boolean b
			,Model model,HttpSession session)
	{

		model.addAttribute("tital", "Register Here !!");
		
		try {
			if (!b) 
			{
				System.out.println("you are not agreed our terms and condition");
				throw new Exception("you are not agreed our therms and condition");
			}
			if(result1.hasErrors())
			{
				System.out.println(result1);
				return "signup";
			}
//			User1 finleuser=new User1();
//			finleuser.setName(user.getName());
//			finleuser.setEmail(user.getEmail());
//		    finleuser.setPassword(passwordEncoder.encode(user.getPassword()));
//		    finleuser.setContects(user.getContects());
//		    finleuser.setAbout(user.getAbout());
//		    finleuser.setEnable(true);
//		    finleuser.setRole("ROLE_USER");
//		    finleuser.setImageUrl("image/image.jpg");
			
			    user.setRole("ROLE_USER");
				user.setEnable(true);
				user.setImageUrl("image/image.jpg");
				
//				System.out.println("agrement "+b);
//				System.out.println("user "+user);
//			  User1 result=repo.save(user);
				service.addUser(user);
			  model.addAttribute("user",new User1());
			  session.setAttribute("message", new Messanger("SUCCESFULLY REGISTERED !!","form-alert-green"));
			
		} catch (Exception e) {
			e.printStackTrace();
//			model.addAttribute("user", user);
			session.setAttribute("message", new Messanger("SOMTHING WENT WRONG !!","form-alert-red"));
		}
		return "signup";
	}
}
