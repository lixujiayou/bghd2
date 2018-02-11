package com.bghd.express.entiy;

/**
 * Created by lixu on 2018/2/9.
 */

public class SaveOrderEntity {


    /**
     * status : 2
     * info : 缺少运单号
     */

    private int status;
    private String date;
    private String info;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
