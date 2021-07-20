package com.lhind.AnnualLeaveApp.repository;

import com.lhind.AnnualLeaveApp.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    @Query(nativeQuery = true, value = "select * from leave_details where active=true")
    public List<LeaveRequest> getAllActiveLeaves();

    @Query(nativeQuery = true, value = "select * from leave_details where user_id=? and pending=false order by request_id desc")
    public List<LeaveRequest> getAllLeavesOfUser(Integer userId);

    @Query (nativeQuery = true, value = "select * from leave_details where flag=?")
    public List<LeaveRequest> getAllLeavesOnStatus(boolean flag);

    @Query (nativeQuery = true, value = "select * from leave_details where pending=true")
    public List<LeaveRequest> getAllPendingLeaves ();

    @Query (nativeQuery = true, value = "select * from leave_details where pending=false")
    public List<LeaveRequest> getAll();
}
