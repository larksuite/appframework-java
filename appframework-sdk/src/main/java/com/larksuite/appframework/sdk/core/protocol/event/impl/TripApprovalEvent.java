/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol.event.impl;


import com.larksuite.appframework.sdk.annotation.Event;
import com.larksuite.appframework.sdk.core.protocol.event.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@Event(type = "trip_approval")
public class TripApprovalEvent extends BaseEvent {

    private String instanceCode;
    private String employeeId;
    private Long startTime;
    private Long endTime;

    private List<Schedule> schedules;
    private Integer tripInterval;
    private String tripReason;
    private List<String> tripPeers;

    @Setter
    @Getter
    @ToString
    public static class Schedule {
        private String tripStartTime;
        private String tripEndTime;
        private Integer tripInterval;
        private String departure;
        private String destination;
        private String transportation;
        private String tripType;
        private String remark;
    }
}
