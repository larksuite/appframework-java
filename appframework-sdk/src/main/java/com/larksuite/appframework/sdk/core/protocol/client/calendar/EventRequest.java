package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.larksuite.appframework.sdk.core.protocol.BaseRequest;
import lombok.Data;

import java.util.List;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/18
 */
@Data
public class EventRequest extends BaseRequest {
    private String summary;

    private String description;

    private String visibility;

    private EventResponse.Start start;

    private EventResponse.End end;

    private List<EventResponse.Attendees> attendees;
}
