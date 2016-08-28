package com.brightyu.printer.modules.printer.list;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.brightyu.printer.modules.base.BasePresenter;
import com.brightyu.printer.modules.base.BaseView;

import java.util.List;

/**
 * 蓝牙设备列表
 */
public class PrinterListContract {

    interface View extends BaseView<Presenter> {

        /**
         * 是否显示正在搜索
         */
        void showSearching(boolean show);

        /**
         * 添加设备
         */
        void addBluetoothDevice(BluetoothDevice device);

        /**
         * 添加设备
         */
        void addBluetoothDevice(List<BluetoothDevice> devices);

        /**
         * 更新绑定列表
         */
        void updateBluetoothDeviceStatus();

        /**
         * 前往打印界面
         */
        void goPrint(BluetoothDevice device);
    }

    interface Presenter extends BasePresenter {

        void checkBluetooth();

        void openBluetooth();

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
