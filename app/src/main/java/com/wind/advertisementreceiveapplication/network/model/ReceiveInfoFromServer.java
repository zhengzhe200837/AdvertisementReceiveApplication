package com.wind.advertisementreceiveapplication.network.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhengzhe on 2017/12/14.
 */

public class ReceiveInfoFromServer implements Parcelable{
    private String url = null;
    private String playTime = null;
    private int order_status = -1;
    private String name;
    private int playTimes;
    private String orderId;
    public ReceiveInfoFromServer(String url, String playTime, int order_status, String name, int playTimes, String orderId) {
        this.url = url;
        this.playTime = playTime;
        this.order_status = order_status;
        this.name = name;
        this.playTimes = playTimes;
        this.orderId = orderId;
    }

    public String toString() {
        return "" + "url = " + url + " playTime = " + playTime + " order_status = " + order_status +
                " name = " + name + " playTimes = " + playTimes + " orderId = " + orderId;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(playTime);
        dest.writeInt(order_status);
        dest.writeString(name);
        dest.writeInt(playTimes);
        dest.writeString(orderId);
    }

    public static final Parcelable.Creator<ReceiveInfoFromServer> CREATOR = new Creator<ReceiveInfoFromServer>() {
        @Override
        public ReceiveInfoFromServer createFromParcel(Parcel source) {
            return new ReceiveInfoFromServer(source);
        }

        @Override
        public ReceiveInfoFromServer[] newArray(int size) {
            return new ReceiveInfoFromServer[size];
        }
    };


    private ReceiveInfoFromServer(Parcel in) {
        this.url = in.readString();
        this.playTime = in.readString();
        this.order_status = in.readInt();
        this.name = in.readString();
        this.playTimes = in.readInt();
        this.orderId = in.readString();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getVideoName() {
        return name;
    }

    public int getPlayTimes() {
        return playTimes;
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
