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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Entity
@Table(name = "AllLeaves")
public class AllLeave {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long l_id;
    @Column(length=30)
    private String date_time;
    @Column(length=30)
    private String date_from;
    @Column(length=30)
    private String date_to;
    @Column(length=10)
    private String status;
    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    @JoinColumn(name="u_id")
    @OnDelete(action=OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;
    @Column(length=20)
    private String appby;

    public AllLeave() {
    }

    public AllLeave(String date_time, String date_from, String date_to, String status, User user) {
        this.date_time = date_time;
        this.date_from = date_from;
        this.date_to = date_to;
        this.status = status;
        this.user = user;
    }
    

    /**
     * @return Long return the l_id
     */
    public Long getL_id() {
        return l_id;
    }

    /**
     * @param l_id the l_id to set
     */
    public void setL_id(Long l_id) {
        this.l_id = l_id;
    }

    /**
     * @return String return the date_time
     */
    public String getDate_time() {
        return date_time;
    }

    /**
     * @param date_time the date_time to set
     */
    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    /**
     * @return String return the date_from
     */
    public String getDate_from() {
        return date_from;
    }

    /**
     * @param date_from the date_from to set
     */
    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    /**
     * @return String return the date_to
     */
    public String getDate_to() {
        return date_to;
    }

    /**
     * @param date_to the date_to to set
     */
    public void setDate_to(String date_to) {
        this.date_to = date_to;
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

    /**
     * @return User return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

}
