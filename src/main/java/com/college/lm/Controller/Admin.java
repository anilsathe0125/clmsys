package com.college.lm.Controller;

import java.util.List;

import com.college.lm.DataSource.DepartMent;
import com.college.lm.Repository.DepartMentRepo;
import com.college.lm.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Admin {
    @Autowired
    DepartMentRepo drepo;
    @Autowired
    UserRepository UserRepo;
    @GetMapping("Admin/dashboard.html")
    public String getDashbord(Model model,Authentication auth){
            try{
            String Login=this.userType(auth);
            if(Login.equals("admin")){
            model.addAttribute("pageName","Admin/dashboard");
            model.addAttribute("pageTitle", "Admin Dashboard");
            model.addAttribute("userType","admin");
            }
            return Login.equals("admin")?"index":"redirect:/loginSucess";
            }
            catch(Exception e){
                return "login.html";
            }
    }
    @GetMapping("Admin/m-department.html")
    public String getDepartment(Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("admin")){
            List<DepartMent> dl=drepo.findAll();
            model.addAttribute("pageName","Admin/m-department");
            model.addAttribute("pageTitle", "Manage Department");
            model.addAttribute("dlist", dl);
        }
        return Login.equals("admin")?"index":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @GetMapping("/user")
    @ResponseBody
    public Object getUser(Authentication auth){
        return auth.getName();
    }
    private String userType(Authentication auth){
            String userName=auth.getName();
            return UserRepo.findByEmail(userName).getUser_role();
    }
}
