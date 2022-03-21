package com.college.lm.Repository;

import com.college.lm.DataSource.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    List<User> findByDepartmentDid(Long id);
    List<User> findByRole(String roll);
}