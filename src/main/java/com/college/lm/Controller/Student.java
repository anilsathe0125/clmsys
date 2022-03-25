package com.college.lm.Controller;

import com.college.lm.Repository.AllLeaveRepo;
import com.college.lm.Repository.DepartMentRepo;
import com.college.lm.Repository.LeaveRepo;
import com.college.lm.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Student {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private DepartMentRepo departMentRepo;
    @Autowired
    private LeaveRepo leaveRepo;
    @Autowired
    private AllLeaveRepo allLeaveRepo;
    @GetMapping("Student/dashboard.html")
    public String getHome(Model model,Authentication auth){
            try{
                String Login=this.userType(auth);
                if(Login.equals("student")){
                    model.addAttribute("pageName","Student/dashboard");
                    model.addAttribute("pageTitle", "Student Dashboard");
//                    model.addAttribute("dashboard_department",drepo.findAll().size());
//                    model.addAttribute("dashboard_staff",UserRepo.findByRole("staff").size());
//                    model.addAttribute("dashboard_staff_all",UserRepo.findByRole("student"));
//                    model.addAttribute("dashboard_student",UserRepo.findByRole("student").size());
                    model.addAttribute("userDetails",userRepo.findByEmail(auth.getName()));
                }
                return Login.equals("student")?"common/index":"redirect:/loginSucess";
            }
            catch(Exception e){
                return "login.html";
            }
        }


    private String userType(Authentication auth){
        String userName=auth.getName();
        return userRepo.findByEmail(userName).getRole();
    }
    private String MsgRes(String Login,String status, String message){
        return Login.equals("student") ? "{\"status\":\""+ status +"\",\"message\":\""+ message +"\"}" :
                "{\"status\":\"error\",\"message\":\"You are different user or not login.. !\"}";
    }
}
