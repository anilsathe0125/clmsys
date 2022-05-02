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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
            try {
            String Login=this.userType(auth);
            if(this.userPermission(Login)) {
                User user = userRepo.findByEmail(auth.getName());
                Leave leave = leaveRepo.findByUserU_Id(user.getU_id());
                model.addAttribute("pageName", "Staff/dashboard");
                model.addAttribute("pageTitle", "Staff Dashboard");
                model.addAttribute("totalLeave", leave);
                if (Login.equals("staff")) {
                    model.addAttribute("pendingLeave", allLeaveRepo.findByPending(user.getU_id(), "pending").size());
                    model.addAttribute("approveLeave", allLeaveRepo.findByPending(user.getU_id(), "approve").size());
                    model.addAttribute("studentPending", allLeaveRepo.studentPendingLeaveCount(user.getDepartment().getDid()).size());
                }
                else {
                    model.addAttribute("totStudent",userRepo.findByType(user.getDepartment().getDid(),"student").size());
                    model.addAttribute("totStaff", userRepo.findByType(user.getDepartment().getDid(),"staff").size());
                    model.addAttribute("staffPending", allLeaveRepo.findByStaffPendingLeave(user.getDepartment().getDid()).size());
                }
                model.addAttribute("listLeave", allLeaveRepo.findByPending(user.getU_id(), "pending"));
                model.addAttribute("userDetails", user);
                return this.userPermission(Login)?"common/index":"redirect:/loginSucess";
            }
            else return "redirect:/loginSucess";
            }
            catch (Exception e)
            {
                return "redirect:/login.html";
            }
    }
    //---------------------
    @GetMapping("Staff/m-leave.html")
    public Object mleaveStaff(Model model, Authentication auth){
        try{
            User user=userRepo.findByEmail(auth.getName());
            List <AllLeave> allleave=allLeaveRepo.findByUser(user.getU_id());
            model.addAttribute("pageName","Staff/m-leave");
            model.addAttribute("responsible",userRepo.findByHod(user.getDepartment().getDid()));
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
    public String setApplyleave(@RequestParam Map<String,String> getParam, Authentication auth){
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
                    if (leave.getGetLeave()==0.0) leave.setGetLeave(0);
                    float balance = leave.getGetLeave() + 1;
                    if (leave.getBalLeave()==0.0) leave.setBalLeave(0);
                    float fb=leave.getTotal()-(leave.getGetLeave()+1);
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
                List<Object> allLeaves=allLeaveRepo.findByStudentPendingLeave(user.getDepartment().getDid());
                model.addAttribute("aleave",allLeaves);
                model.addAttribute("error_message",null);
            }
            else if(Login.equals("hod") && user.isPermission()){
                List<Object> allLeaves=allLeaveRepo.findByStaffPendingLeave(user.getDepartment().getDid());
                model.addAttribute("aleave",allLeaves);
                model.addAttribute("error_message",null);
            }
            else if (Login.equals("staff") && user.getStatus().equals("active")){
                model.addAttribute("error_message","You have not permission");
            }
            else {
                model.addAttribute("error_message","Activation pending by admin");
            }
            return this.userPermission(Login)?"common/index":"redirect:/loginSucess";
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
            model.addAttribute("id",getParam.get("id"));
            model.addAttribute("userDetails",user);
            String Login = this.userType(auth);
            return this.userPermission(Login)?"Staff/approve_leave":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Staff/leave_approve.html")
    public Object AsetleaveStdent(@RequestParam Map<String,String> getParam,Model model,Authentication auth){
        try{
            User user=userRepo.findByEmail(auth.getName());
            AllLeave allLeave=allLeaveRepo.getById(Long.valueOf(getParam.get("id")));
            allLeave.setAppby(user.getFirst_name()+' '+user.getLast_name());
            switch (getParam.get("status")){
                case "1":
                    allLeave.setStatus("reject");
                    Leave leave= leaveRepo.findByUserU_Id(allLeave.getUser().getU_id());
                    float nb=0.0F;
                    float ngl= 0.0F;
                    float b=leave.getBalLeave();
                    float gl=leave.getGetLeave();
                    if (allLeave.getLeaveType().equals("halfday")){
                        nb= (float) (b+0.5);
                        ngl=  (float) (gl-0.5);
                    }
                    if (allLeave.getLeaveType().equals("fullday"))
                    {
                        nb= (float) (b+1.0);
                        ngl=  (float) (gl-1.0);
                    }
                    if (allLeave.getLeaveType().equals("multiday")){
                        nb= (float) (b+this.calDay(allLeave.getDate_from(),allLeave.getDate_to()));
                        ngl=  (float) (gl-this.calDay(allLeave.getDate_from(),allLeave.getDate_to()));
                    }
                    if (nb==0 & nb!=0.5) leave.setBalLeave(1);
                    leave.setBalLeave(nb);
                    leave.setGetLeave(ngl);
                    leaveRepo.save(leave);
                    break;
                case "2":
                    allLeave.setStatus("approve");
                    break;
            }
            allLeave.setMessage(getParam.get("message"));
            allLeaveRepo.save(allLeave);
            String Login = this.userType(auth);
            return this.userPermission(Login)?"redirect:/Staff/m-approve.html":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @GetMapping("Staff/get_password.html")
    public String userPassword(Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(this.userPermission(Login)){
                model.addAttribute("pageTitle", "Change Password");
                model.addAttribute("userDetails",userRepo.findByEmail(auth.getName()));
            }
            return this.userPermission(Login)?"common/change_password":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Staff/password_update.html")
    public Object updatePassword(@RequestParam Map<String,String> getParam,Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(this.userPermission(Login)){
                User user=userRepo.getById(Long.valueOf(getParam.get("id")));
                BCryptPasswordEncoder be=new BCryptPasswordEncoder();
                if (!getParam.get("password").equals("")) user.setPassword(be.encode(getParam.get("password")));
                userRepo.save(user);
            }
            return this.userPermission(Login)?"redirect:/Staff/profile.html":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @GetMapping("Staff/profile.html")
    public String userProfile(Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(this.userPermission(Login)){
                model.addAttribute("pageName","common/profile");
                model.addAttribute("pageTitle", "My Profile");
                model.addAttribute("userDetails",userRepo.findByEmail(auth.getName()));
            }
            return this.userPermission(Login)?"common/index":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Staff/profile_update.html")
    public Object updateProfile(@RequestParam Map<String,String> getParam,Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(this.userPermission(Login)){
                User user=userRepo.getById(Long.valueOf(getParam.get("id")));
                if (!getParam.get("gender").equals("")) user.setUser_gender(getParam.get("gender"));
                if (!getParam.get("email").equals("")) user.setEmail(getParam.get("email"));
                if (!getParam.get("mobile_no").equals("")) user.setMobile_no(getParam.get("mobile_no"));
                userRepo.save(user);
            }
            return this.userPermission(Login)?"redirect:/Staff/profile.html":"redirect:/loginSucess";
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
        if (leave.getGetLeave()==0.0) leave.setGetLeave(0);
        float blance=leave.getTotal()-leave.getGetLeave();
        return (blance>0)?true : false;
    }
    private Float calDay(String d1,String d2){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDate da1 = LocalDate.parse(d1, formatter);
        LocalDate da2 = LocalDate.parse(d2, formatter);
        return Float.valueOf(ChronoUnit.DAYS.between(da1,da2));
    }
    private boolean userPermission(String Login){
        return (Login.equals("staff")||Login.equals("hod"))?true:false;
    }
    @GetMapping("Staff/test.html")
    @ResponseBody
    public Object testResponse(Authentication auth){
        //User user=userRepo.findByEmail(auth.getName());
        //List<Object> allLeaves=allLeaveRepo.findByStaffPendingLeave(user.getDepartment().getDid());
        return userRepo.findByEmail(auth.getName());
    }
}
