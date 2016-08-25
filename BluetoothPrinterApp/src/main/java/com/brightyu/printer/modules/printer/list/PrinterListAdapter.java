package com.brightyu.printer.modules.printer.list;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bright.common.adapter.ListAdapter;

/**
 * 设备列表的Adapter
 */
public class PrinterListAdapter extends ListAdapter<BluetoothDevice, PrinterListAdapter.ViewHolder> {

    public PrinterListAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position, int type) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {

    }

    public class ViewHolder extends ListAdapter.Holder {

        public ViewHolder(View item, int type) {
            super(item, type);
        }
    }
}
