/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bright.common.utils.http.okhttp.builder;

import com.bright.common.utils.http.okhttp.OkHttpUtils;
import com.bright.common.utils.http.okhttp.request.OkHttpRequest;
import com.bright.common.utils.http.okhttp.request.OtherRequest;
import com.bright.common.utils.http.okhttp.request.RequestCall;

public class HeadBuilder extends GetBuilder {
    @Override
    public RequestCall build() {
        OkHttpRequest request = new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers, id);
        request.setLogTag(logTag);
        return request.build();
    }
}