package com.college.lm.Repository;
import java.util.List;

import com.college.lm.DataSource.AllLeave;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AllLeaveRepo extends JpaRepository<AllLeave, Long> {
    @Query(value = "SELECT * FROM all_leaves WHERE u_id=?1 ORDER BY l_id DESC", nativeQuery = true)
    List<AllLeave> findByUser(Long id);
    @Query(value = "SELECT * FROM all_leaves WHERE did=?1 and status='pending' ORDER BY l_id DESC", nativeQuery = true)
    List<AllLeave> findByPending(Long id);
    @Query(value = "SELECT * FROM all_leaves WHERE u_id=?1 and status=?2 ORDER BY l_id DESC", nativeQuery = true)
    List<AllLeave> findByPending(Long id,String status);
    @Query(value = "SELECT * FROM all_leaves WHERE did=?1 ORDER BY l_id DESC", nativeQuery = true)
    List<AllLeave> findByDepartment(Long id);
    @Query(value = "SELECT al.l_id, al.date_from,al.date_to,al.appby,al.status,al.u_id,al.reason,al.leave_type,al.did,al.message,u.first_name,u.last_name"+
    " FROM all_leaves AS al INNER JOIN users AS u ON al.u_id=u.u_id"+
    " WHERE al.did=?1 AND u.role='student' ORDER BY al.status='pending'", nativeQuery = true)
    List<Object> findByStudentPendingLeave(Long did);
    @Query(value = "SELECT * FROM all_leaves INNER JOIN users ON all_leaves.u_id=users.u_id WHERE users.role='student' and all_leaves.did=?1 and all_leaves.status='pending'", nativeQuery = true)
    List<AllLeave> studentPendingLeaveCount(Long did);
    @Query(value = "SELECT all_leaves.l_id, all_leaves.date_from,all_leaves.date_to,all_leaves.appby,all_leaves.status,all_leaves.u_id,all_leaves.reason,all_leaves.leave_type,all_leaves.did,all_leaves.message,users.first_name,users.last_name " +
            " FROM all_leaves INNER JOIN users ON all_leaves.u_id=users.u_id WHERE users.role='staff' and all_leaves.did=?1 and all_leaves.status='pending' or all_leaves.status='approve' ORDER BY FIELD(all_leaves.status,'pending','approve')", nativeQuery = true)
    List<Object> findByStaffPendingLeave(Long did);
}