package com.app.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.entity.User1;
import com.app.helper.Messanger;
import com.app.repositary.UserRepo;
import com.app.service.EmailService;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {
	
	//generate otp
	Random random=new Random(1000);
	
//	@Autowired(required = true)
	private BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private EmailService emailService;
	
	
	//email open form handler
	@GetMapping("/forgot")
	public String openEmailForm()
	{
		
		return "forgot_email_form";
	}
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email
			,HttpSession session)
	{
		System.out.println("Name of the Email :" +email);
		
		int otp=random.nextInt(999999);
		
		System.out.println("generated otp is :"+otp);
		
		//write code to send otp to email....
		
		
		String subject="otp from SCM";
		String message=""
				      +"<div style='border:1px solid #e2e2e2; padding:20px;'>"
				      +"<h1>"
				      +"OTP IS "
				      +"<b>"+otp
				      +"</b>"
				      +"</h1>"
				      +"</div>";
		String to=email;
		String from="ranjithr200020@gmail.com";
		
		
	boolean flag = this.emailService.sendEmail(email, from, subject, message);
	
	if (flag) {
//		session.setAttribute("message", new Messanger("cheak your Email Id", "form-alert-green"));
		session.setAttribute("myotp", otp);
		session.setAttribute("email", email);
		return "verify_otp";
	}else {
		
		session.setAttribute("message", new Messanger("email id not send", "form-alert-red"));
		return "forgot_email_form";
	}
	
	}
	
	//verify otp
	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("otp")int otp
			,HttpSession session)
	{
		int myotp= (int) session.getAttribute("myotp");
		String email=(String) session.getAttribute("email");
		
		if(myotp == otp)
		{
			//password change form
			
		User1 user	=this.userRepo.getUserByUserName(email);
		
		
		if (user ==null) {
			//send error message
			
			session.setAttribute("message",new Messanger("Your Email Id Not Found", "form-alert-red"));
			
			
			return "forgot_email_form";
		}
		else {
			
			
			//send change password form
		}
			
			return "password_change_form";
		}
		else {
			session.setAttribute("message", new Messanger("you have enterd wrong otp","form-alert-red"));
			return "verify_otp";
		}
	}
	//change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newPassword") String newPassword
			,HttpSession session)
	{
		
		String email=(String) session.getAttribute("email");
		User1 user=this.userRepo.getUserByUserName(email);
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		this.userRepo.save(user);
		
//		session.setAttribute("message", new Messanger("new password succesfuly don", "form-alert-green"));
		
		return "redirect:/signin?change=password changed successfuly...";
	}

}
