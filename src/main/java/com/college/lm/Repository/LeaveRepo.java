package com.college.lm.Repository;

import com.college.lm.DataSource.Leave;
import com.college.lm.DataSource.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LeaveRepo extends JpaRepository<Leave,Long> {
    @Query(value = "SELECT * FROM leaves WHERE uid=?1 ORDER BY lid DESC", nativeQuery = true)
    Leave findByUserU_Id(Long u_id);
}