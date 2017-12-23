package com.wind.advertisementreceiveapplication.network.model;

/**
 * Created by zhengzhe on 2017/12/14.
 */

public class ReceiveInfoFromServer {
    private String url = null;
    private String playTime = null;
    private int order_status = -1;
    private String name;
    public ReceiveInfoFromServer(String url, String playTime, int order_status, String name) {
        this.url = url;
        this.playTime = playTime;
        this.order_status = order_status;
        this.name = name;
    }

    public String getVideoName() {
        return name;
    }

    public int getOrderStatus() {
        return order_status;
    }

    public String getUrl() {
        return url;
    }

    public String getPlayTime() {
        return playTime;
    }
}
