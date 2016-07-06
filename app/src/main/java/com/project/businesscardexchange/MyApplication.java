package com.project.businesscardexchange;


import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.view.Display;
import android.view.WindowManager;

import com.project.businesscardexchange.utils.permission.Nammu;

import java.io.File;


/**
 * Created by CrazyCoder on 2015/9/11.
 */
public class MyApplication extends Application {

    public static int SCREEN_WIDTH;
    private static MyApplication instance;
    public static final String IMAGE_DIRECTORY_NAME = "BusinessExchange";
    public static final String ZIP_DIRECTORY_NAME = "BusinessExchange/Zip/";
    public static final String UNZIP_DIRECTORY_NAME = "BusinessExchange/Unzip/";
    public static MyApplication getInstance() {
        return instance;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    /*static public Realm getRealmInstance(Context context)
    {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context).build();
        try {
            return Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e){
            try {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                return Realm.getInstance(realmConfiguration);
            } catch (Exception ex){
                throw ex;
                //No Realm file to remove.
            }
        }
    }
    */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Nammu.init(getApplicationContext());
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point screen = new Point();
        display.getSize(screen);
        SCREEN_WIDTH = Math.min(screen.x, screen.y);

        initImageLoader();
    }

    private void initImageLoader() {
//        ImageLoaderConfig config = new ImageLoaderConfig()
//                .setLoadingPlaceholder(R.drawable.icon_loading)
//                .setCache(new MemoryCache())
//                .setLoadPolicy(new SerialPolicy());
//        SimpleImageLoader.getInstance().init(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
    public static File getCardLocation()
    {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(getCardRootLocationDir()),getCardLocationDir());
        
        return mediaStorageDir;
    }
    public static String getCardLocationDir()
    {
        return MyApplication.IMAGE_DIRECTORY_NAME;
    }
    public static String getCardRootLocationDir()
    {

        return Environment.DIRECTORY_PICTURES;
    }
}
