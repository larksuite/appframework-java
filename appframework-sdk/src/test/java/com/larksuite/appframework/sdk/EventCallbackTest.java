/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.core.App;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.auth.AppTicketStorage;
import com.larksuite.appframework.sdk.core.auth.TokenCenter;
import com.larksuite.appframework.sdk.core.eventhandler.AppEventHandlerManager;
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AddBotEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AddUserToChatEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AppOpenEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AppStatusChangeEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.ApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.ChatDisbandEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.CreateWidgetInstanceEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.DeleteWidgetInstanceEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.GroupSettingUpdateEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.LeaveApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.MessageEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.OrderPaidEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.P2pChatCreateEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.RemedyApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.RemoveBotEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.ShiftApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.TripApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.UserAddEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.WorkApprovalEvent;
import com.larksuite.appframework.sdk.utils.Constants;
import com.larksuite.appframework.sdk.utils.MockHttpServletRequest;
import com.larksuite.appframework.sdk.utils.MockOpenApiClient;
import com.larksuite.appframework.sdk.client.message.card.Card;
import com.larksuite.appframework.sdk.utils.MockAppTicketStorage;
import com.larksuite.appframework.sdk.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EventCallbackTest {

    static InstanceContext instanceContext;

    static AppTicketStorage appTicketStorage = new MockAppTicketStorage();

    @BeforeAll
    public static void buildInstanceContext() {
        OpenApiClient openApiClient = new MockOpenApiClient(null, null);

        App app = new App(Constants.APP_NAME, Constants.APP_ID, Constants.APP_SECRET, null, "lPIl3TCujWxoxOaqc9SNXXyPOAIpXBd8", true);

        InstanceContext ic = new InstanceContext();
        ic.setApp(app);
        ic.setTokenCenter(new TokenCenter(openApiClient, ic.getApp(), appTicketStorage));

        new LarkClient(ic, openApiClient);

        instanceContext = ic;
    }


    @Test
    public void testAppOpenEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(AppOpenEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getAppId(), "cli_xxx");
            Assertions.assertEquals(e.getTenantKey(), "xxx");
            Assertions.assertEquals(e.getAppId(), "cli_xxx");

            {
                Assertions.assertEquals(e.getApplicants().size(), 1);
                Assertions.assertEquals(e.getApplicants().get(0).getOpenId(), "ou_2dc19f54146a87e869c842d8f14d077a");
                Assertions.assertEquals(e.getApplicants().get(0).getUserId(), "de6fegf4");
            }

            {
                Assertions.assertEquals(e.getInstaller().getOpenId(), "ou_78cad46d30530b72f7fcbdee51e6b4b1");
                Assertions.assertEquals(e.getInstaller().getUserId(), "b78cfg77");
            }

            return "{}";
        });

        invokeReceive(m, "event-json/AppEnabledEvent.json");
    }


    @Test
    public void testApprovalEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(ApprovalEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getDefinitionCode(), "xxx");
            Assertions.assertEquals(e.getDefinitionName(), "xxx");
            Assertions.assertEquals(e.getInstanceCode(), "xxx");
            Assertions.assertEquals(e.getStartTime(), 1502199207);
            Assertions.assertEquals(e.getEndTime(), 1502199307);
            return "{}";
        });

        invokeReceive(m, "event-json/ApprovalEvent.json");
    }

    @Test
    public void testAppStatusChangeEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(AppStatusChangeEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getStatus(), "start_by_tenant");
            return "{}";
        });

        invokeReceive(m, "event-json/AppStatusChangeEvent.json");
    }

    @Test
    public void testAddBotEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(AddBotEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getChatName(), "接口修改");
            Assertions.assertEquals(e.getChatOwnerEmployeeId(), "ca51d83b");
            Assertions.assertEquals(e.getChatOwnerName(), "fanlv");
            Assertions.assertEquals(e.getChatOwnerOpenId(), "ou_18eac85d35a26f989317ad4f02e8bbbb");
            Assertions.assertEquals(e.getOpenChatId(), "oc_e520983d3e4f5ec7306069bffe672aa3");
            Assertions.assertEquals(e.getOperatorEmployeeId(), "ca51d83b");
            Assertions.assertEquals(e.getOperatorName(), "fanlv");
            Assertions.assertEquals(e.getOperatorOpenId(), "ou_18eac85d35a26f989317ad4f02e8bbbb");
            Assertions.assertEquals(e.getOwnerIsBot(), false);

            Assertions.assertEquals(e.getChatI18nNames().getEnUs(), "英文标题1");
            Assertions.assertEquals(e.getChatI18nNames().getZhCn(), "中文标题");

            return "{}";
        });

        invokeReceive(m, "event-json/BotInvitedEvent.json");
    }

    @Test
    public void testRemoveBotEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(RemoveBotEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getChatName(), "接口修改");
            Assertions.assertEquals(e.getChatOwnerEmployeeId(), "ca51d83b");
            Assertions.assertEquals(e.getChatOwnerName(), "fanlv");
            Assertions.assertEquals(e.getChatOwnerOpenId(), "ou_18eac85d35a26f989317ad4f02e8bbbb");
            Assertions.assertEquals(e.getOpenChatId(), "oc_e520983d3e4f5ec7306069bffe672aa3");
            Assertions.assertEquals(e.getOperatorEmployeeId(), "ca51d83b");
            Assertions.assertEquals(e.getOperatorName(), "fanlv");
            Assertions.assertEquals(e.getOperatorOpenId(), "ou_18eac85d35a26f989317ad4f02e8bbbb");
            Assertions.assertEquals(e.getOwnerIsBot(), false);

            Assertions.assertEquals(e.getChatI18nNames().getEnUs(), "英文标题1");
            Assertions.assertEquals(e.getChatI18nNames().getZhCn(), "中文标题");

            return "{}";
        });

        invokeReceive(m, "event-json/BotRemovedEvent.json");
    }

    @Test
    public void testUserAddEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(UserAddEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getOpenId(), "xxx");
            Assertions.assertEquals(e.getEmployeeId(), "xxx");
            return "{}";
        });

        invokeReceive(m, "event-json/ContactsUpdatesEvent.json");
    }

    @Test
    public void testLeaveApprovalEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(LeaveApprovalEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getInstanceCode(), "xxx");
            Assertions.assertEquals(e.getEmployeeId(), "xxx");
            Assertions.assertEquals(e.getStartTime(), 1502199207);
            Assertions.assertEquals(e.getEndTime(), 1502199307);
            Assertions.assertEquals(e.getLeaveType(), "xxx");
            Assertions.assertEquals(e.getLeaveUnit(), "1");
            Assertions.assertEquals(e.getLeaveStartTime(), "2018-12-01 12:00:00");
            Assertions.assertEquals(e.getLeaveEndTime(), "2018-12-02 12:00:00");
            Assertions.assertEquals(e.getLeaveInterval(), 7200);
            Assertions.assertEquals(e.getLeaveReason(), "xxx");

            return "{}";
        });

        invokeReceive(m, "event-json/LeaveApprovalEvent.json");
    }

    @Test
    public void testMessageEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(MessageEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getRootId(), "");
            Assertions.assertEquals(e.getParentId(), "");
            Assertions.assertEquals(e.getOpenChatId(), "oc_5ce6d572455d361153b7cb51da133945");
            Assertions.assertEquals(e.getChatType(), "private");
            Assertions.assertEquals(e.getMsgType(), "text");
            Assertions.assertEquals(e.getOpenId(), "ou_18eac85d35a26f989317ad4f02e8bbbb");
            Assertions.assertEquals(e.getOpenMessageId(), "om_36686ee62209da697d8775375d0c8e88");
            Assertions.assertEquals(e.getIsMention(), false);
            Assertions.assertEquals(e.getText(), "消息内容");
            Assertions.assertEquals(e.getTextWithoutAtBot(), "消息内容");

            return "{}";
        });

        invokeReceive(m, "event-json/MessageEvent.json");
    }

    @Test
    public void testOrderPaidEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(OrderPaidEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getOrderId(), "6704894492631105539");
            Assertions.assertEquals(e.getPricePlanId(), "price_9d86fa1333b8110c");
            Assertions.assertEquals(e.getPricePlanType(), "per_seat_per_month");
            Assertions.assertEquals(e.getSeats(), 20);
            Assertions.assertEquals(e.getBuyCount(), 1);
            Assertions.assertEquals(e.getCreateTime(), 1502199207);
            Assertions.assertEquals(e.getPayTime(), "1502199209");
            Assertions.assertEquals(e.getBuyType(), "buy");
            Assertions.assertEquals(e.getSrcOrderId(), "6704894492631105539");
            Assertions.assertEquals(e.getOrderPayPrice(), 10000);

            return "{}";
        });

        invokeReceive(m, "event-json/OrderPaidEvent.json");
    }

    @Test
    public void testWorkApprovalEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(WorkApprovalEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getInstanceCode(), "xxx");
            Assertions.assertEquals(e.getEmployeeId(), "xxx");
            Assertions.assertEquals(e.getStartTime(), 1502199207);
            Assertions.assertEquals(e.getEndTime(), 1502199307);
            Assertions.assertEquals(e.getWorkType(), "xxx");
            Assertions.assertEquals(e.getWorkStartTime(), "2018-12-01 12:00:00");
            Assertions.assertEquals(e.getWorkEndTime(), "2018-12-02 12:00:00");
            Assertions.assertEquals(e.getWorkInterval(), 7200);
            Assertions.assertEquals(e.getWorkReason(), "xxx");

            return "{}";
        });

        invokeReceive(m, "event-json/OvertimeApprovalEvent.json");
    }

    @Test
    public void testP2pChatCreateEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(P2pChatCreateEvent.class, (c, e) -> {


            Assertions.assertEquals(e.getChatId(), "oc_26b66a5eb603162b849f91bcd8815b20");
            Assertions.assertEquals(e.getOperator().getUserId(), "gfa21d92");
            Assertions.assertEquals(e.getOperator().getOpenId(), "ou_2d2c0399b53d06fd195bb393cd1e38f2");


            Assertions.assertEquals(e.getUser().getUserId(), "gfa21d92");
            Assertions.assertEquals(e.getUser().getOpenId(), "ou_7dede290d6a27698b969a7fd70ca53da");
            Assertions.assertEquals(e.getUser().getName(), "张三");

            return "{}";
        });

        invokeReceive(m, "event-json/P2pChatCreateEvent.json");
    }

    @Test
    public void testRemedyApprovalEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(RemedyApprovalEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getInstanceCode(), "xxx");
            Assertions.assertEquals(e.getEmployeeId(), "xxx");
            Assertions.assertEquals(e.getStartTime(), 1502199207);
            Assertions.assertEquals(e.getEndTime(), 1502199307);
            Assertions.assertEquals(e.getRemedyReason(), "xxx");
            Assertions.assertEquals(e.getRemedyTime(), "2018-12-01 12:00:00");

            return "{}";
        });

        invokeReceive(m, "event-json/RemedyApprovalEvent.json");
    }

    @Test
    public void testShiftApprovalEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(ShiftApprovalEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getInstanceCode(), "xxx");
            Assertions.assertEquals(e.getEmployeeId(), "xxx");
            Assertions.assertEquals(e.getStartTime(), 1502199207);
            Assertions.assertEquals(e.getEndTime(), 1502199307);
            Assertions.assertEquals(e.getShiftReason(), "xxx");
            Assertions.assertEquals(e.getShiftTime(), "2018-12-01 12:00:00");
            Assertions.assertEquals(e.getReturnTime(), "2018-12-02 12:00:00");

            return "{}";
        });

        invokeReceive(m, "event-json/ShiftApprovalEvent.json");
    }

    @Test
    public void testTripApprovalEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(TripApprovalEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getInstanceCode(), "xxx");
            Assertions.assertEquals(e.getEmployeeId(), "xxx");
            Assertions.assertEquals(e.getStartTime(), 1502199207);
            Assertions.assertEquals(e.getEndTime(), 1502199307);

            {
                Assertions.assertEquals(e.getSchedules().size(), 1);
                Assertions.assertEquals(e.getSchedules().get(0).getTripStartTime(), "2018-12-01 12:00:00");
                Assertions.assertEquals(e.getSchedules().get(0).getTripEndTime(), "2018-12-01 12:00:00");
                Assertions.assertEquals(e.getSchedules().get(0).getTripInterval(), 3600);
                Assertions.assertEquals(e.getSchedules().get(0).getDeparture(), "xxx");
                Assertions.assertEquals(e.getSchedules().get(0).getDestination(), "xxx");
                Assertions.assertEquals(e.getSchedules().get(0).getTransportation(), "xxx");
                Assertions.assertEquals(e.getSchedules().get(0).getTripType(), "单程");
                Assertions.assertEquals(e.getSchedules().get(0).getRemark(), "备注");
            }

            Assertions.assertEquals(e.getTripInterval(), 3600);
            Assertions.assertEquals(e.getTripReason(), "xxx");
            assertIterableEquals(e.getTripPeers(), Lists.newArrayList("xxx", "yyy"));

            return "{}";
        });

        invokeReceive(m, "event-json/TripApprovalEvent.json");
    }

    @Test
    public void testAddUserToChatEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(AddUserToChatEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getChatId(), "oc_9e9619b938c9571c1c3165681cdaead5");
            Assertions.assertEquals(e.getOperator().getOpenId(), "ou_18eac85d35a26f989317ad4f02e8bbbb");
            Assertions.assertEquals(e.getOperator().getUserId(), "ca51d83b");

            Assertions.assertEquals(e.getUsers().get(0).getName(), "James");
            Assertions.assertEquals(e.getUsers().get(0).getOpenId(), "ou_706adeb944ab1473b9fb3e7da2a40b68");
            Assertions.assertEquals(e.getUsers().get(0).getUserId(), "51g97a4g");
            Assertions.assertEquals(e.getUsers().get(1).getName(), "张三");
            Assertions.assertEquals(e.getUsers().get(1).getOpenId(), "ou_7885357f9922aaa34001b190109e0b48");
            Assertions.assertEquals(e.getUsers().get(1).getUserId(), "6e125386");

            return "{}";
        });

        invokeReceive(m, "event-json/AddUserToChatEvent.json");
    }

    @Test
    public void testChatDisbandEvent() {

        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(ChatDisbandEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getChatId(), "oc_9f2df3c095c9395334bb6e70ced0fa83");
            Assertions.assertEquals(e.getOperator().getOpenId(), "ou_18eac85d35a26f989317ad4f02e8bbbb");
            Assertions.assertEquals(e.getOperator().getUserId(), "ca51d83b");

            return "{}";
        });

        invokeReceive(m, "event-json/ChatDisbandEvent.json");
    }

    @Test
    public void testGroupSettingUpdateEvent_1() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(GroupSettingUpdateEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getChatId(), "oc_a080495ca4c3748da1cb38f681f48d6a");
            Assertions.assertEquals(e.getOperator().getOpenId(), "ou_18eac85d35a26f989317ad4f02e8bbbb");
            Assertions.assertEquals(e.getOperator().getUserId(), "ca51d83b");

            Assertions.assertEquals(e.getAfterChange().getMessageNotification(), false);
            Assertions.assertEquals(e.getBeforeChange().getMessageNotification(), true);

            return "{}";
        });

        invokeReceive(m, "event-json/GroupSettingUpdateEvent_1.json");
    }

    @Test
    public void testGroupSettingUpdateEvent_2() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(GroupSettingUpdateEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getAfterChange().getAddMemberPermission(), "everyone");
            Assertions.assertEquals(e.getBeforeChange().getAddMemberPermission(), "owner");

            return "{}";
        });

        invokeReceive(m, "event-json/GroupSettingUpdateEvent_2.json");
    }

    @Test
    public void testGroupSettingUpdateEvent_3() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(GroupSettingUpdateEvent.class, (c, e) -> {

            Assertions.assertEquals(e.getAfterChange().getOwnerOpenId(), "ou_18eac85d35a26f989317ad4f02e8bbbb");
            Assertions.assertEquals(e.getAfterChange().getOwnerUserId(), "ca51d83b");
            Assertions.assertEquals(e.getBeforeChange().getOwnerUserId(), null);
            Assertions.assertEquals(e.getBeforeChange().getOwnerOpenId(), "ou_2d2c0399b53d06fd195bb393cd1e38f2");

            return "{}";
        });

        invokeReceive(m, "event-json/GroupSettingUpdateEvent_3.json");
    }

    @Test
    public void testCreateWidgetInstance() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(CreateWidgetInstanceEvent.class, (c, e) -> {
            Assertions.assertIterableEquals(e.getInstanceId(), Lists.newArrayList("b21d41d9-5aeb-4efc-8b7e-a9d6feede8ee"));
            return "{}";
        });

        invokeReceive(m, "event-json/CreateWidgetInstance.json");
    }

    @Test
    public void testDeleteWidgetInstance() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onEvent(DeleteWidgetInstanceEvent.class, (c, e) -> {
            Assertions.assertIterableEquals(e.getInstanceId(), Lists.newArrayList("b21d41d9-5aeb-4efc-8b7e-a9d6feede8ee"));
            return "{}";
        });

        invokeReceive(m, "event-json/DeleteWidgetInstance.json");
    }

    @Test
    public void testAppTicketEvent() {
        assertEquals("", new LarkAppInstance(instanceContext).receiveLarkNotify(TestUtils.loadJsonFile("event-json/AppTicketEvent.json")));
        assertEquals("xxx", appTicketStorage.loadAppTicket(Constants.APP_NAME, "cli_xxx"));
    }


    @Test
    public void testCardEvent() {
        AppEventListener m = LarkAppInstanceFactory.createAppEventListener();
        m.onCardEvent((c, e) -> {

            Assertions.assertEquals(e.getOpenId(), "ou_sdfimx9948345");
            Assertions.assertEquals(e.getOpenMessageId(), "om_abcdefg1234567890");
            Assertions.assertEquals(e.getUserId(), "xxx");
            Assertions.assertEquals(e.getAction().getOption(), null);
            Assertions.assertEquals(e.getAction().getTag(), "button");
            Assertions.assertEquals(e.getAction().getValue().size(), 1);
            Assertions.assertEquals(e.getAction().getValue().get("key"), "value");

            return new Card(null, null);
        });

        HttpServletRequest request = new MockHttpServletRequest() {

            Map<String, String> header = ImmutableMap.of(
                    "X-Lark-Request-Timestamp", "Tuesday, 22-Oct-19 10:52:31 CST",
                    "X-Lark-Request-Nonce", "a04f15a0-5cc4-433b-a971-e0aa562cd942",
                    "X-Lark-Signature", "9ca669aecfead5beaaee3dcef0e6e9472f0094f4"
            );

            @Override
            public String getHeader(String s) {
                return header.get(s);
            }
        };

        assertTrue(TestUtils.jsonEquals("{}", new LarkAppInstance(instanceContext).setAppEventListener(m).receiveCardNotify(TestUtils.loadJsonFile("card-json/button.json"), request)));
    }

    private void invokeReceive(AppEventListener m, String jsonFile) {
        assertEquals(new LarkAppInstance(instanceContext).setAppEventListener(m).receiveLarkNotify(TestUtils.loadJsonFile(jsonFile)), "{}");
    }
}
