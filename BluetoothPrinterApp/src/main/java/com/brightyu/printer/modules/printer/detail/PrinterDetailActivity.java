package com.brightyu.printer.modules.printer.detail;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.TextView;

import com.brightyu.printer.R;
import com.brightyu.printer.modules.base.AppBaseActivity;

/**
 * 打印机详情界面
 */
public class PrinterDetailActivity extends AppBaseActivity {
    private static final String TAG = "PrinterDetailActivity";
    public static final String KEY_DEVICE = "key_device";
    private BluetoothDevice mBluetoothDevice;
    private TextView mName;
    private TextView mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_detail);

        mName.setText(mBluetoothDevice.getName());
        mAddress.setText(mBluetoothDevice.getAddress());
    }

    @Override
    protected void initNecessaryData() {
        super.initNecessaryData();
        mBluetoothDevice = getIntent().getParcelableExtra(KEY_DEVICE);
    }

    @Override
    protected void initViews() {
        super.initViews();
        mName = (TextView) findViewById(R.id.name);
        mAddress = (TextView) findViewById(R.id.address);
    }
}
