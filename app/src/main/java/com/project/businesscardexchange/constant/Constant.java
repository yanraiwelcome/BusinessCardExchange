package com.project.businesscardexchange.constant;


import com.project.businesscardexchange.MyApplication;
import com.project.businesscardexchange.R;


/**
 * Created by CrazyCoder on 2015/9/15.
 */
public class Constant {
    public static final String WIFI_HOT_SPOT_SSID_PREFIX = MyApplication.getInstance()
            .getString(R.string.app_name);
    public static final String FREE_SERVER = "192.168.43.1";

    public interface MSG {
        public static final int PICTURE_OK = 0;
        public static final int APP_OK = 1;
    }

}
