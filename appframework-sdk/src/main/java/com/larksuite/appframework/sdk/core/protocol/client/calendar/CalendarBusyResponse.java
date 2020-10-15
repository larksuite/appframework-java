package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 *
 * @author zht
 */
@Data
public class CalendarBusyResponse extends BaseResponse {
    private BusyData data;
    @Data
    @ToString
    public static class BusyData {
        @JsonProperty("time_min")
        private String timeMin;
        @JsonProperty("time_max")
        private String timeMax;
        private Map<String, Object> items;
    }
}
