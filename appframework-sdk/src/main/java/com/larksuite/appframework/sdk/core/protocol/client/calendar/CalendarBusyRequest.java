package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseRequest;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zht
 */
@Data
public class CalendarBusyRequest extends BaseRequest {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "GMT+8")
    @JsonProperty("time_min")
    private Date timeMin;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "GMT+8")
    @JsonProperty("time_max")
    private Date timeMax;
    private List<Map<String,String>> items;
}
