package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import com.larksuite.appframework.sdk.core.protocol.client.BaseClientTest;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/20
 */
@Slf4j
public class CalendarClientTest extends BaseClientTest {
    static CalendarClient calendarClient;
    String calendarId = System.getenv("calendar_id");
    String openId = System.getenv("open_id");

    @BeforeClass
    public static void setup() {
        calendarClient = openApiClient.calendarClient();
    }

    @Test
    public void testCalendarList() throws LarkClientException {
        CalendarListRequest request = new CalendarListRequest();
        CalendarListResponse response = calendarClient.calendarList(tenantAccessToken, request);
        log.info("{}", response);
        Assert.assertNotNull(response);
    }


    @Test
    public void testAdd() throws LarkClientException {
        CalendarRequest request = new CalendarRequest();
        request.setSummary("test");
        CalendarResponse response = calendarClient.createCalendar(tenantAccessToken, request);
        log.info("{}", response);
        Assert.assertNotNull(response);

    }

    @Test
    public void testUpdate() throws LarkClientException {
        CalendarUpdateRequest request = new CalendarUpdateRequest();
        request.setSummary("update");
        CalendarResponse response = calendarClient.updateCalendar(tenantAccessToken, calendarId, request);
        log.info("{}", response);
        Assert.assertNotNull(response);

    }

    @Test
    public void testDelete() throws LarkClientException {
        BaseResponse response = calendarClient.deleteCalendar(tenantAccessToken, calendarId);
        log.info("{}", response);
        Assert.assertNotNull(response);

    }

    @Test
    public void testControlList() throws LarkClientException {
        ControlListResponse response = calendarClient.controlList(calendarId, tenantAccessToken);
        log.info("{}", response);
        Assert.assertNotNull(response);

    }


    @Test
    public void testCreateCalendar() throws LarkClientException {
        CreateControlRequest request = new CreateControlRequest();
        ControlListResponse.Scope scope = new ControlListResponse.Scope();
        scope.setType("user");
        scope.setOpenId(openId);
        request.setRole("writer");
        request.setScope(scope);
        BaseResponse response = calendarClient.createControl(tenantAccessToken, calendarId, request);
        log.info("{}", response);
        Assert.assertNotNull(response);

    }


    @Test
    public void testDel() throws LarkClientException {
        String ruleId = "";
        DeleteControlRequest request = new DeleteControlRequest();
        request.setCalendarId(calendarId);
        request.setRoleId(ruleId);
        BaseResponse response = calendarClient.delControl(tenantAccessToken, request);
        log.info("{}", response);
        Assert.assertNotNull(response);

    }

    @Test
    public void testBustStatus() throws LarkClientException {
        CalendarBusyRequest request = new CalendarBusyRequest();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("id", calendarId);
        list.add(map);
        request.setItems(list);
        request.setTimeMax(new Date());
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -7);
        request.setTimeMin(calendar.getTime());
        CalendarBusyResponse response = calendarClient.busyCalendar(tenantAccessToken, request);
        log.info("{}", response);
        Assert.assertNotNull(response);
    }
}