/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card;

public enum Layout {

    BISECTED,
    TRISECTION,
    FLOW,
    ;

    public String asValue() {
        return this.name().toLowerCase();
    }
}
