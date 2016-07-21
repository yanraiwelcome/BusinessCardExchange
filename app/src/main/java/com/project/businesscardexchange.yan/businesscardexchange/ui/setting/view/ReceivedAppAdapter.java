package com.project.businesscardexchange.yan.businesscardexchange.ui.setting.view;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import  com.project.businesscardexchange.yan.businesscardexchange.R;
import com.project.businesscardexchange.yan.businesscardexchange.ui.uientity.CardInfo;
import com.project.businesscardexchange.yan.businesscardexchange.ui.uientity.IInfo;

import java.io.File;
import java.util.ArrayList;


public class ReceivedAppAdapter extends RecyclerView.Adapter<ReceivedAppAdapter.MyHolder> {
    private Context mContext;
    private ArrayList<IInfo> mAppInfoList;

    public ReceivedAppAdapter(Context context, ArrayList<IInfo> appInfoList) {
        mContext = context;
        mAppInfoList = appInfoList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder myHolder = new MyHolder(LayoutInflater.from(mContext).inflate(
                R.layout.view_received_app_item, null));

        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        final CardInfo app = (CardInfo) mAppInfoList.get(position);

        if (app == null)
            return;

        holder.mIcon.setImageBitmap(((BitmapDrawable) app.getFileIcon()).getBitmap());
        holder.mName.setText(app.getFileName());
        holder.mSize.setText(app.getFileSize());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // install app
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(app.getFilePath())),
                        "application/vnd.android.package-archive");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppInfoList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        ImageView mIcon;
        TextView mName;
        TextView mSize;
        LinearLayout mLayout;

        public MyHolder(View view) {
            super(view);
            mIcon = (ImageView) view.findViewById(R.id.received_app_icon);
            mName = (TextView) view.findViewById(R.id.received_app_name);
            mSize = (TextView) view.findViewById(R.id.received_app_size);
            mLayout = (LinearLayout) view.findViewById(R.id.received_app_layout);
        }

    }
}
