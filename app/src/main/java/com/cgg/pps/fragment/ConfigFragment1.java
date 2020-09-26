//package com.cgg.pps.fragment;
//
//
//import android.Manifest;
//import android.app.ActionBar;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.analogics.thermalAPI.Bluetooth_Printer_2inch_ThermalAPI;
//import com.analogics.thermalprinter.AnalogicsThermalPrinter;
//import com.cgg.pps.R;
//import com.cgg.pps.application.OPMSApplication;
//import com.cgg.pps.databinding.ConfigFragmnetBinding;
//import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
//import com.cgg.pps.util.AppConstants;
//import com.cgg.pps.util.CustomProgressDialog;
//import com.cgg.pps.util.Utils;
//import com.cgg.pps.view.DashboardActivity;
//import com.cgg.pps.view.LoginActivity;
//import com.cgg.pps.view.SplashActivity;
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//
//import java.io.IOException;
//
//import androidx.core.content.ContextCompat;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.Fragment;
//
//public class ConfigFragment1 extends Fragment {
//
//    private static final int REQUEST_ENABLE_BT = 1;
//
//    private BluetoothAdapter mBluetoothAdapter = null;
//    private AnalogicsThermalPrinter conn = new AnalogicsThermalPrinter();
//
//    private ArrayAdapter<String> btArrayAdapter;
//    private ConfigFragmnetBinding binding;
//    private PPCUserDetails ppcUserDetails;
//    private CustomProgressDialog customProgressDialog;
//    private SharedPreferences sharedPreferences;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        try {
//            binding = DataBindingUtil.inflate(inflater, R.layout.config_fragmnet, container, false);
//
//
//            customProgressDialog = new CustomProgressDialog(getActivity());
//            getPreferenceData();
//
//            binding.scandevice.setOnClickListener(onBtnClick);
//            binding.printerCheckPrinterBtn.setOnClickListener(onBtnClick);
//
//
//            btArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
//            binding.devicesfound.setAdapter(btArrayAdapter);
//            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//            CheckBlueToothState();
//            getActivity().registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
//
//            binding.devicesfound.setOnItemClickListener(new OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    try {
//
//                        if (customProgressDialog != null && !customProgressDialog.isShowing())
//                            customProgressDialog.show();
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                if (customProgressDialog != null && customProgressDialog.isShowing())
//                                    customProgressDialog.dismiss();
//
//                                String btAddress = null;
//                                String btName = null;
//                                String selection = (String) (binding.devicesfound.getItemAtPosition(position));
//                                if (selection != null && !selection.equalsIgnoreCase("")) {
//                                    btAddress = selection.substring(0, 17);
//                                }
//
//                                if (selection != null && !selection.equalsIgnoreCase("")) {
//                                    String[] str = selection.split("\n");
//                                    if (str.length > 0) {
//                                        btName = str[1];
//                                    }
//                                }
//                                if (btAddress != null) {
//
//                                    boolean flag = Utils.saveBTAddress(btAddress, btName);
//                                    if (flag) {
//                                        sharedPreferences.edit().putBoolean(AppConstants.PRINTER_CON_FLAG, false);
//                                        sharedPreferences.edit().commit();
//                                        Toast.makeText(getActivity(), "BLUETOOTH ADDRESS IS SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
//                                    }
//                                    connectBT();
//                                } else {
//                                    Toast.makeText(getActivity(), "Something went wrong",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                        }, 3000);
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return binding.getRoot();
//    }
//
//    public void onResume() {
//        super.onResume();
//
//        try {
//            if (getActivity() != null)
//                ((DashboardActivity) getActivity())
//                        .setActionBarTitle(getResources().getString(R.string.Printersetting));
//
//        } catch (Resources.NotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void getPreferenceData() {
//        try {
//            Gson gson = OPMSApplication.get(getActivity()).getGson();
//            sharedPreferences = OPMSApplication.get(getActivity()).getPreferences();
//            String string = sharedPreferences.getString(AppConstants.PPC_DATA, "");
//            ppcUserDetails = gson.fromJson(string, PPCUserDetails.class);
//            if (ppcUserDetails == null) {
//                //call logout directly by showing something wrong alert
//            }
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean CheckBlueToothState() {
//        boolean btFlag = false;
//        try {
//            if (mBluetoothAdapter == null) {
//                binding.printerBluetoothStateTv.setText(getString(R.string.bt_not));
//            } else {
//                if (mBluetoothAdapter.isEnabled()) {
//                    btFlag = true;
//                    if (mBluetoothAdapter.isDiscovering()) {
//                        binding.printerBluetoothStateTv.setText(getString(R.string.bt_running));
//                    } else {
//                        binding.printerBluetoothStateTv.setText(getString(R.string.bt_enab));
//                        binding.scandevice.setEnabled(true);
//                    }
//                } else {
//                    binding.printerBluetoothStateTv.setText(getString(R.string.bt_not_ena));
//                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return btFlag;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_ENABLE_BT) {
//            CheckBlueToothState();
//        }
//    }
//
//    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            try {
//                String action = intent.getAction();
//                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                    btArrayAdapter.add(device.getAddress() + "\n" + device.getName());
//                    btArrayAdapter.notifyDataSetChanged();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    private void connectBT() {
//        AnalogicsThermalPrinter conn = new AnalogicsThermalPrinter();
//        try {
//            if (!TextUtils.isEmpty(AppConstants.BT_ADDRESS)) {
//                conn.openBT(AppConstants.BT_ADDRESS);
//                binding.printerBluetoothAddressTv.setText(AppConstants.BT_ADDRESS);
//            } else {
//                Toast.makeText(getActivity(), "BT Address is empty", Toast.LENGTH_SHORT).show();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private View.OnClickListener onBtnClick = new OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            try {
//                switch (view.getId()) {
//                    case R.id.scandevice:
//                        if (CheckBlueToothState()) {
//
//                            if (customProgressDialog != null && !customProgressDialog.isShowing())
//                                customProgressDialog.show();
//
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    if (customProgressDialog != null && customProgressDialog.isShowing())
//                                        customProgressDialog.dismiss();
//
//                                    btArrayAdapter.clear();
//                                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                                    mBluetoothAdapter.startDiscovery();
//
//                                }
//                            }, 4000);
//
//
//                        }
//                        break;
//                    case R.id.printer_checkPrinter_Btn:
//                        checkPrinterState();
//                        break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    private void checkPrinterState() {
//        try {
//            if (AppConstants.BT_ADDRESS != null) {
//                conn.openBT(AppConstants.BT_ADDRESS);
//                Bluetooth_Printer_2inch_ThermalAPI printer = new Bluetooth_Printer_2inch_ThermalAPI();
//                String feeddata = "yyy";
//                feeddata = printer.line_Feed();
//                conn.printData(feeddata);
//                if (AppConstants.BT_NAME != null)
//                    binding.printerPrinterStateTv.setText("Connected to: " + AppConstants.BT_NAME);
//            } else {
//                Toast.makeText(getActivity(),  getResources().getString(R.string.print_alert_not), Toast.LENGTH_SHORT).show();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onDestroy() {
//        try {
//            getActivity().unregisterReceiver(ActionFoundReceiver);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onDestroy();
//    }
//
//}
