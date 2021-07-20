package com.lhind.AnnualLeaveApp.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "leave_details")
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer id;

    @NotNull(message = "Please enter start date!")
    @Column(name = "from_date", nullable = false)
    private String fromDate;

    @NotNull(message = "Please enter end date!")
    @Column(name = "to_date", nullable = false)
    private String toDate;

    @NotEmpty(message = "Please select leave type!")
    @Column(name = "leave_type", nullable = false)
    private String leaveType;

    @NotEmpty(message = "Please provide a reason for the leave!")
    @Column(name = "reason")
    private String reason;

    @Column(name = "duration")
    private int duration;

    @Column(name = "pending")
    private boolean pending = true;

    @Column(name = "flag")
    private boolean flag;

    @Column(name = "active")
    private boolean active = false;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    public LeaveRequest(Integer id,
                        String fromDate,
                        String toDate,
                        String leaveType,
                        String reason,
                        int duration) throws ParseException {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.leaveType = leaveType;
        this.reason = reason;
        this.duration = duration;
        this.pending = true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
