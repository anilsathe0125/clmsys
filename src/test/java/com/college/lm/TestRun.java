package com.college.lm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestRun {
    public String getString() {
       BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
       String rawPassword="Test#8555";
       return bCryptPasswordEncoder.encode(rawPassword);
    }
}
