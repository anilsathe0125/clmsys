package com.college.lm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.college.lm.DataSource.User;
import com.college.lm.Repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
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
    System.out.println(new TestRun().getString());
}
}