package com.brightyu.printer.modules.printer.list;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.brightyu.printer.modules.base.BasePresenter;
import com.brightyu.printer.modules.base.BaseView;

/**
 * 蓝牙设备列表
 */
public class PrinterListContract {

    interface View extends BaseView<Presenter> {

        void showSearching(boolean show);

        void addBluetoothDevice(BluetoothDevice device);
    }

    interface Presenter extends BasePresenter {

        void checkBluetooth();

        void openBluetook();

        /**
         * 搜索设备
         */
        void searchDevice();

        /**
         * 连接设备
         */
        void connect(BluetoothDevice device);

        /**
         * 断开连接设备
         */
        void disConnect(BluetoothDevice device);

        /**
         * 处理蓝牙事件
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);

        void stop();
    }
}
