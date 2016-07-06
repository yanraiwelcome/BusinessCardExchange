package com.guo.duoduo.p2pmanager.p2pinterface;


import com.guo.duoduo.p2pmanager.p2pentity.P2PFileInfo;
import com.guo.duoduo.p2pmanager.p2pentity.P2PNeighbor;

/**
 * Created by CrazyCoder on 2015/9/20.
 */
public interface SendFile_Callback {
    public void BeforeSending();

    public void OnSending(P2PFileInfo file, P2PNeighbor dest);

    public void AfterSending(P2PNeighbor dest);

    public void AfterAllSending();

    public void AbortSending(int error, P2PNeighbor dest);
}
