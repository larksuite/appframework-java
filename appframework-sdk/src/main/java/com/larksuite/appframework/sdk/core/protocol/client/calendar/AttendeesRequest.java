package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseRequest;
import lombok.Data;

import java.util.List;

@Data
public class AttendeesRequest extends BaseRequest {

    private List<Attendees> attendees;

    @Data
    public static class Attendees {
        @JsonProperty("open_id")
        private String openId;

        @JsonProperty("employee_id")
        private String employeeId;

        private String status;

        @JsonProperty("display_name")
        private String displayName;

        private Boolean optional;
    }

}
