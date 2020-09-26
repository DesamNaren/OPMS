package com.cgg.pps.fragment;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.analogics.thermalAPI.Bluetooth_Printer_2inch_ThermalAPI;
import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.ConfigFragmnetBinding;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.util.APrinter;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.cgg.pps.view.DashboardActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

public class ConfigFragment extends Fragment {

    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter mBluetoothAdapter = null;

    private ArrayAdapter<String> btArrayAdapter;
    private ConfigFragmnetBinding binding;
    private PPCUserDetails ppcUserDetails;
    private CustomProgressDialog customProgressDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private APrinter aPrinter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            binding = DataBindingUtil.inflate(inflater, R.layout.config_fragmnet, container, false);
            aPrinter = APrinter.getaPrinter();

            customProgressDialog = new CustomProgressDialog(getActivity());
            getPreferenceData();

            binding.printerSwitch.setChecked(sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false));


            binding.scandevice.setOnClickListener(onBtnClick);
            binding.printerCheckPrinterBtn.setOnClickListener(onBtnClick);

            if (!TextUtils.isEmpty(AppConstants.BT_ADDRESS)) {
                binding.printerBluetoothAddressTv.setText(AppConstants.BT_ADDRESS);
            }


            btArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
            binding.devicesfound.setAdapter(btArrayAdapter);
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            CheckBlueToothState();

            getActivity().registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

            binding.devicesfound.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        String btAddress = null;
                        String btName = null;
                        String selection = (String) (binding.devicesfound.getItemAtPosition(position));
                        if (selection != null && !selection.equalsIgnoreCase("")) {
                            btAddress = selection.substring(0, 17);
                        }

                        if (selection != null && !selection.equalsIgnoreCase("")) {
                            String[] str = selection.split("\n");
                            if (str.length > 0) {
                                btName = str[1];
                            }
                        }
                        if (btAddress != null) {
                            new AsyncBTSaveTask(btAddress, btName).execute();
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong",
                                    Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.printerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor = sharedPreferences.edit();
                if (compoundButton.isChecked()) {
                    editor.putBoolean(AppConstants.PRINTER_CON_FLAG, true);
                } else {
                    editor.putBoolean(AppConstants.PRINTER_CON_FLAG, false);
                }
                editor.commit();
            }
        });

        return binding.getRoot();
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncBTSaveTask extends AsyncTask<Void, Void, Boolean> {

        private String btName, btAddress;

        AsyncBTSaveTask(String btAddress, String btName) {
            this.btName = btName;
            this.btAddress = btAddress;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (customProgressDialog != null && !customProgressDialog.isShowing())
                customProgressDialog.show();
        }

        protected Boolean doInBackground(Void... x) {
            return Utils.saveBTAddress(btAddress, btName);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();
            if (result) {
                binding.printerBluetoothAddressTv.setText(AppConstants.BT_ADDRESS);
                Toast.makeText(getActivity(), getString(R.string.bt_saved), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncBTPrintTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (customProgressDialog != null && !customProgressDialog.isShowing())
                customProgressDialog.show();

        }

        protected Boolean doInBackground(Void... x) {
            return checkPrinterState();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();
            if (!result) {
                Toast.makeText(getActivity(), getResources().getString(R.string.print_alert_not), Toast.LENGTH_SHORT).show();
            }

            try {
                aPrinter.closeBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onResume() {
        super.onResume();

        try {
            if (getActivity() != null)
                ((DashboardActivity) getActivity())
                        .setActionBarTitle(getResources().getString(R.string.Printersetting));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private void getPreferenceData() {
        try {
            if(getActivity()!=null) {
                Gson gson = OPMSApplication.get(getActivity()).getGson();
                sharedPreferences = OPMSApplication.get(getActivity()).getPreferences();

                String string = sharedPreferences.getString(AppConstants.PPC_DATA, "");
                ppcUserDetails = gson.fromJson(string, PPCUserDetails.class);
                if (ppcUserDetails == null) {
                    Utils.ShowDeviceSessionAlert(getActivity(),
                            getResources().getString(R.string.Printersetting),
                            getString(R.string.som_re),
                            getFragmentManager());
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private boolean CheckBlueToothState() {
        boolean btFlag = false;
        try {
            if (mBluetoothAdapter == null) {
                binding.printerBluetoothStateTv.setText(getString(R.string.bt_not));
            } else {
                if (mBluetoothAdapter.isEnabled()) {
                    btFlag = true;
                    if (mBluetoothAdapter.isDiscovering()) {
                        binding.printerBluetoothStateTv.setText(getString(R.string.bt_running));
                    } else {
                        binding.printerBluetoothStateTv.setText(getString(R.string.bt_enab));
                        binding.scandevice.setEnabled(true);
                    }
                } else {
                    binding.printerBluetoothStateTv.setText(getString(R.string.bt_not_ena));
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return btFlag;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            CheckBlueToothState();
        }
    }

    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    btArrayAdapter.add(device.getAddress() + "\n" + device.getName());
                    btArrayAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void connectBT() {
        try {
            if (!TextUtils.isEmpty(AppConstants.BT_ADDRESS)) {
                aPrinter.openBT(AppConstants.BT_ADDRESS);
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getResources().getString(R.string.print_alert_not), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener onBtnClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                switch (view.getId()) {
                    case R.id.scandevice:
                        if (binding.printerSwitch.isChecked()) {

                            if (CheckBlueToothState()) {

                                if (customProgressDialog != null && !customProgressDialog.isShowing())
                                    customProgressDialog.show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (customProgressDialog != null && customProgressDialog.isShowing())
                                            customProgressDialog.dismiss();

                                        btArrayAdapter.clear();
                                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                        mBluetoothAdapter.startDiscovery();

                                    }
                                }, 4000);


                            }
                        } else {
                            Toast.makeText(getActivity(), "Printer feature is disabled, Please enable it", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.printer_checkPrinter_Btn:
                        if (binding.printerSwitch.isChecked()) {
                            if (AppConstants.BT_ADDRESS != null) {
                                new AsyncBTPrintTask().execute();
                            } else {
                                Toast.makeText(getActivity(), "No Printer connected", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), "Printer feature is disabled, Please enable it", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private boolean checkPrinterState() {
        connectBT();
        boolean flag = false;
        try {
            Bluetooth_Printer_2inch_ThermalAPI printer = new Bluetooth_Printer_2inch_ThermalAPI();
            String feeddata = "yyy";
            feeddata = printer.line_Feed();
            aPrinter.printData(feeddata);


            if (AppConstants.BT_NAME != null && !AppConstants.BT_NAME.equalsIgnoreCase("BT_NAME"))
                binding.printerPrinterStateTv.setText("Connected to: " + AppConstants.BT_NAME);
            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (getActivity() != null)
                getActivity().unregisterReceiver(ActionFoundReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
    }

}
