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

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bright.common.widget.TopBar;
import com.bright.common.widget.YToast;
import com.bright.common.widget.dialog.BaseDialog;
import com.brightyu.printer.R;
import com.brightyu.printer.modules.base.AppBaseActivity;
import com.brightyu.printer.modules.printer.detail.PrinterDetailActivity;

import java.util.List;

public class PrinterListActivity extends AppBaseActivity implements PrinterListContract.View, View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "PrinterListActivity";
    /**
     * 蓝牙状态回调
     */
    public static final int REQUEST_BT_STATUS = 1;
    /**
     * 请求蓝牙权限
     */
    public static final int REQUEST_PERMISSION_BT = 2;
    private PrinterListContract.Presenter mPresenter;
    private PrinterListAdapter mAdapter;
    private ImageView mWifiImageStatus;
    private TextView mWifiTextStatus;
    private long mLastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
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
        listView.setOnItemClickListener(this);
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
            mAdapter.clear();
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
    public void addBluetoothDevice(List<BluetoothDevice> devices) {
        mAdapter.plusData(devices);
    }

    @Override
    public void updateBluetoothDeviceStatus() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void goPrint(BluetoothDevice device) {
        Intent intent = new Intent(this, PrinterDetailActivity.class);
        intent.putExtra(PrinterDetailActivity.KEY_DEVICE, device);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                mPresenter.searchDevice();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: ");
        BluetoothDevice device = mAdapter.getItem(position);
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            goPrint(device);
        } else {
            mPresenter.connect(device);
        }
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 提示权限已经被禁用 且不在提示
                dialog(R.string.please_open_local_permission);
                return;
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_BT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_BT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO 请求权限成功
                } else {
                    // 提示权限已经被禁用
                    dialog(R.string.please_open_local_permission);
                    BaseDialog dialog = new BaseDialog.Builder(PrinterListActivity.this)
                            .setMessage(R.string.please_open_local_permission)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(PrinterListActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_BT);
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .create();
                    dialog.show();

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
