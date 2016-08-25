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

package com.brightyu.printer.modules.printer.list;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bright.common.widget.TopBar;
import com.bright.common.widget.YToast;
import com.brightyu.printer.R;
import com.brightyu.printer.modules.base.AppBaseActivity;

public class PrinterListActivity extends AppBaseActivity implements PrinterListContract.View, View.OnClickListener {
    private static final String TAG = "PrinterListActivity";
    public static final int REQUEST_BT_STATUS = 1;
    private PrinterListContract.Presenter mPresenter;
    private PrinterListAdapter mAdapter;
    private ImageView mWifiImageStatus;
    private TextView mWifiTextStatus;
    private long mLastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new PrinterListPresenter(this, this);
        mPresenter.start();
    }

    @Override
    protected void initViews() {
        super.initViews();
        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);

        mWifiImageStatus = (ImageView) findViewById(R.id.wifi_status);
        mWifiTextStatus = (TextView) findViewById(R.id.search);
        mWifiTextStatus.setOnClickListener(this);

        mAdapter = new PrinterListAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.checkBluetooth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_BT_STATUS:
                mPresenter.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastTime < 2000) {
            super.onBackPressed();
        } else {
            YToast.show(this, R.string.click_again_to_exit);
            mLastTime = nowTime;
        }
    }

    @Override
    public void showSearching(boolean show) {
        AnimationDrawable animation = (AnimationDrawable) mWifiImageStatus.getDrawable();
        if (show) {
            mWifiTextStatus.setText(R.string.searching);
            mWifiTextStatus.setClickable(false);
            animation.start();
        } else {
            mWifiTextStatus.setText(R.string.search);
            mWifiTextStatus.setClickable(true);
            animation.stop();
        }
    }

    @Override
    public void addBluetoothDevice(BluetoothDevice device) {
        mAdapter.plusData(device);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                mPresenter.searchDevice();
                break;
        }
    }
}
