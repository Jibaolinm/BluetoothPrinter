package com.brightyu.printer.modules.printer.list;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bright.common.adapter.ListAdapter;
import com.bright.common.utils.StringUtils;
import com.brightyu.printer.R;

/**
 * 设备列表的Adapter
 */
public class PrinterListAdapter extends ListAdapter<BluetoothDevice, PrinterListAdapter.ViewHolder> {

    public PrinterListAdapter(Context context) {
        super(context);
    }


    /**
     * 增加数据
     */
    public void plusData(BluetoothDevice data) {
        if (data != null && !mData.contains(data)) {
            mData.add(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position, int type) {
        View item = mLayoutInflater.inflate(R.layout.item_printer_list, parent, false);
        return new ViewHolder(item, type);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {
        BluetoothDevice entry = getItem(position);
        bindName(holder, entry);
        bindStatus(holder, entry);
    }

    private void bindName(ViewHolder holder, BluetoothDevice entry) {
        holder.name.setText(getFormatName(entry));
    }

    private void bindStatus(ViewHolder holder, BluetoothDevice entry) {
        switch (entry.getBondState()) {
            case BluetoothDevice.BOND_BONDED:
                holder.iconStatus.setVisibility(View.VISIBLE);
                holder.textStatus.setText(R.string.bt_bonded);
                break;
            case BluetoothDevice.BOND_BONDING:
                holder.iconStatus.setVisibility(View.INVISIBLE);
                holder.textStatus.setText(R.string.bt_bonding);
                break;
            case BluetoothDevice.BOND_NONE:
                holder.iconStatus.setVisibility(View.INVISIBLE);
                holder.textStatus.setText(R.string.bt_unbond);
                break;
        }
    }


    public String getFormatName(BluetoothDevice device) {
        String name = device.getName();
        if (TextUtils.isEmpty(name)) {
            name = mContext.getString(R.string.unknown_device);
        }

        String address = device.getAddress();
        if (TextUtils.isEmpty(address)) {
            return name;
        }

        String[] addressChildren = address.split(":");
        int length = addressChildren.length;
        if (length >= 2) {
            address = StringUtils.plusString(addressChildren[length - 2], addressChildren[length - 1]);
        }

        address = StringUtils.plusString("(", address, ")");

        return StringUtils.plusString(name, address);
    }

    public class ViewHolder extends ListAdapter.Holder {
        private TextView name;
        private TextView textStatus;
        private ImageView iconStatus;

        public ViewHolder(View item, int type) {
            super(item, type);
            name = (TextView) item.findViewById(R.id.name);
            textStatus = (TextView) item.findViewById(R.id.status_text);
            iconStatus = (ImageView) item.findViewById(R.id.status_image);
        }
    }
}
