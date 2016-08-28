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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

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
            openBluetooth();
        }
    }

    @Override
    public void openBluetooth() {
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
        try {
            Method method = BluetoothDevice.class.getMethod("createBond");
            method.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void disConnect(BluetoothDevice device) {
        try {
            Method method = BluetoothDevice.class.getMethod("removeBond");
            method.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 注册广播接收器，接收并处理搜索结果
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
        Set<BluetoothDevice> deviceSet = mBluetoothAdapter.getBondedDevices();
        mView.addBluetoothDevice(new ArrayList<>(deviceSet));
    }


    @Override
    public void stop() {
        mContext.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void destroy() {
        mContext = null;
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int beforStatus;
            int status;
            BluetoothDevice device;
            Log.i(TAG, "onReceive: action = " + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(TAG, "onReceive: device = " + device.getBluetoothClass().getMajorDeviceClass());
                device.getBondState();
                mView.addBluetoothDevice(device);
            } else if (TextUtils.equals(BluetoothAdapter.ACTION_STATE_CHANGED, action)) {
                status = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_ON);
                beforStatus = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, BluetoothAdapter.STATE_ON);
                if (beforStatus == BluetoothAdapter.STATE_TURNING_OFF && status == BluetoothAdapter.STATE_OFF) {
                    checkBluetooth();
                }
            } else if (TextUtils.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED, action)) {
                mView.showSearching(false);
            } else if (TextUtils.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED, action)) {
                mView.updateBluetoothDeviceStatus();
                beforStatus = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.BOND_NONE);
                status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
                if (beforStatus == BluetoothDevice.BOND_BONDING && status == BluetoothDevice.BOND_BONDED) {
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mView.goPrint(device);
                }
            }
        }
    };


}
