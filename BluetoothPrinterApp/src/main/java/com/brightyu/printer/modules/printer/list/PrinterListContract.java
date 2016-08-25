package com.brightyu.printer.modules.printer.list;

import android.bluetooth.BluetoothDevice;

import com.brightyu.printer.modules.base.BasePresenter;
import com.brightyu.printer.modules.base.BaseView;

/**
 * 蓝牙设备列表
 */
public class PrinterListContract {
    interface View extends BaseView<Presenter> {

        void showSearching(boolean show);

        void addBluetoothDevice(BluetoothDevice device);

        void needOpenBluetooth();
    }

    interface Presenter extends BasePresenter {
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
    }
}
