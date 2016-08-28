/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brightyu.printer.entries;

import android.text.TextUtils;

import com.bright.common.utils.StringUtils;
import com.bright.common.utils.Utils;

/**
 * 设备
 */
public class Device {
    public String name;
    public String formatName;
    public String address;
    public int type;
    public int status;

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public int getType() {
        return type;
    }

    public String getFlag() {
        if (TextUtils.isEmpty(address)) {
            return Utils.EMPTY;
        }

        String[] addresss = address.split(":");
        int length = addresss.length;
        if (length >= 2) {
            return StringUtils.plusString("(", addresss[length - 2], addresss[length - 1], ")");
        }

        return StringUtils.plusString("(", addresss, ")");
    }

    public void genFormatName() {
        formatName = StringUtils.plusString(getName(), getFlag());
    }

    public String getFormatName() {
        return formatName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Device)) {
            return false;
        }

        Device device = (Device) o;
        return TextUtils.equals(device.address, address);
    }
}
