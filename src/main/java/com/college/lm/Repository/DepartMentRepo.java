package com.college.lm.Repository;

import com.college.lm.DataSource.DepartMent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartMentRepo extends JpaRepository<DepartMent,Long>{
    
}