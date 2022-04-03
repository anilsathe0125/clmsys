package com.college.lm.Repository;

import com.college.lm.DataSource.AllLeave;
import com.college.lm.DataSource.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    List<User> findByDepartmentDid(Long id);
    List<User> findByRole(String roll);
    Optional<User> findById(Long id);
    @Query(value = "SELECT * FROM users WHERE dpid=?1 ORDER BY u_id DESC", nativeQuery = true)
    List<User> findByStudent(Long did);
    @Query(value = "SELECT * FROM users WHERE dpid=?1 and role=?2 ORDER BY u_id DESC", nativeQuery = true)
    List<User> findByType(Long did,String type);
    @Query(value = "SELECT * FROM users WHERE dpid=?1 and role='staff' and permission=1 and status='active' ORDER BY u_id DESC", nativeQuery = true)
    List<User> findByResponsible(Long did);
    @Query(value = "SELECT * FROM users WHERE dpid=?1 and role='hod' ORDER BY u_id DESC", nativeQuery = true)
    List<User> findByHod(Long did);
}