package com.lhind.AnnualLeaveApp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class pass {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("admin"));
    }
}
