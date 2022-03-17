package com.college.lm.Repository;
import java.util.List;

import com.college.lm.DataSource.AllLeave;
import com.college.lm.DataSource.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AllLeaveRepo extends JpaRepository<AllLeave, Long> {
List<AllLeave> findByUser(User user);
}