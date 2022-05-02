package com.college.lm.DataSource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PendingLeave {
    private Long l_id;
    private String date_from;
    private String date_to;
    private String appby;
    private String status;
    private Long u_id;
    private String reason;
    private String leave_type;
    private Long did;
    private String message;
    private String first_name;
    private String last_name;
    private String role;
}
