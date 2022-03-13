package com.college.lm;

import java.sql.Timestamp;

public class TestRun {
    public String getString() {
    //    BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
    //    String rawPassword="Test#8555";
    Long date=System.currentTimeMillis();
    Timestamp ts=new Timestamp(date);
       return date.toString();
    }
}
