package com.bghd.express.entiy;

/**
 * Created by lixu on 2018/2/6.
 */

public class UserEntity {

    /**
     * status : 1
     * data : {"uid":"15011"}
     */

    private int status;
    private DataBean data;
    private String info;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static class DataBean {
        /**
         * uid : 15011
         */

        private String uid;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
