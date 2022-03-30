package com.college.lm.DataSource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="leaves")
public class Leave {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long lid;
    @Column(length=5)
    private float total;
    @Column(length=5)
    private float getLeave;
    @Column(length=5)
    private float balLeave;
    @OneToOne
    @JoinColumn(name="uid")
    private User user;
    private String dete;
    

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
    public float getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(float total) {
        this.total = total;
    }

    /**
     * @return Integer return the getLeave
     */
    public float getGetLeave() {
        return getLeave;
    }

    /**
     * @param getLeave the getLeave to set
     */
    public void setGetLeave(float getLeave) {
        this.getLeave = getLeave;
    }

    /**
     * @return Integer return the balLeave
     */
    public float getBalLeave() {
        return balLeave;
    }

    /**
     * @param balLeave the balLeave to set
     */
    public void setBalLeave(float balLeave) {
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
    public String getDete() {
        return dete;
    }

    /**
     * @param dete the dete to set
     */
    public void setDete(String dete) {
        this.dete = dete;
    }

}
