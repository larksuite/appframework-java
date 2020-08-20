package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.JsonUtil;

import static com.larksuite.appframework.sdk.core.protocol.OpenApiClient.DEFAULT_CONN_TIMEOUT;
import static com.larksuite.appframework.sdk.core.protocol.OpenApiClient.DEFAULT_READ_TIMEOUT;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/18
 */
public class CalendarClient extends OpenApiClient.AbstractSubClient {

    private static final String GET_CALENDAR = "/open-apis/calendar/v3/calendar_list/%s";
    private static final String LIST_CALENDAR = "/open-apis/calendar/v3/calendar_list";
    private static final String CREATE_CALENDAR = "/open-apis/calendar/v3/calendars";
    private static final String DELETE_CALENDAR = "/open-apis/calendar/v3/calendars/%s";
    private static final String GET_EVENT = "/open-apis/calendar/v3/calendars/%s/events/%s";
    private static final String CREATE_EVENT = "/open-apis/calendar/v3/calendars/%s/events";
    private static final String LIST_EVENT = "/open-apis/calendar/v3/calendars/%s/events";
    private static final String DELETE_EVENT = "/open-apis/calendar/v3/calendars/%s/events/%s";
    private static final String PATCH_EVENT = "/open-apis/calendar/v3/calendars/%s/events/%s";
    private static final String ATTENDEES = "/open-apis/calendar/v3/calendars/%s/events/%s/attendees";
    private static final String CONTROL="/open-apis/calendar/v3/calendars/%s/acl";
    private static final String DEL_CONTROL="/open-apis/calendar/v3/calendars/%s/acl/%s";
    private static final String BUSY_CALENDAR="/open-apis/calendar/v3/freebusy/query";


    public CalendarResponse getCalendar(String tenantAccessToken, String calendarId) throws LarkClientException {
        return call(() -> getHttpClient().doGet(String.format(getBasePath() + GET_CALENDAR, calendarId), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), CalendarResponse.class);
    }

    public CalendarListResponse calendarList(String tenantAccessToken, CalendarListRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(getBasePath() + LIST_CALENDAR, request), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), CalendarListResponse.class);
    }

    public CalendarResponse createCalendar(String tenantAccessToken, CalendarRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + CREATE_CALENDAR, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(request)),
                CalendarResponse.class);
    }

    public BaseResponse deleteCalendar(String tenantAccessToken, String calendarId) throws LarkClientException {
        return call(() -> getHttpClient().doDelete(String.format(getBasePath() + DELETE_CALENDAR, calendarId), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), BaseResponse.class);
    }


    public CalendarResponse createCalendar(String tenantAccessToken, String calendarId, CalendarUpdateRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doPatch(String.format(getBasePath() + DELETE_CALENDAR, calendarId), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT,
                createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(request)), CalendarResponse.class);
    }

    public EventResponse getEvents(String tenantAccessToken, String calendarId, String eventId) throws LarkClientException {
        return call(() -> getHttpClient().doGet(String.format(getBasePath() + GET_EVENT, calendarId, eventId), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), EventResponse.class);
    }

    public EventResponse createEvent(String tenantAccessToken, String calendarId, EventRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(String.format(getBasePath() + CREATE_EVENT, calendarId), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(request)), EventResponse.class);
    }

    public EventResponse listEvent(String tenantAccessToken, String calendarId, ListEventRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(String.format(getBasePath() + LIST_EVENT, calendarId), request), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), EventResponse.class);
    }

    public BaseResponse deleteEvent(String tenantAccessToken, String calendarId, String eventId) throws LarkClientException {
        return call(() -> getHttpClient().doDelete(String.format(getBasePath() + DELETE_EVENT, calendarId, eventId), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), BaseResponse.class);
    }

    public EventResponse updateEvent(String tenantAccessToken, String calendarId, String eventId, EventRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doPatch(String.format(getBasePath() + PATCH_EVENT, calendarId, eventId), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(request)), EventResponse.class);
    }

    public AttendeesResponse attendees(String tenantAccessToken, String calendarId, String eventId, AttendeesRequest request) throws LarkClientException {
        String url = String.format(getBasePath() + ATTENDEES, calendarId, eventId);
        return call(() -> getHttpClient().doPostJson(url, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(request)), AttendeesResponse.class);
    }

    public ControlListResponse controlList(ControlListRequest request) throws LarkClientException {
        String url = String.format(getBasePath()+CONTROL,request.getCalendarId());
        return call(() -> getHttpClient().doGet(buildGetUrl(url, request), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(request.getAuthorization())), ControlListResponse.class);
    }


    public BaseResponse createControl(String tenantAccessToken, String calendarId, CreateControlRequest request) throws LarkClientException {
        String url = String.format(getBasePath()+CONTROL,calendarId);
        return call(() -> getHttpClient().doPostJson(url, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(request)) , BaseResponse.class);
    }


    public BaseResponse delControl(DeleteControlRequest request) throws LarkClientException {
        String url = String.format(getBasePath()+DEL_CONTROL,request.getCalendarId(),request.getRoleId());
        return call(() -> getHttpClient().doDelete(buildGetUrl(url, request), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(request.getAuthorization())) , BaseResponse.class);
    }


    public CalendarBusyResponse busyCalendar(String tenantAccessToken, CalendarBusyRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(BUSY_CALENDAR, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(request)) , CalendarBusyResponse.class);
    }

    protected CalendarClient(OpenApiClient parent) {
        super(parent);
    }
}
