package com.college.lm.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.college.lm.DataSource.DepartMent;
import com.college.lm.DataSource.Leave;
import com.college.lm.DataSource.User;
import com.college.lm.Repository.DepartMentRepo;
import com.college.lm.Repository.LeaveRepo;
import com.college.lm.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class Admin {
    @Autowired
    DepartMentRepo drepo;
    @Autowired
    UserRepository UserRepo;
    @Autowired
    LeaveRepo leaveRepo;
    @GetMapping("Admin/dashboard.html")
    public String getDashbord(Model model,Authentication auth){
            try{
            String Login=this.userType(auth);
            if(Login.equals("admin")){
            model.addAttribute("pageName","Admin/dashboard");
            model.addAttribute("pageTitle", "Admin Dashboard");
            model.addAttribute("dashboard_department",drepo.findAll().size());
            model.addAttribute("dashboard_staff",UserRepo.findByRole("staff").size());
            model.addAttribute("dashboard_staff_all",UserRepo.findByRole("student"));
            model.addAttribute("dashboard_student",UserRepo.findByRole("student").size());
            model.addAttribute("userDetails",UserRepo.findByEmail(auth.getName()));
            }
            return Login.equals("admin")?"common/index":"redirect:/loginSucess";
            }
            catch(Exception e){
                return "login.html";
            }
    }
    /**
    *-------------------------- Manage Department ---------------------------------------------------
    */
    @GetMapping("Admin/m-department.html")
    public String getDepartment(Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("admin")){
            List<DepartMent> dl=drepo.findAll();
            model.addAttribute("pageName","Admin/m-department");
            model.addAttribute("pageTitle", "Manage Department");
            model.addAttribute("userDetails",UserRepo.findByEmail(auth.getName()));
            model.addAttribute("dlist", dl);
        }
        return Login.equals("admin")?"common/index":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }

    @GetMapping("Admin/department_add.html")
    public String addDepartment(Model model,Authentication auth){
        try{
            model.addAttribute("pageName","Admin/add_department");
            model.addAttribute("pageTitle", "Add Department");
            model.addAttribute("dobj",new DepartMent());
            model.addAttribute("userDetails",UserRepo.findByEmail(auth.getName()));
            String Login=this.userType(auth);
            return Login.equals("admin")?"Admin/add_department":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @GetMapping("Admin/department_update.html")
    public String getUpdateDepartment(@RequestParam(value = "id") Long id, Model model, Authentication auth){
        try{
            if (id==null) return "redirect:/Admin/m-department";
            else {
                DepartMent departMent = new DepartMent();
                Optional<DepartMent> departMent1 = drepo.findById(id);
                departMent.setDname(departMent1.get().getDname());
                departMent.setStatus(departMent1.get().getStatus());
                departMent.setDid(departMent1.get().getDid());
                model.addAttribute("pageTitle", "Update Department");
                model.addAttribute("dobj", departMent);
                model.addAttribute("userDetails",UserRepo.findByEmail(auth.getName()));
            }
            String Login = this.userType(auth);
            return Login.equals("admin")?"Admin/update_department":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Admin/department_update.html")
    public Object updateDepartment(DepartMent departMent,Authentication auth){
        try{
            drepo.save(departMent);
            String Login = this.userType(auth);
            return Login.equals("admin")?"redirect:/Admin/m-department.html":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @GetMapping("Admin/department_delete.html")
    public Object getDeleteDepartment(Model model,@RequestParam(value = "id") Long id,Authentication auth){
        try{
            model.addAttribute("pageTitle", "Delete Department");
            model.addAttribute("did",id);
            String Login = this.userType(auth);
            return Login.equals("admin")?"Admin/delete_department":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Admin/department_add.html")
    public String saveDepartment(DepartMent departMent, Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("admin")){
                drepo.save(departMent);
                return "redirect:/Admin/m-department.html";
            }
            else return "redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }

    /**
     *-------------------------- Manage Student ----------------------------------------
     */
    @GetMapping("Admin/m-student.html")
    public String getStudent(Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("admin")){
                List<User> st=UserRepo.findByRole("student");
                model.addAttribute("pageName","Admin/m-student");
                model.addAttribute("pageTitle", "Manage Student");
                model.addAttribute("userDetails",UserRepo.findByEmail(auth.getName()));
                model.addAttribute("slist", st);
            }
            return Login.equals("admin")?"common/index":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @GetMapping("Admin/student_update.html")
    public String getUpdateStudent(@RequestParam(value = "id") Long id, Model model, Authentication auth){
        try{
            if (id==null) return "redirect:/Admin/m-student";
            else {
                model.addAttribute("pageTitle", "Update Student");
                model.addAttribute("updateUser",id);
                model.addAttribute("userDetails",UserRepo.findByEmail(auth.getName()));
            }
            String Login = this.userType(auth);
            return Login.equals("admin")?"Admin/update_student":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Admin/student_update.html")
    public Object updateStudent(@RequestParam Map<String,String> getParam,Authentication auth){
        try{
            String Login = this.userType(auth);
            if (getParam.get("balance").length()>0){
                this.updateBalance(getParam);
            }
            if (!getParam.get("password").equals("") && getParam.get("password").length()>=6){
                User user=UserRepo.getById(Long.valueOf(getParam.get("id")));
                BCryptPasswordEncoder b=new BCryptPasswordEncoder();
                user.setPassword(b.encode(getParam.get("password")));
                UserRepo.save(user);
            }
            return Login.equals("admin")?"redirect:/Admin/m-student.html":"redirect:/loginSucess";
        }
        catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     *-------------------------- Manage Staff ----------------------------------------
     */
    @GetMapping("Admin/m-staff.html")
    public String getStaff(Model model,Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("admin")){
                List<User> hod=UserRepo.findByRole("hod");
                List<User> st=UserRepo.findByRole("staff");
                hod.addAll(st);
                model.addAttribute("pageName","Admin/m-staff");
                model.addAttribute("pageTitle", "Manage Staff");
                model.addAttribute("userDetails",UserRepo.findByEmail(auth.getName()));
                model.addAttribute("slist",hod);
            }
            return Login.equals("admin")?"common/index":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @GetMapping("Admin/staff_update.html")
    public String getUpdateStaff(@RequestParam(value = "id") Long id, Model model, Authentication auth){
        try{
            if (id==null) return "redirect:/Admin/m-staff";
                model.addAttribute("pageTitle", "Update Staff");
                model.addAttribute("staffUpdate",UserRepo.getById(id));
                model.addAttribute("userDetails",UserRepo.findByEmail(auth.getName()));
                String Login = this.userType(auth);
                return Login.equals("admin")?"Admin/update_staff":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Admin/staff_update.html")
    public Object updateStaff(@RequestParam Map<String,String> getParam,Authentication auth){
        try{
            User user=UserRepo.getById(Long.valueOf(getParam.get("id")));
            if (!getParam.get("balance").equals("")){
                this.updateBalance(getParam);
            }
            if (!getParam.get("role").equals("")){
                user.setRole(getParam.get("role"));
            }
            if (!getParam.get("password").equals("") && getParam.get("password").length()>=6){
                BCryptPasswordEncoder b=new BCryptPasswordEncoder();
                user.setPassword(b.encode(getParam.get("password")));
            }
            UserRepo.save(user);
            String Login = this.userType(auth);
            return Login.equals("admin")?"redirect:/Admin/m-staff.html":"redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Admin/unable_staff.html")
    @ResponseBody
    public String unableStaff(@RequestParam Map<String,String> getParam, Authentication auth){
        try {
            if (getParam.get("user_id")!=null) {
                String Login = this.userType(auth);
                User user=UserRepo.getById(Long.valueOf(getParam.get("user_id")));
                if (getParam.get("user_status").equals("1")) {
                    user.setStatus("active");
                    UserRepo.save(user);
                    return this.MsgRes(Login,"success","Unable successfully !");
                }
                else if(getParam.get("user_status").equals("0")){
                    user.setStatus(null);
                    UserRepo.save(user);
                    return this.MsgRes(Login,"success","Disable successfully !");
                }
                else {
                    return this.MsgRes(Login,"error","Department has some user present !");
                }
            }
            else {
                return "{\"status\":\"error\",\"message\":\"User not found !\"}";
            }
        }
        catch(Exception e){
            return "login.html";
        }
    }
    @PostMapping("Admin/staff_permission.html")
    @ResponseBody
    public String permissionStaff(@RequestParam Map<String,String> getParam, Authentication auth){
        try {
            if (getParam.get("user_id")!=null) {
                String Login = this.userType(auth);
                User user=UserRepo.getById(Long.valueOf(getParam.get("user_id")));
                if (getParam.get("user_permission").equals("1")) {
                    user.setPermission(true);
                    UserRepo.save(user);
                    return this.MsgRes(Login,"success","Give permission successfully !");
                }
                else if(getParam.get("user_permission").equals("0")){
                    user.setPermission(false);
                    UserRepo.save(user);
                    return this.MsgRes(Login,"success","Remove permission successfully !");
                }
                else {
                    return this.MsgRes(Login,"error","User has not available !");
                }
            }
            else {
                return "{\"status\":\"error\",\"message\":\"User not found !\"}";
            }
        }
        catch(Exception e){
            return "login.html";
        }
    }
    /**
    * All Function Delete data
    *-------------------------- Common Delete All Category ----------------------------
    */
    @PostMapping("/Admin/delete_record")
    @ResponseBody
    public String deleteDepartment(@RequestParam Map<String,String> getParam, Authentication auth){
        try {
            if (getParam.get("table_name").equals("department")) {
                String Login = this.userType(auth);
                List<User> user = UserRepo.findByDepartmentDid(Long.valueOf(getParam.get("row_id")));
                if (user.isEmpty()) {
                    drepo.deleteById(Long.valueOf(getParam.get("row_id")));
                    return this.MsgRes(Login,"success","Deleted successfully !");
                } else {
                    return this.MsgRes(Login,"error","Department has some user present !");
                }
            }
            else {
               return "{\"status\":\"error\",\"message\":\"Table not found !\"}";
            }
        }
        catch(Exception e){
            return "login.html";
        }
    }

    private String userType(Authentication auth){
            String userName=auth.getName();
            return UserRepo.findByEmail(userName).getRole();
    }
    private String MsgRes(String Login,String status, String message){
        return Login.equals("admin") ? "{\"status\":\""+ status +"\",\"message\":\""+ message +"\"}" :
                "{\"status\":\"error\",\"message\":\"You are different user or not login.. !\"}";
    }
    private void updateBalance(@RequestParam Map<String,String> getParam){
        String number=getParam.get("balance");
        if (number.chars().allMatch(Character::isDigit)) {
            Leave leave = leaveRepo.findByUserU_Id(Long.valueOf(getParam.get("id")));
            float balance = leave.getTotal() + Integer.parseInt(getParam.get("balance"));
            leave.setTotal(balance);
            leaveRepo.save(leave);
        }
    }
    @GetMapping("Admin/test.html")
    @ResponseBody
    public Object Test(Authentication auth){
        try{
            String Login=this.userType(auth);
            if(Login.equals("admin")){
               return UserRepo.findByEmail("anil@gmail.com");
            }
            else return "redirect:/loginSucess";
        }
        catch(Exception e){
            return "login.html";
        }
    }

}
