package com.wind.advertisementreceiveapplication.network.model;

/**
 * Created by zhengzhe on 2017/12/26.
 */

public class PostModelUploadPlayStatus {
    private String tableName = "orderInfo";
    private String todo = "update";
    private String orderId;
    private int orderStatus = -1;   //订单状态：0审核通过还未播放，1已播放，2未审核，3审核不通过，4审核通过未上传
    public PostModelUploadPlayStatus(int orderStatus, String orderId) {
        this.orderStatus = orderStatus;
        this.orderId = orderId;
    }
}
