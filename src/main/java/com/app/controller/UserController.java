package com.app.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.entity.Contect;
import com.app.entity.User1;
import com.app.helper.Messanger;
import com.app.repositary.ContectRepo;
import com.app.repositary.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private ContectRepo contectRepo;
	
	@ModelAttribute
	public void addCommonData(Model model,Principal principal)
	{
		String userName=principal.getName();
//		System.out.println(userName);
//		model.addAttribute("userName",userName);
	    User1  user	=repo.getUserByUserName(userName);
		model.addAttribute("user",user);
	}
	
	@RequestMapping("/index")
	public String dashBord(Model model,Principal principal)
	{
		model.addAttribute("email" , principal.getName());
		return "normal/user_dashbord";
	}
	//open add form handler
	@GetMapping("/contects-info")
	public String openAddContectsForm(Model model,Principal principal)
	{
		model.addAttribute("tital", "add contect form");
		model.addAttribute("contects", new Contect());
		return "normal/add_contect_from";
	}
	//form request handler
	//processing add contect form
	@PostMapping("/process-contect")
	public String processContents(@ModelAttribute Contect contects
			,Principal principal
			,@RequestParam("profileimage") MultipartFile profileimage
			,HttpSession session)
	{
		
		try {
			 String name	=principal.getName();
				User1 user=this.repo.getUserByUserName(name);
				
				if(profileimage.isEmpty())
				{
					//if file is empty try the message
					System.out.println("file is empty");
					contects.setImage("user.png");
					
				}
				else {
					//file the file to the folder and update contect
					contects.setImage(profileimage.getOriginalFilename());
					//saving file in the folder
					String saveFile	=new ClassPathResource("static/image").getFile().getAbsolutePath();
					Path path=Paths.get(saveFile+File.separator+profileimage.getOriginalFilename());
					Files.copy(profileimage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					System.out.println("image is uploded");
				}
				user.getContects().add(contects);
				contects.setUser1(user);
				
	            User1 uploading	=this.repo.save(user);
				System.out.println(contects);
				
			session.setAttribute("message", new Messanger("sucsses contect added", "form-alert-green"));
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.setAttribute("message", new Messanger("failure during add contect","form-alert-red"));
		}
	    
		return "normal/add_contect_from";
	}
	
	//show contect handler
	@GetMapping("/show_contects")
	public String showContects(Model model,Principal principal)
	{
		model.addAttribute("tital","ALL CONTECTS OF USER");
		
		 String username =  principal.getName();
		 
        User1 user	= this.repo.getUserByUserName(username);

      List<Contect> contects = contectRepo.findContectsByUser(user.getId());
       
     model.addAttribute("contects", contects);
      
		return "normal/show_contects";
	}
	//delete contect controller
	@GetMapping("/delete-contect/{cId}")
	public String deleteContect(@PathVariable("cId") Integer cId
			,Principal principal
			,HttpSession session)
	{
		String userName=principal.getName();
		
	 Optional<Contect> optional	=this.contectRepo.findById(cId);
	 
	 Contect contect=optional.get();
	 
	 User1 user1=repo.getUserByUserName(userName);
	 
	 if(user1.getId() == contect.getUser1().getId())
	 { 
		 //deleting file in the folder
//		 String saveFile	=new ClassPathResource("static/image").getFile().getAbsolutePath();
//			Path path=Paths.get(saveFile+File.separator+contect.getImage());
//			Files.copy(profileimage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//		 contect.getImage();
		 
		 this.contectRepo.delete(contect);
		 session.setAttribute("message", new Messanger("succses contect deleted succefuly", "form-alert-green"));
	 }
	 else
	 {
		 session.setAttribute("message", new Messanger("failure not able to delet", "form-alert-red"));
	 }
		return "redirect:/user/show_contects";
	}
	//open update contect
	@PostMapping("/update-contect/{cId}")
	public String updateContect(@PathVariable("cId") Integer cId,Model model)
	{
		model.addAttribute("tital","update contect");
		
	Contect contect=this.contectRepo.findById(cId).get();
	model.addAttribute("contect",contect);
		return "normal/update_contect.html";
	}
	//update contect controller
	@PostMapping("/process-update")
	public String processUpdate(@ModelAttribute Contect contect
			,Principal principal
			,@RequestParam("profileimage") MultipartFile profileimage)
	{
		System.out.println("contect name "+contect.getName());
		System.out.println("contect ID "+contect.getcId());
		
		try {
			
			//new file
			String newFile=profileimage.getOriginalFilename();
			
			//old contect
			Contect oldContect=this.contectRepo.findById(contect.getcId()).get();
			
		    
			
			if(!profileimage.isEmpty())
			{
				//file path
				//remove old image
				File deleteFile=new ClassPathResource("static/image").getFile();
				File file1=new File(deleteFile, oldContect.getImage());
				file1.delete();
				
				//update new image
				String saveFile	=new ClassPathResource("static/image").getFile().getAbsolutePath();
				Path path=Paths.get(saveFile+File.separator+profileimage.getOriginalFilename());
				Files.copy(profileimage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);				
				contect.setImage(profileimage.getOriginalFilename());
			}
			else
			{
				contect.setImage(oldContect.getImage());
				
			}
			     String userName	=principal.getName();
		         User1 user1=this.repo.getUserByUserName(userName);
				 contect.setUser1(user1);
			
				 this.contectRepo.save(contect);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "redirect:/user/"+contect.getcId()+"/contects";
	}
	
	//showing particulre contects
	@GetMapping("/{cId}/contects")
	public String showContectDetails(@PathVariable("cId") Integer cId
			,Model model
			,Principal principal)
	{
		System.out.println("contect CID : "+cId);
		model.addAttribute("tital", "Contect Details");
		
	Optional<Contect> optionalContects=this.contectRepo.findById(cId);
		Contect contect=optionalContects.get();
		
	    String userName=principal.getName();
	    User1 user1=this.repo.getUserByUserName(userName);
	    
	    if(user1.getId()== contect.getUser1().getId())
	    {
	    	model.addAttribute("contect",contect);
	    }
		
		return "normal/Show_Contect_Detail.html";
	}
	//show profile controller
	@GetMapping("/show_profile")
	public String getProfile(Model model,Principal principal)
	{
		model.addAttribute("tital","Profile");
		
		User1 user=this.repo.getUserByUserName(principal.getName());
		model.addAttribute("user",user);
		return "normal/profile_page.html";
	}
	@PostMapping("/delete_user/{id}")
	public String deleteUserProfile()
	{
		return "";
	}
	@PostMapping("/update_user/{id}")
	public String updateUserprofile(@PathVariable("id") Integer id
			,Model model,HttpSession session)
	{
		model.addAttribute("tital", "update contect");
		return "normal/update_user";
	}
	//open Setting
	@GetMapping("/settings")
	public String openSetting()
	{
		return "normal/settings";
	}
	//change password hadler
	@PostMapping("/changepassword")
	public String settingChangePassword(@RequestParam("oldPassword") String oldPassword
			,@RequestParam("newPassword") String newPassword
			,Principal principal
			,HttpSession session)
	{
		User1 user=this.repo.getUserByUserName(principal.getName());
		
		System.out.println("old password  :"+oldPassword);
		System.out.println("new password  :"+newPassword);
		System.out.println("encripted password :"+user.getPassword());
		
		if (this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
			//if password maches
			user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.repo.save(user);
			session.setAttribute("message", new Messanger("Password Changed", "form-alert-green"));
		}
		else
		{
			//error
			session.setAttribute("message", new Messanger("Incorect Password", "form-alert-red"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
	}
}