package com.college.lm.DataSource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long u_id;
@Column(nullable = false,unique = true,length = 120)
private String email;
@Column(nullable = false,length = 64)
private String password;
@Column(nullable = false, length = 30)
private String first_name;
@Column(nullable = false, length = 30)
private String last_name;
@Column(length = 10)
private String mobile_no;
@Column(length = 10)
private String user_role;

    public long getU_id() {
        return this.u_id;
    }

    public void setU_id(long u_id) {
        this.u_id = u_id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMobile_no() {
        return this.mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getUser_role() {
        return this.user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

}