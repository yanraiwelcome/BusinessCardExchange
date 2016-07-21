package com.project.businesscardexchange.yan.businesscardexchange.ui.transfer.fragment;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.guo.duoduo.p2pmanager.p2pconstant.P2PConstant;
import com.guo.duoduo.p2pmanager.p2pentity.P2PFileInfo;
import com.project.businesscardexchange.yan.businesscardexchange.MyApplication;
import com.project.businesscardexchange.R;
import com.project.businesscardexchange.yan.businesscardexchange.models.BusinessCard;
import com.project.businesscardexchange.yan.businesscardexchange.sdk.cache.Cache;
import com.project.businesscardexchange.yan.businesscardexchange.ui.transfer.view.CardSelectAdapter;
import com.project.businesscardexchange.yan.businesscardexchange.ui.uientity.CardInfo;
import com.project.businesscardexchange.yan.businesscardexchange.ui.uientity.IInfo;
import com.project.businesscardexchange.yan.businesscardexchange.ui.view.MyWindowManager;
import com.project.businesscardexchange.yan.businesscardexchange.utils.DBHelper;
import com.project.businesscardexchange.yan.businesscardexchange.utils.DeviceUtils;
import com.project.businesscardexchange.yan.businesscardexchange.utils.ViewUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by CrazyCoder on 2015/9/16.
 */
