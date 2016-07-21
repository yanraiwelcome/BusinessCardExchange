package com.project.businesscardexchange.yan.businesscardexchange.ui.transfer.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.duoduo.p2pmanager.p2pentity.P2PFileInfo;
import com.project.businesscardexchange.R;
import com.project.businesscardexchange.yan.businesscardexchange.sdk.cache.Cache;
import com.project.businesscardexchange.yan.businesscardexchange.ui.uientity.IInfo;

import java.io.File;
import java.util.List;


/**
 * Created by CrazyCoder on 2015/9/15.
 */
public class CardSelectAdapter extends RecyclerView.Adapter<CardSelectAdapter.MyViewHolder> {
    private Context context;
    private List<IInfo> list;
    private OnItemClickListener onItemClickListener;

    public CardSelectAdapter(Context context, List<IInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public CardSelectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(
                R.layout.view_app_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final CardSelectAdapter.MyViewHolder holder,
                                 final int position) {


       /* holder.imageView.setImageBitmap(((BitmapDrawable) list.get(position)
                .getFileIcon()).getBitmap());*/
        String encodedImage = list.get(position).getPhotoPath();
        //String encodedImageLogo = song.getPhotocompanylogo();

        try {
            // byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
           Bitmap decodedBitmap = BitmapFactory.decodeFile(encodedImage);//BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(decodedBitmap);
        }catch (Exception e){
            e.printStackTrace();
        }

        IInfo info = list.get(position);
        P2PFileInfo fileInfo = new P2PFileInfo();
        fileInfo.name = info.getFileName();
        fileInfo.type = info.getFileType();
        fileInfo.size = new File(info.getFilePath()).length();
        fileInfo.path = info.getFilePath();

        if (Cache.selectedList.contains(fileInfo)) {
            holder.app_choice.setVisibility(View.VISIBLE);
        } else {
            holder.app_choice.setVisibility(View.GONE);
        }

        holder.appName.setText(list.get(position).getFileName());
        holder.appSize.setText(list.get(position).getFileSize());

        holder.FileName.setText(list.get(position).getFileGuiName());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemLayout, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public IInfo getItem(int position) {
        return list.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView app_choice;
        TextView appName;
        TextView appSize;
        TextView FileName;
        LinearLayout itemLayout;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.AppIcon);
            appName = (TextView) view.findViewById(R.id.AppName);
            appSize = (TextView) view.findViewById(R.id.AppSize);
            app_choice = (ImageView) view.findViewById(R.id.app_choice);
            itemLayout = (LinearLayout) view.findViewById(R.id.app_item_layout);
            FileName = (TextView) view.findViewById(R.id.FileName);
        }
    }
}
