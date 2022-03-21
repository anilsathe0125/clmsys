package com.college.lm.Controller;

import com.college.lm.DataSource.Leave;
import com.college.lm.DataSource.User;
import com.college.lm.Exception.UserExitException;
import com.college.lm.Repository.DepartMentRepo;
import com.college.lm.Repository.LeaveRepo;
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
    @Autowired
    private DepartMentRepo departMentRepo;
    @Autowired
    private LeaveRepo leaveRepo;
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
        model.addAttribute("depatment",departMentRepo.findAllByStatus(true));
        return "register";
    }
    @PostMapping("register.html")
    public String setRegister(User user,final BindingResult bindingResult, final Model model){
        model.addAttribute(user);
        BCryptPasswordEncoder ecode=new BCryptPasswordEncoder();
        user.setPassword(ecode.encode(user.getPassword()));
        Leave leave=new Leave();
        leave.setTotal(6);
            try {
            if(userRepo.findByEmail(user.getEmail())!=null){
                throw new UserExitException("User Email already exit");
            }
            if (user.getRole().equals("student")) {
                user.setStatus("active");
                leave.setUser(userRepo.save(user));
                leave.setDete("" + System.currentTimeMillis());
                leaveRepo.save(leave);
            }
            else {
                leave.setUser(userRepo.save(user));
                leave.setDete("" + System.currentTimeMillis());
                leaveRepo.save(leave);
            }
            return "login.html";
            } catch (UserExitException e) {
                bindingResult.rejectValue("email", "user.email","An account already exists for this email.");
                model.addAttribute("user", user);
                return "register";
        }
    }
    @GetMapping("loginSucess")
    public String userRedirect(Authentication auth){
            try{
            String userName=auth.getName();
            User uObj=userRepo.findByEmail(userName);
            String UserRoll=uObj.getRole();
            if(UserRoll.equals("admin")){
            return "redirect:Admin/dashboard.html";
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