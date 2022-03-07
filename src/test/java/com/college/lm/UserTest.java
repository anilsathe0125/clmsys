package com.college.lm;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    user.setEmail("abc@in.com");
    user.setFirst_name("ABC");
    user.setLast_name("XYZ");
    user.setMobile_no("8208513426");
    user.setPassword("Test#8555");
    user.setUser_role("student");
    User saveUser = repo.save(user);
    User userExit = entityManager.find(User.class,saveUser.getU_id());
    //assertThat(saveUser, userExit);
} 
}