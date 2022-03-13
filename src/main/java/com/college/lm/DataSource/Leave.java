package com.college.lm.DataSource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="leave")
public class Leave {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long lid;
    @Column(length=5)
    private Integer total;
    @Column(length=5)
    private Integer getLeave;
    @Column(length=5)
    private Integer balLeave;
    @OneToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="uid")
    @OnDelete(action=OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;
    @Column(length=5)
    private Long dete;
    

    /**
     * @return Long return the lid
     */
    public Long getLid() {
        return lid;
    }

    /**
     * @param lid the lid to set
     */
    public void setLid(Long lid) {
        this.lid = lid;
    }

    /**
     * @return Integer return the total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * @return Integer return the getLeave
     */
    public Integer getGetLeave() {
        return getLeave;
    }

    /**
     * @param getLeave the getLeave to set
     */
    public void setGetLeave(Integer getLeave) {
        this.getLeave = getLeave;
    }

    /**
     * @return Integer return the balLeave
     */
    public Integer getBalLeave() {
        return balLeave;
    }

    /**
     * @param balLeave the balLeave to set
     */
    public void setBalLeave(Integer balLeave) {
        this.balLeave = balLeave;
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

    /**
     * @return Long return the dete
     */
    public Long getDete() {
        return dete;
    }

    /**
     * @param dete the dete to set
     */
    public void setDete(Long dete) {
        this.dete = dete;
    }

}
