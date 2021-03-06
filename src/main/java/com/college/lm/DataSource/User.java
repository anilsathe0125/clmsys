package com.college.lm.DataSource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Entity
@Table(name="users")
public class User implements Serializable {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long u_id;
@Column(name="email",nullable = false,unique = true,length = 120)
private String email;
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
@Column(nullable = false,length = 64)
private String password;
@Column(nullable = false, length = 30)
private String first_name;
@Column(nullable = false, length = 30)
private String last_name;
@Column(length = 10)
private String mobile_no;
@Column(length = 20)
private String role;
@Column(length = 20)
private String user_gender;
private boolean permission;
@ManyToOne(fetch= FetchType.LAZY,optional=false)
@JoinColumn(name="dpid")
@OnDelete(action= OnDeleteAction.CASCADE)
private DepartMent department;
@Column(length = 20)
private String status;
@Column(length = 20)
private Long timestamp;


    public User() {
    }


    public User(Long u_id, String email, String password, String first_name, String last_name, String mobile_no, String role, boolean permission, DepartMent department, String status) {
        this.u_id = u_id;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobile_no = mobile_no;
        this.role = role;
        this.permission = permission;
        this.department = department;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }
 


    /**
     * @return Long return the u_id
     */
    public Long getU_id() {
        return u_id;
    }

    /**
     * @param u_id the u_id to set
     */
    public void setU_id(Long u_id) {
        this.u_id = u_id;
    }

    /**
     * @return String return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return String return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return String return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return String return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return String return the mobile_no
     */
    public String getMobile_no() {
        return mobile_no;
    }

    /**
     * @param mobile_no the mobile_no to set
     */
    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    /**
     * @return String return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the user_role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return boolean return the permission
     */
    public boolean isPermission() {
        return permission;
    }

    /**
     * @param permission the permission to set
     */
    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    /**
     * @return DepartMent return the department
     */
    public DepartMent getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(DepartMent department) {
        this.department = department;
    }

    /**
     * @return String return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }
}