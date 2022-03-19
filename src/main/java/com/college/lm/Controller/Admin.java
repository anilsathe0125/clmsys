package com.college.lm.Controller;

import java.util.List;
import java.util.Optional;

import com.college.lm.DataSource.DepartMent;
import com.college.lm.DataSource.User;
import com.college.lm.Repository.DepartMentRepo;
import com.college.lm.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            model.addAttribute("userType","admin");
            model.addAttribute("dlist", dl);
        }
        return Login.equals("admin")?"index":"redirect:/loginSucess";
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
            model.addAttribute("userType","admin");
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
                model.addAttribute("userType", "admin");
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
            drepo.save(departMent);String Login = this.userType(auth);
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
    @PostMapping("Admin/department_delete.html")
    public Object deleteDepartment(@RequestParam(value = "id") Long id,Authentication auth){
        try{
            String Login = this.userType(auth);
            List <User> user=UserRepo.findByDepartmentDid(id);
            if (user.isEmpty()) {
                drepo.deleteById(id);
                return Login.equals("admin") ? "redirect:/Admin/m-department.html" : "redirect:/loginSucess";
            }
            else{
                return Login.equals("admin") ? "redirect:/Admin/m-department.html" : "redirect:/loginSucess";
            }
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
    private String userType(Authentication auth){
            String userName=auth.getName();
            return UserRepo.findByEmail(userName).getUser_role();
    }
}
