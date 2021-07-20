package com.lhind.AnnualLeaveApp.security;

import com.lhind.AnnualLeaveApp.model.User;
import com.lhind.AnnualLeaveApp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);

        String redirectUrl = request.getContextPath();

        if (user.getRole().name().equals(ApplicationRoles.USER.name())){
            redirectUrl = "/api/user/home";
        } else if (user.getRole().name().equals(ApplicationRoles.SUPERVISOR.name())){
            redirectUrl = "/api/supervisor/home";
        } else if (user.getRole().name().equals(ApplicationRoles.ADMIN.name())){
            redirectUrl = "/api/admin/home";
        }

        response.sendRedirect(redirectUrl);
    }
}
