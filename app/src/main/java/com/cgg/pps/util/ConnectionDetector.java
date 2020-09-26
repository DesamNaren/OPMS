package com.cgg.pps.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    public static Boolean isConnectingToInternet(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED && canHit()) {
                            return true;
                        }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean isConnectedToInternet(Context context) {
        // TODO Auto-generated method stub
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (mWifi.isConnected() && canHit()) {
                //if wifi connected
                return true;
            }

            if (mMobile.isConnected() && canHit()) {
                //if internet connected
                return true;
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    private static Boolean canHit() {

        /*try {

            URL url = new URL("http://www.google.com/");
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setConnectTimeout(30000);
            urlConnection.connect();
            urlConnection.disconnect();
            return true;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }*/
        return true;
    }

    public static Boolean checkActiveInternet(Context context) {
        return canHit() && isConnectedToInternet(context);
    }

}
