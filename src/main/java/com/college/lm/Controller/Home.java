package com.college.lm.Controller;

import com.college.lm.DataSource.User;
import com.college.lm.Exception.UserExitException;
import com.college.lm.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class Home  {
    @Autowired
    private UserRepository userRepo;
    @GetMapping("index.html")
    public String getHome(Model model){
        model.addAttribute("pageName","Admin/dashboard");
        model.addAttribute("pageTitle", "Admin Dashboard");
        return "index";
    }
    @GetMapping("/login.html")
    public String getLogin() {
        return "login";
    }
    @GetMapping("register.html")
    public String Register(Model model){
        model.addAttribute("user",new User());
        return "register";
    }
    @PostMapping("register.html")
    public String setRegister(User user,final BindingResult bindingResult, final Model model){
        model.addAttribute(user);
        BCryptPasswordEncoder ecode=new BCryptPasswordEncoder();
        user.setPassword(ecode.encode(user.getPassword()));
            try {
            if(userRepo.findByEmail(user.getEmail())!=null){
                throw new UserExitException("User Email already exit");
            }
            userRepo.save(user);
            return "login.html";
            } catch (UserExitException e) {
                bindingResult.rejectValue("email", "user.email","An account already exists for this email.");
                model.addAttribute("user", user);
                return "register";
        }
    }
    @GetMapping("loginSucess")
    @ResponseBody
    public String userRedirect(Authentication auth){
            try{
            String userName=auth.getName();
            User uObj=userRepo.findByEmail(userName);
            String UserRoll=uObj.getUser_role();
            if(UserRoll.equals("admin")){
            return "redirect:/dashboard.html";
            }
            else if(UserRoll.equals("student")){
            return "redirect:/login.html";
            }
            else if(UserRoll.equals("hod")){
            return "redirect:/login.html";
            }
            else if(UserRoll.equals("staff")){
            return "redirect:/login.html";
            }
            else{
            return "redirect:/login.html";
            }
            }catch(Exception e){
               return "redirect:/login.html";
            }
    }

}