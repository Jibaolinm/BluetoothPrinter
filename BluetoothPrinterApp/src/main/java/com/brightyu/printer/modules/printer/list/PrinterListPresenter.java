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

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.brightyu.printer.modules.base.AppBaseActivity;

/**
 * 打印机列表
 */
public class PrinterListPresenter implements PrinterListContract.Presenter {
    private static final String TAG = "PrinterListPresenter";
    private PrinterListContract.View mView;
    private AppBaseActivity mContext;
    private BluetoothAdapter mBluetoothAdapter;

    public PrinterListPresenter(PrinterListContract.View view, AppBaseActivity activity) {
        Log.i(TAG, "PrinterListPresenter: ");
        mView = view;
        mContext = activity;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void checkBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            openBluetook();
        }
    }

    @Override
    public void openBluetook() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        mContext.startActivityForResult(intent, PrinterListActivity.REQUEST_BT_STATUS);
    }

    @Override
    public void searchDevice() {
        mView.showSearching(true);
        if (mBluetoothAdapter.isDiscovering()) {
            Log.i(TAG, "searchDevice: already searching ");
            return;
        }
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void connect(BluetoothDevice device) {

    }

    @Override
    public void disConnect(BluetoothDevice device) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            mContext.finish();
        } else {
            searchDevice();
        }
    }


    @Override
    public void start() {
        Log.i(TAG, "start: ");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 注册广播接收器，接收并处理搜索结果
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void stop() {
        Log.i(TAG, "start: ");
        mContext.unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int status;
            Log.i(TAG, "onReceive: action = " + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mView.addBluetoothDevice(device);
            } else if (TextUtils.equals(BluetoothAdapter.ACTION_STATE_CHANGED, action)) {
                status = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_ON);
                Log.i(TAG, "onReceive: status - " + status);
            } else if (TextUtils.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED, action)) {
                mView.showSearching(false);
            }
        }
    };

}
