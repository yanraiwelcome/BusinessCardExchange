package com.project.businesscardexchange.ui.uientity;


import android.graphics.drawable.Drawable;

import com.guo.duoduo.p2pmanager.p2pconstant.P2PConstant;


/**
 * Created by CrazyCoder on 2015/9/15.
 */
public class PictureInfo implements IInfo {
    public int type = P2PConstant.TYPE.PIC;
    public String picPath;
    public String picSize;
    public String picName;


    @Override
    public String getFileGuiName() {
        return null;
    }

    @Override
    public String getFilePath() {
        return picPath;
    }

    @Override
    public String getFileSize() {
        return picSize;
    }

    @Override
    public int getFileType() {
        return type;
    }

    @Override
    public Drawable getFileIcon() {
        return null;
    }

    @Override
    public String getFileName() {
        return picName;
    }

    @Override
    public String getPhotoPath() {
        return picPath;
    }

    @Override
    public boolean equals(Object o) {
        if (getFilePath() != null && ((PictureInfo) o).getFilePath() != null)
            return getFilePath().equals(((PictureInfo) o).getFilePath());
        else
            return false;
    }
}
