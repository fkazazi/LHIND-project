package com.lhind.AnnualLeaveApp.controller;

import com.lhind.AnnualLeaveApp.model.LeaveRequest;
import com.lhind.AnnualLeaveApp.model.User;
import com.lhind.AnnualLeaveApp.service.LeaveRequestService;
import com.lhind.AnnualLeaveApp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;

@AllArgsConstructor
@Controller
@RequestMapping("/api")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final UserService userService;

    @GetMapping("user/my-leaves")
    public String userLeaves(Model model){
        User user = (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("myLeaves", leaveRequestService.getAllLeavesOfUser(user.getId()));
        return "user/userLeaves";
    }

    @GetMapping("supervisor/home")
    public String allLeaves(Model model) throws ParseException {
        model.addAttribute("allLeaves",leaveRequestService.displayAllLeaves());
        return "supervisor/supervisorHome";
    }

    @GetMapping("supervisor/accepted-leaves")
    public String acceptedLeaves(Model model){
        model.addAttribute("activeLeaves", leaveRequestService.getAllLeavesOnStatus(true));
        return "supervisor/activeLeaves";
    }

    @GetMapping("supervisor/rejected-leaves")
    public String rejectedLeaves(Model model){
        model.addAttribute("rejectedLeaves", leaveRequestService.getAllLeavesOnStatus(false));
        return "supervisor/rejectedLeaves";
    }

    @GetMapping("supervisor/pending-leaves")
    public String pendingLeaves(Model model){
        model.addAttribute("pendingLeaves", leaveRequestService.getAllPendingLeaves());
        return "supervisor/pendingLeaves";
    }

    @PostMapping("supervisor/pending-leaves/confirm/{id}")
    public String confirmRequest (@PathVariable("id") Integer id){
        leaveRequestService.confirmRejectLeave(leaveRequestService.getLeaveById(id), true, "");
        return "supervisor/confirmed";
    }

    @PostMapping("supervisor/pending-leaves/reject/{id}")
    public String rejectRequest (@PathVariable("id") Integer id, @RequestParam("message") String message){
        leaveRequestService.confirmRejectLeave(leaveRequestService.getLeaveById(id), false,  message);
        return "supervisor/rejected";
    }

    @RequestMapping(path = "user/new-request")
    public String newRequest(Model model){
        LeaveRequest leaveRequest = new LeaveRequest();
        model.addAttribute("leave_details", leaveRequest);
        return "/user/newRequest";
    }

    @PostMapping("user/new-request/save")
    public String saveRequest(@ModelAttribute("leave_request") LeaveRequest leaveRequest) throws ParseException {
            User user = (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            leaveRequestService.saveLeave(leaveRequest, user);
        return "user/newRequestSuccess";
    }
}
