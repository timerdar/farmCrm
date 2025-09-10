package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.model.Admin;
import com.timerdar.farmCrm.repository.AdminRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminDetailsService implements UserDetailsService {

    @Autowired private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(admin.getLogin(), admin.getPasswordHash(), Collections.emptyList());
    }

    public Admin createAdmin(String login, String passwordHash){
        return adminRepository.save(new Admin(0L, login, passwordHash));
    }

    public boolean isAdminCreated(String username){
        return adminRepository.findByLogin(username).isPresent();
    }
}
