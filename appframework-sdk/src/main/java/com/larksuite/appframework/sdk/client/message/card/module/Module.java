/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.module;

import com.larksuite.appframework.sdk.client.message.card.AbstractTagged;
import com.larksuite.appframework.sdk.client.message.card.CardComponent;

public abstract class Module extends AbstractTagged implements CardComponent {

    public Module(String tag) {
        super(tag);
    }
}
