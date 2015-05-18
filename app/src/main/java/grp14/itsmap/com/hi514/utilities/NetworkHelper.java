package grp14.itsmap.com.hi514.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import grp14.itsmap.com.hi514.H5App;

public class NetworkHelper {

    //Check if network is available (Doesn't matter what kind of network, 3G, WiFi, 4G, etc..)
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) H5App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}