package com.bghd.express.entiy;

import java.util.List;

/**
 * Created by lixu on 2018/2/11.
 */

public class ShowImgEntity {

    /**
     * status : 1
     * data : [{"id":"213","uid":"0","name":"5a06b0269248f.jpg","type":"local","path":"/Uploads/Picture/2017-11-11/5a06b0269248f.jpg","url":"","md5":"c025e1c3beec786cd3766cab26d6cce4","sha1":"628e4b71f30e0dbf5888bc27a2d709b2f0e1d77b","status":"1","create_time":"2017-11-11 16:09","img":"http://www.baigehuidi.com/Uploads/Picture/2017-11-11/5a06b0269248f.jpg"},{"id":"214","uid":"0","name":"5a06b0ac073e5.jpg","type":"local","path":"/Uploads/Picture/2017-11-11/5a06b0ac073e5.jpg","url":"","md5":"d763a075cbf9cc302cfc50c6b1fdfd3b","sha1":"adc65ebf2b8aabb8b78ff761849e5a5fb7d8799b","status":"1","create_time":"2017-11-11 16:11","img":"http://www.baigehuidi.com/Uploads/Picture/2017-11-11/5a06b0ac073e5.jpg"},{"id":"216","uid":"0","name":"5a76b2959d380.jpg","type":"local","path":"/Uploads/Picture/2018-02-04/5a76b2959d380.jpg","url":"","md5":"9008250b92ccb6126d3fc89af8a99aaf","sha1":"0d81e8a63aaea978ca5518a682fd42f2ac4daf84","status":"1","create_time":"02月04日 15:13","img":"http://www.baigehuidi.com/Uploads/Picture/2018-02-04/5a76b2959d380.jpg"}]
     */

    private int status;
    private List<DataBean> data;
    private String info;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
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
         * id : 213
         * uid : 0
         * name : 5a06b0269248f.jpg
         * type : local
         * path : /Uploads/Picture/2017-11-11/5a06b0269248f.jpg
         * url :
         * md5 : c025e1c3beec786cd3766cab26d6cce4
         * sha1 : 628e4b71f30e0dbf5888bc27a2d709b2f0e1d77b
         * status : 1
         * create_time : 2017-11-11 16:09
         * img : http://www.baigehuidi.com/Uploads/Picture/2017-11-11/5a06b0269248f.jpg
         */

        private String id;
        private String uid;
        private String name;
        private String type;
        private String path;
        private String url;
        private String md5;
        private String sha1;
        private String status;
        private String create_time;
        private String img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getSha1() {
            return sha1;
        }

        public void setSha1(String sha1) {
            this.sha1 = sha1;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
