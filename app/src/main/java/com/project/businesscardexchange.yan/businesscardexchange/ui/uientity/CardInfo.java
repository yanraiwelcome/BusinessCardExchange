package com.project.businesscardexchange.yan.businesscardexchange.ui.uientity;


import android.graphics.drawable.Drawable;

import com.guo.duoduo.p2pmanager.p2pconstant.P2PConstant;


/**
 * Created by CrazyCoder on 2015/9/15.
 */
public class CardInfo implements IInfo {
    public int type = P2PConstant.TYPE.APP;
    public Drawable appIcon;

    @Override
    public String getPhotoPath() {
        return photoPath;
    }



    public String photoPath;
    public String logoPath;
    public String appLabel;
    public String FileGuiName;
    public String pkgName;
    public String appSize;
    public String appFilePath;



    @Override
    public String getFileGuiName() {
        return FileGuiName;
    }

    @Override
    public String getFilePath() {
        return appFilePath;
    }

    @Override
    public String getFileSize() {
        return appSize;
    }

    @Override
    public int getFileType() {
        return type;
    }

    @Override
    public Drawable getFileIcon() {
        return appIcon;
    }

    @Override
    public String getFileName() {
        return appLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (getFilePath() != null && ((CardInfo) o).getFilePath() != null)
            return getFilePath().equals(((CardInfo) o).getFilePath());
        else
            return false;
    }

}
