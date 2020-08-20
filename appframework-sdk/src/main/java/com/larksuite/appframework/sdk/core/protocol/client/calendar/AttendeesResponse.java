package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
public class AttendeesResponse extends BaseResponse {


    private List<Data> data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        @JsonProperty("open_id")
        private String openId;

        @JsonProperty("employee_id")
        private String employeeId;

        @JsonProperty("display_name")
        private String displayName;

        private Boolean optional;
    }
}