public class CardFragment extends Fragment
        implements
        CardSelectAdapter.OnItemClickListener,
        OnSelectItemClickListener {
    public static final int ANIMATION_DURATION = 800;
    private static final String tag = CardFragment.class.getSimpleName();
    private View view = null;
    private List<IInfo> appList = new ArrayList<>();
    private PackageManager pkManager;
  //  private AppFragmentHandler handler;
    private CardSelectAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private OnSelectItemClickListener clickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            Log.d(tag, "CardFragment onCreateView function");
           // myRealm = MyApplication.getRealmInstance(getActivity());
            myDbHelper = DBHelper.getInstance(getActivity());
            view = inflater.inflate(R.layout.view_select, container, false);
            //handler = new AppFragmentHandler(CardFragment.this);

            recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            adapter = new CardSelectAdapter(getActivity(), appList);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
            progressBar = (ProgressBar) view.findViewById(R.id.loading);

/*
            RealmResults<BusinessCardRealm> results1 =  myRealm.where(BusinessCardRealm.class).findAll();
            for(BusinessCard card:results1) {
                bizCardLists.add(card);
            }
            */
            ArrayList<BusinessCard> bizCardLists= myDbHelper.getAllCards();


            getAppInfo(bizCardLists);
        }

        return view;
    }

    private void getAppInfo(final ArrayList<BusinessCard> bizCardLists) {


       /* new Thread() {
            public void run() {*/
                appList.clear();
                appList.addAll(getCards(bizCardLists));
                Log.d(tag, "app list size =" + appList.size());
                progressBar.setVisibility(View.GONE);
              /*  Message msg = Message.obtain();
                msg.what = Constant.MSG.APP_OK;
                handler.sendMessage(msg);
            }
        }.start();*/

    }
   // Realm myRealm;
    DBHelper myDbHelper;
    private List<IInfo> getCards(ArrayList<BusinessCard> results1) {
      //  pkManager = MyApplication.getInstance().getPackageManager();

        List<IInfo> appInfo = new ArrayList<>();
        appInfo.clear();
        try {

            for(BusinessCard card:results1)
            {
               // bizCardLists.add(c);
                String timestamp = card.getTimestamp();
                String inputPath = Environment.getExternalStoragePublicDirectory(MyApplication.getCardRootLocationDir())+File.separator+MyApplication.ZIP_DIRECTORY_NAME;
                Log.d(tag,"inputPath:"+inputPath);
                File file = new File(inputPath+timestamp+".zip");
                Log.d(tag,"filePath:"+file.getAbsolutePath());
                if (file.exists())
                {
                    Log.d(tag,"Yes Exist:");
                    CardInfo info = getCardInfo(card);

                    if (info == null)
                        continue;
                    else if (!appInfo.contains(info))
                        appInfo.add(info);
                }
            }

            //lists = select.all().from(BCard.class).orderBy("name ASC").execute();
        } catch (Exception e)
        {
            Log.e("Error","No any record:"+e.getLocalizedMessage());
            e.fillInStackTrace();
        }

       // List<ApplicationInfo> listApp = pkManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
      //  Collections.sort(listApp, new ApplicationInfo.DisplayNameComparator(pkManager));
      //  List<IInfo> appInfo = new ArrayList<>();

/*
        for (ApplicationInfo app : listApp) { // get the third APP
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                CardInfo info = getCardInfo(app);
                if (info == null)
                    continue;
                else if (!appInfo.contains(info))
                    appInfo.add(info);
            }
        }*/

        return appInfo;
    }

    public static CardInfo getCardInfo(BusinessCard app) {
        CardInfo cardInfo = new CardInfo();
        //String label = ((String)app.loadLabel(pkManager)).replace(" ","") + ".apk";
        String label = app.getTimestamp()+ ".zip";
        cardInfo.appLabel = label;
        cardInfo.FileGuiName = app.getName();
        cardInfo.photoPath = app.getPhoto();
        cardInfo.logoPath = app.getPhotocompanylogo();
       // cardInfo.appIcon = app.loadIcon(pkManager);
        cardInfo.pkgName = app.getName();

        String filepath;
        String inputPath = Environment.getExternalStoragePublicDirectory(MyApplication.getCardRootLocationDir())+File.separator+MyApplication.ZIP_DIRECTORY_NAME;

        try
        {
            filepath = inputPath+app.getTimestamp()+".zip";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        if (filepath == null)
        {
            return null;
        }
        cardInfo.appFilePath = filepath;
        File file = new File(filepath);
        long fileSize = file.length();
        if (fileSize <= 0)
            return null;
        String size = DeviceUtils.convertByte(fileSize);
        cardInfo.appSize = size;

        return cardInfo;
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            clickListener = (OnSelectItemClickListener) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(View view, int position) {
        CardInfo info = ((CardInfo) adapter.getItem(position));
        P2PFileInfo fileInfo = new P2PFileInfo();
        fileInfo.name = info.getFileName();
        fileInfo.type = P2PConstant.TYPE.APP;
        fileInfo.size = new File(info.getFilePath()).length();
        fileInfo.path = info.getFilePath();

        if (Cache.selectedList.contains(fileInfo)) {
            Cache.selectedList.remove(fileInfo);
        } else {
            Cache.selectedList.add(fileInfo);
            startFloating(view, position);
        }
        adapter.notifyDataSetChanged();
        clickListener.onItemClicked(P2PConstant.TYPE.APP);
    }

    @Override
    public void onItemClicked(int type) {

    }

    private void startFloating(View view, int position) {
        if (!MyWindowManager.isWindowShowing()) {
            int[] location = ViewUtils.getViewItemLocation(view);
            int viewX = location[0];
            int viewY = location[1];

            MyWindowManager.createSmallWindow(getActivity(), viewX, viewY, 0, 0, adapter
                    .getItem(position).getFileIcon());
        }
    }

  /*  private static class AppFragmentHandler extends Handler {
        private WeakReference<CardFragment> weakReference;

        public AppFragmentHandler(CardFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            CardFragment fragment = weakReference.get();
            if (fragment == null)
                return;
            if (fragment.getActivity() == null)
                return;

            if (fragment.getActivity().isFinishing())
                return;

            switch (msg.what) {
                case Constant.MSG.APP_OK:
                    fragment.adapter.notifyDataSetChanged();
                    fragment.progressBar.setVisibility(View.GONE);
                    break;
            }

        }
    }*/

}
