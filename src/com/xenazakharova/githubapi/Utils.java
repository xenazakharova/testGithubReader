package com.xenazakharova.githubapi;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {
	static void setLocal(Context mContext, String lang) {
		Locale myLocale = new Locale(lang);
        Resources res = mContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
	
	static boolean isOnline(Context mContext){
    	final ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
 
        NetworkInfo nInfo = connMgr.getActiveNetworkInfo();
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //Log.v(LOG_TAG, "Model wifi = "+wifi.isAvailable());
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //Log.v(LOG_TAG, "Model mobile = "+mobile.isAvailable());
        //Log.v(LOG_TAG, "Model nInfo = "+nInfo);
        //Log.v(LOG_TAG, "Model nInfo.isConnected() = "+nInfo.isConnected());
        if( (wifi.isAvailable() || mobile.isAvailable()) && nInfo != null && nInfo.isConnected() )
        	return true;
        else 
            return false;
     }
	
	 static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
          
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
