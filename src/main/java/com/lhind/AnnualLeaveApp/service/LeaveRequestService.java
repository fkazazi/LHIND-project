package com.lhind.AnnualLeaveApp.service;

import com.lhind.AnnualLeaveApp.model.LeaveRequest;
import com.lhind.AnnualLeaveApp.model.User;

import java.util.List;

public interface LeaveRequestService {

    List<LeaveRequest> displayAllLeaves ();

    List<LeaveRequest> getAllLeavesOnStatus(boolean flag);

    List<LeaveRequest> getAllLeavesOfUser(Integer userId);

    List<LeaveRequest> getAllPendingLeaves ();

    LeaveRequest getLeaveById (Integer id);

    void deleteLeave (Integer id);

    LeaveRequest saveLeave(LeaveRequest leaveRequest, User user);

    void confirmRejectLeave(LeaveRequest leaveRequest, boolean flag, String message);
}
