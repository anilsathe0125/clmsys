package com.college.lm.DataSource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "department")
public class DepartMent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long did;
    @Column(nullable =false,length = 20)
    private String dname;
    @Column(nullable = false)
    private boolean status;

    public Long getDid() {
        return this.did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getDname() {
        return this.dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public boolean isStatus() {
        return this.status;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
