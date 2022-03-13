package com.college.lm.Services;

import com.college.lm.DataSource.User;
import com.college.lm.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetServie implements UserDetailsService{
    @Autowired
    private UserRepository userRpo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRpo.findByEmail(email);
        if(user==null){
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetailsAuth(user);
    }


} 