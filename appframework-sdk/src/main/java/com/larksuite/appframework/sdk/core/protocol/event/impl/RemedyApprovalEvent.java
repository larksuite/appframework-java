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

@Setter
@Getter
@ToString
@Event(type = "remedy_approval")
public class RemedyApprovalEvent extends BaseEvent {

    private String instanceCode;
    private String employeeId;
    private Long startTime;
    private Long endTime;
    private String remedyTime;
    private String remedyReason;
}
