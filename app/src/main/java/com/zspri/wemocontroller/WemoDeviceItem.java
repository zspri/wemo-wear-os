package com.zspri.wemocontroller;

public class WemoDeviceItem {
    private String iName;
    private String iHash;
    private String iAddr;
    private int iState;

    public WemoDeviceItem(String name, String addr, String hash, int state) {
        iName = name;
        iAddr = addr;
        iHash = hash;
        iState = state;
    }

    public String getName() {
        return iName;
    }

    public String getHash() {
        return iHash;
    }

    public String getAddr() {
        return iAddr;
    }

    public int getState() {
        return iState;
    }

    public void setState(int state) {
        iState = state;
    }
}
