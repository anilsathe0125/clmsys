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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
@Controller
public class Staff {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private DepartMentRepo departMentRepo;
    @Autowired
    private LeaveRepo leaveRepo;
    @Autowired
    private AllLeaveRepo allLeaveRepo;

    @GetMapping("Staff/dashboard.html")
    public String getHome(Model model, Authentication auth){
        try{
            String Login=this.userType(auth);
            if(this.userPermission(Login)){
                User user=userRepo.findByEmail(auth.getName());
                Leave leave=leaveRepo.findByUserU_Id(user.getU_id());
                model.addAttribute("pageName","Staff/dashboard");
                model.addAttribute("pageTitle", "Staff Dashboard");
                model.addAttribute("totalLeave",leave);
                if(Login.equals("staff")) {
                    model.addAttribute("pendingLeave", allLeaveRepo.findByPending(user.getU_id(), "pending").size());
                    model.addAttribute("approveLeave", allLeaveRepo.findByPending(user.getU_id(), "approve").size());
                    model.addAttribute("studentPending",allLeaveRepo.findByStudentPendingLeave(user.getDepartment().getDid()).size());
                }
                model.addAttribute("listLeave",allLeaveRepo.findByPending(user.getU_id(),"pending"));
                model.addAttribute("userDetails",user);
            }
            return Login.equals("staff")?"common/index":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    //---------------------
    @GetMapping("Staff/m-leave.html")
    public Object mleaveStaff(Model model, Authentication auth){
        try{
            User user=userRepo.findByEmail(auth.getName());
            List <AllLeave> allleave=allLeaveRepo.findByUser(user.getU_id());
            model.addAttribute("pageName","Staff/m-leave");
            model.addAttribute("pageTitle", "Manage Leave");
            model.addAttribute("al",allleave);
            model.addAttribute("userDetails",user);
            String Login = this.userType(auth);
            return Login.equals("staff")?"common/index":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @GetMapping("Staff/apply_leave.html")
    public String getApplyleave(Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("staff")){
                model.addAttribute("pageName","Staff/apply_leave");
                model.addAttribute("pageTitle", "Apply Leave");
                model.addAttribute("userDetails",userRepo.findByEmail(auth.getName()));
            }
            return Login.equals("staff")?"Staff/apply_leave":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Staff/apply_leave.html")
    public String setApplyleave(@RequestParam Map<String,String> getParam, Model model, Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("staff")){
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
            return Login.equals("staff")?"redirect:/Staff/m-leave.html":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Staff/leave_update.html")
    public Object updateStaff(@RequestParam Map<String,String> getParam, Authentication auth){
        String Login = this.userType(auth);
        if (Login.equals("staff")) {
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
                return "redirect:/Staff/m-leave.html";
            } catch (Exception e) {
                return "login.html";
            }
        }
        return "login.html";
    }
    @GetMapping("Staff/leave_update.html")
    public String upadeApplyleave(@RequestParam Map<String,String> getParam,Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("staff")){
                AllLeave allLeave=allLeaveRepo.getById(Long.valueOf(getParam.get("id")));
                model.addAttribute("pageName","Staff/leave_update");
                model.addAttribute("pageTitle", "Update Leave");
                model.addAttribute("updateLeave", allLeave);
                model.addAttribute("updateId", getParam.get("id"));
                model.addAttribute("userDetails",userRepo.findByEmail(auth.getName()));
            }
            return Login.equals("staff")?"Staff/update_leave":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @GetMapping("Staff/m-approve.html")
    public String getApproveLeave(Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            User user=userRepo.findByEmail(auth.getName());
            model.addAttribute("pageName","Staff/m-approve");
            model.addAttribute("pageTitle", "Manage Leave Approve");
            model.addAttribute("userDetails",userRepo.findByEmail(auth.getName()));
            if(Login.equals("staff") && user.getStatus().equals("active")
            && user.isPermission()){
                List<AllLeave> allLeaves=allLeaveRepo.findByStudentPendingLeave(user.getDepartment().getDid());
                model.addAttribute("aleave",allLeaves);
                model.addAttribute("error_message",null);
            }
            else if (Login.equals("staff") && user.getStatus().equals("active")){
                model.addAttribute("error_message","You have not permission");
            }
            else {
                model.addAttribute("error_message","Activation pending by admin");
            }
            return Login.equals("staff")?"common/index":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @GetMapping("Staff/leave_approve.html")
    public Object AleaveStdent(@RequestParam Map<String,String> getParam,Model model,Authentication auth){
        try{
            User user=userRepo.findByEmail(auth.getName());
            List <AllLeave> allleave=allLeaveRepo.findByUser(user.getU_id());
            model.addAttribute("pageName","Staff/approve_leave");
            model.addAttribute("pageTitle", "Approve Leave");
            model.addAttribute("al",allleave);
            model.addAttribute("userDetails",user);
            String Login = this.userType(auth);
            return this.userPermission(Login)?"Staff/approve_leave":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    //---------------------
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
    private boolean userPermission(String Login){
        return (Login.equals("staff") || Login.equals("hod"))?true:false;
    }
    @GetMapping("Staff/test.html")
    @ResponseBody
    public Object testResponse(Authentication auth){
        User user=userRepo.findByEmail(auth.getName());
        List<AllLeave> allleave= allLeaveRepo.findByStudentPendingLeave(user.getDepartment().getDid());
        return allleave.size();
    }
}
