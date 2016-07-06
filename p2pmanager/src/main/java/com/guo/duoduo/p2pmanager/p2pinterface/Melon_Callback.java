package com.guo.duoduo.p2pmanager.p2pinterface;


import com.guo.duoduo.p2pmanager.p2pentity.P2PNeighbor;

/**
 * Created by CrazyCoder on 2015/9/19.
 */
public interface Melon_Callback {
    /**
     *
     *
     * @param melon
     */
    public void Melon_Found(P2PNeighbor melon);

    /**
     *
     * @param melon
     */
    public void Melon_Removed(P2PNeighbor melon);
}
