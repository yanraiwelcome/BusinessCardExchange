package com.project.businesscardexchange.yan.businesscardexchange.ui.transfer.view;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.project.businesscardexchange.R;
import com.project.businesscardexchange.yan.businesscardexchange.sdk.cache.Cache;
import com.project.businesscardexchange.yan.businesscardexchange.ui.uientity.IInfo;
import com.guo.duoduo.p2pmanager.p2pentity.P2PFileInfo;

import java.io.File;
import java.util.List;


/**
 * Created by CrazyCoder on 2015/9/16.
 */
public class ImageSelectAdapter
        extends
        RecyclerView.Adapter<ImageSelectAdapter.MyViewHolder> {

    private static final String tag = ImageSelectAdapter.class.getSimpleName();

    private List<IInfo> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ImageSelectAdapter(Context context, List<IInfo> list) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public ImageSelectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(
                R.layout.view_pic_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ImageSelectAdapter.MyViewHolder holder,
                                 int position) {
        Glide.with(context).load(list.get(position).getFilePath()).into(holder.imageView);

        IInfo info = list.get(position);
        P2PFileInfo fileInfo = new P2PFileInfo();
        fileInfo.name = info.getFileName();
        fileInfo.type = info.getFileType();
        fileInfo.size = new File(info.getFilePath()).length();
        fileInfo.path = info.getFilePath();

        if (Cache.selectedList.contains(fileInfo))
            holder.pic_choice.setVisibility(View.VISIBLE);
        else
            holder.pic_choice.setVisibility(View.INVISIBLE);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.imageView,
                            holder.getLayoutPosition());
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
        ImageView pic_choice;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            pic_choice = (ImageView) view.findViewById(R.id.pic_choice);
        }
    }

}
