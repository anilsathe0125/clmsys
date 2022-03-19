package com.college.lm.Repository;

import com.college.lm.DataSource.DepartMent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartMentRepo extends JpaRepository<DepartMent,Long>{
    List<DepartMent> findAllByStatus(boolean val);
}