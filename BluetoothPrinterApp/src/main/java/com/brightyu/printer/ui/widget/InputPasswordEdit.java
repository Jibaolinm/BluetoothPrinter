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

package com.brightyu.printer.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.brightyu.printer.R;


/**
 * 搜索输入框
 */
public class InputPasswordEdit extends InputEdit {
    public InputPasswordEdit(Context context) {
        this(context, null);
    }

    public InputPasswordEdit(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputPasswordEdit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void setCurrentView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.widget_input_password_edit, this, true);
    }
}
