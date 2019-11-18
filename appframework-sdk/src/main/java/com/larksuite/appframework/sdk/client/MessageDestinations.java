/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client;

public class MessageDestinations {

    public static MessageDestination openId(String id) {
        return new OpenId(id);
    }

    public static MessageDestination chatId(String id) {
        return new ChatId(id);
    }

    public static MessageDestination userId(String id) {
        return new UserId(id);
    }

    public static MessageDestination email(String id) {
        return new Email(id);
    }

    public static class OpenId extends AbstractMessageDestination {

        OpenId(String identity) {
            super(identity);
        }
    }

    public static class ChatId extends AbstractMessageDestination {

        ChatId(String identity) {
            super(identity);
        }
    }

    public static class UserId extends AbstractMessageDestination {

        UserId(String identity) {
            super(identity);
        }
    }

    public static class Email extends AbstractMessageDestination {

        Email(String identity) {
            super(identity);
        }
    }

    private static abstract class AbstractMessageDestination implements MessageDestination {
        private String identity;

        AbstractMessageDestination(String identity) {
            this.identity = identity;
        }

        @Override
        public String identity() {
            return identity;
        }
    }
}
