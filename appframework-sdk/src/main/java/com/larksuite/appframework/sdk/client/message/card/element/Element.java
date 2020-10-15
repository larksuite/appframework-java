/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.element;

import com.larksuite.appframework.sdk.client.message.card.AbstractTagged;
import com.larksuite.appframework.sdk.client.message.card.CardComponent;

public abstract class Element extends AbstractTagged implements CardComponent {
    public Element(){}
    public Element(String tag) {
        super(tag);
    }
}
