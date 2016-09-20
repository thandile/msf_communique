package com.example.msf.msf.API;

import com.squareup.otto.Bus;

/**
 * Created by Thandile on 2016/07/27.
 */
public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance(){
        return BUS;
    }

    public BusProvider(){}
}
