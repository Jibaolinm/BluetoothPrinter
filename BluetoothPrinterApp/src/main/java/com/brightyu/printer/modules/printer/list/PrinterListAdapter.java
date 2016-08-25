package com.brightyu.printer.modules.printer.list;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bright.common.adapter.ListAdapter;
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
        holder.name.setText(entry.getName());
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
