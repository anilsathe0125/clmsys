package com.college.lm.Controller;

import com.college.lm.DataSource.AllLeave;
import com.college.lm.DataSource.Leave;
import com.college.lm.DataSource.User;
import com.college.lm.Repository.AllLeaveRepo;
import com.college.lm.Repository.DepartMentRepo;
import com.college.lm.Repository.LeaveRepo;
import com.college.lm.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

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
                    User user=userRepo.findByEmail(auth.getName());
                    Leave leave=leaveRepo.findByUserU_Id(user.getU_id());
                    model.addAttribute("pageName","Student/dashboard");
                    model.addAttribute("pageTitle", "Student Dashboard");
                    model.addAttribute("totalLeave",leave);
                    model.addAttribute("pendingLeave",allLeaveRepo.findByPending(user.getU_id(),"pending").size());
                    model.addAttribute("approveLeave",allLeaveRepo.findByPending(user.getU_id(),"approve").size());
                    model.addAttribute("listLeave",allLeaveRepo.findByPending(user.getU_id(),"pending"));
                    model.addAttribute("userDetails",user);
                }
                return Login.equals("student")?"common/index":"redirect:/loginSucess";
            }
            catch(Exception e){
                return "login.html";
            }
        }
    @GetMapping("Student/m-leave.html")
    public Object mleaveStdent(Model model, Authentication auth){
        try{
            User user=userRepo.findByEmail(auth.getName());
            List <AllLeave> allleave=allLeaveRepo.findByUser(user.getU_id());
            model.addAttribute("pageName","Student/m-leave");
            model.addAttribute("pageTitle", "Manage Leave");
            model.addAttribute("al",allleave);
            model.addAttribute("userDetails",user);
            String Login = this.userType(auth);
            return Login.equals("student")?"common/index":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @GetMapping("Student/apply_leave.html")
    public String getApplyleave(Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("student")){
                model.addAttribute("pageName","Student/apply_leave");
                model.addAttribute("pageTitle", "Apply Leave");
                model.addAttribute("userDetails",userRepo.findByEmail(auth.getName()));
            }
            return Login.equals("student")?"Student/apply_leave":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Student/apply_leave.html")
    public String setApplyleave(@RequestParam Map<String,String> getParam,Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("student")){
                User user=userRepo.findByEmail(auth.getName());
                AllLeave allLeave=new AllLeave();
                allLeave.setDate_from(getParam.get("fdate"));
                allLeave.setDate_to(getParam.get("todate"));
                allLeave.setDate_time(String.valueOf(System.currentTimeMillis()));
                allLeave.setStatus("pending");
                allLeave.setUser(user);
                allLeave.setDid(user.getDepartment().getDid());
                allLeave.setReason(getParam.get("reason"));
                switch (getParam.get("ltype")){
                    case "1":
                        allLeave.setLeaveType("halfday");
                        break;
                    case "2":
                        allLeave.setLeaveType("fullday");
                        break;
                    case "3":
                        allLeave.setLeaveType("multiday");
                        break;
                }
                if (this.getLeaveBalance(auth)) {
                    allLeaveRepo.save(allLeave);
                    Leave leave = leaveRepo.findByUserU_Id(user.getU_id());
                    if (leave.getGetLeave()==null) leave.setGetLeave(0);
                    int balance = leave.getGetLeave() + 1;
                    if (leave.getBalLeave()==null) leave.setBalLeave(0);
                    int fb=leave.getTotal()-(leave.getGetLeave()+1);
                    leave.setGetLeave(balance);
                    leave.setBalLeave(fb);
                    leaveRepo.save(leave);
                }

            }
            return Login.equals("student")?"redirect:/Student/m-leave.html":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Student/leave_update.html")
    public Object updateStudent(@RequestParam Map<String,String> getParam, Authentication auth){
            String Login = this.userType(auth);
            if (Login.equals("student")) {
                try {
                    AllLeave allLeave = allLeaveRepo.getById(Long.valueOf(getParam.get("id")));
                    allLeave.setDate_from(getParam.get("fdate"));
                    allLeave.setDate_to(getParam.get("todate"));
                    allLeave.setReason(getParam.get("reason"));
                    switch (getParam.get("ltype")) {
                        case "1":
                            allLeave.setLeaveType("halfday");
                            break;
                        case "2":
                            allLeave.setLeaveType("fullday");
                            break;
                        case "3":
                            allLeave.setLeaveType("multiday");
                            break;
                    }
                        allLeaveRepo.save(allLeave);
                    return "redirect:/Student/m-leave.html";
                } catch (Exception e) {
                    return "login.html";
                }
            }
        return "login.html";
    }
    @GetMapping("Student/leave_update.html")
    public String upadeApplyleave(@RequestParam Map<String,String> getParam,Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("student")){
                AllLeave allLeave=allLeaveRepo.getById(Long.valueOf(getParam.get("id")));
                model.addAttribute("pageName","Student/leave_update");
                model.addAttribute("pageTitle", "Update Leave");
                model.addAttribute("updateLeave", allLeave);
                model.addAttribute("updateId", getParam.get("id"));
                model.addAttribute("userDetails",userRepo.findByEmail(auth.getName()));
            }
            return Login.equals("student")?"Student/update_leave":"redirect:/loginSucess";
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
    private boolean getLeaveBalance(Authentication auth){
        User user=userRepo.findByEmail(auth.getName());
        Leave leave=leaveRepo.findByUserU_Id(user.getU_id());
        if (leave.getGetLeave()==null) leave.setGetLeave(0);
        int blance=leave.getTotal()-leave.getGetLeave();
        return (blance>0)?true : false;
    }
    @GetMapping("Student/test.html")
    @ResponseBody
    public Object testResponse(Authentication auth){
        User user=userRepo.findByEmail(auth.getName());
        List <AllLeave> allleave=allLeaveRepo.findByUser(user.getU_id());
        return allleave;
    }
}
