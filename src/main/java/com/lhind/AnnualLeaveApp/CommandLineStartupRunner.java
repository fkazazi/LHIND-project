package com.lhind.AnnualLeaveApp;

import com.lhind.AnnualLeaveApp.model.User;
import com.lhind.AnnualLeaveApp.security.ApplicationRoles;
import com.lhind.AnnualLeaveApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineStartupRunner implements CommandLineRunner {
    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {
        User admin = new User("admin@admin.com", "admin", ApplicationRoles.ADMIN.name(), "Admin", "Admin");
        userService.save(admin);
    }
}
