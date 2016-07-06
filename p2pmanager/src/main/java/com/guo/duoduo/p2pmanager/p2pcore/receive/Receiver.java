package com.guo.duoduo.p2pmanager.p2pcore.receive;


import android.content.Context;
import android.util.Log;

import com.guo.duoduo.p2pmanager.p2pconstant.P2PConstant;
import com.guo.duoduo.p2pmanager.p2pcore.MelonHandler;
import com.guo.duoduo.p2pmanager.p2pentity.P2PFileInfo;
import com.guo.duoduo.p2pmanager.p2pentity.P2PNeighbor;
import com.guo.duoduo.p2pmanager.p2pentity.param.ParamIPMsg;

/**
 * Created by CrazyCoder on 2015/9/21.
 */
public class Receiver {
    private static final String tag = Receiver.class.getSimpleName();
    public ReceiveManager receiveManager;
    public P2PNeighbor neighbor;
    public P2PFileInfo[] files;
    public MelonHandler p2PHandler;
    protected ReceiveTask receiveTask = null;
    boolean flagPercent = false;
    Context context;

    public Receiver(ReceiveManager receiveManager, P2PNeighbor neighbor,
                    P2PFileInfo[] files,Context context) {
        this.receiveManager = receiveManager;
        this.neighbor = neighbor;
        this.files = files;
        p2PHandler = receiveManager.p2PHandler;
        this.context=context;
    }

    public void dispatchCommMSG(int cmd, ParamIPMsg ipMsg) {
        switch (cmd) {
            case P2PConstant.CommandNum.SEND_FILE_START:
                Log.d(tag, "start receiver task");
                receiveTask = new ReceiveTask(p2PHandler, this,context);
                receiveTask.start();
                break;
            case P2PConstant.CommandNum.SEND_ABORT_SELF:
                clearSelf();
                if (p2PHandler != null) {
                    p2PHandler.send2UI(cmd, ipMsg);
                }
                break;
        }
    }

    public void dispatchTCPMSG(int cmd, Object obj) {
        switch (cmd) {
            case P2PConstant.CommandNum.RECEIVE_TCP_ESTABLISHED:
                break;
            case P2PConstant.CommandNum.RECEIVE_PERCENT:
                if (p2PHandler != null)
                    p2PHandler.send2UI(P2PConstant.CommandNum.RECEIVE_PERCENT, obj);
                break;
            case P2PConstant.CommandNum.RECEIVE_OVER:
                clearSelf();
                if (p2PHandler != null)
                    p2PHandler.send2UI(P2PConstant.CommandNum.RECEIVE_OVER, null);
                break;
        }
    }

    public void dispatchUIMSG(int cmd, Object obj) {
        switch (cmd) {
            case P2PConstant.CommandNum.RECEIVE_FILE_ACK:
                if (p2PHandler != null)
                    p2PHandler.send2Sender(neighbor.inetAddress, cmd, null);
                break;
            case P2PConstant.CommandNum.RECEIVE_ABORT_SELF:
                clearSelf();
                if (p2PHandler != null)
                    p2PHandler.send2Sender(neighbor.inetAddress, cmd, null);
                break;
        }
    }

    private void clearSelf() {
        receiveManager.init();
    }
}
