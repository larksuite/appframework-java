package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class EventResponse extends BaseResponse {

    private DataItem data;

    @Data
    public static class DataItem {
        private String id;

        private String summary;

        private String description;

        private String visibility;

        private Start start;

        private End end;

        private List<Attendees> attendees;
    }


    @Data
    public static class Start {
        private String date;

        @JsonProperty("time_zone")
        private String timeZone;
    }

    @Data
    public static class End {
        private String date;

        @JsonProperty("time_zone")
        private String timeZone;
    }

    @Data
    public static class Attendees {
        @JsonProperty("open_id")
        private String openId;

        @JsonProperty("employee_id")
        private String employeeId;

        private Boolean optional;

        @JsonProperty("display_name")
        private String displayName;
    }
}
