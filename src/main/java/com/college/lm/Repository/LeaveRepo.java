package com.college.lm.Repository;

import com.college.lm.DataSource.Leave;
import com.college.lm.DataSource.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepo extends JpaRepository<Leave,Long> {
Leave findByUser(User user);
}