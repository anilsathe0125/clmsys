package com.college.lm;

import com.college.lm.DataSource.User;
import com.college.lm.Repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserTest {
@Autowired
private UserRepository repo;
@Autowired
private TestEntityManager entityManager;
@Test
public void testCreateUser(){
    User user=new User();
    user.setEmail("abc01@in.com");
    user.setFirst_name("ABC");
    user.setLast_name("XYZ");
    user.setMobile_no("452365");
    user.setPassword("Test#8555");
    user.setUser_role("student");

    repo.save(user);
} 
@Test
public void testUserByEmail(){
    BCryptPasswordEncoder bEncoder=new BCryptPasswordEncoder();
    System.out.println(bEncoder.encode("Admin@8585"));
}
}
//INSERT INTO `users` (`u_id`, `email`, `first_name`, `last_name`, `mobile_no`, `password`, `permision`, `status`, `timestamp`, `user_role`, `dpid`) VALUES (NULL, 'admin@clmsys.in', 'Administrator', 'Admin', '8888888888', '$2a$10$QogKOqZrHx2c.KgAfGhlIe41mXTiXlWqib8DsVJTplgWY4Il8OFYK', b'000', 'approved', NULL, 'admin', '')