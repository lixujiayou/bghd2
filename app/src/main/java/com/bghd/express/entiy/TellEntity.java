package com.bghd.express.entiy;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lixu on 2018/2/8.
 */

public class TellEntity {

    /**
     * status : 1
     * date : [{"id":"17672","type":"shipuser","uid":"15011","nickname":"18366158972","truename":"樊兆焕","card":null,"mobile":"18754176598","country":"中国","province":"山东省","city":"济南市","district":"历城区","address":"三威大厦2503","map":null,"create_time":"1516937936","address_id":"370112"},{"id":"16449","type":"shipuser","uid":"15011","nickname":"18366158972","truename":"好好超市","card":null,"mobile":"15066103378","country":"中国","province":"山东省","city":"济南市","district":"历下区","address":"山东省济南市历下区华阳路好好超市","map":null,"create_time":"1515225854","address_id":"370102"},{"id":"16346","type":"shipuser","uid":"15011","nickname":"18366158972","truename":"三威","card":null,"mobile":"49798","country":"中国","province":"山东省","city":"济南市","district":"历城区","address":"三威大厦","map":null,"create_time":"1515038761","address_id":"370112"},{"id":"15911","type":"shipuser","uid":"15011","nickname":"18366158972","truename":"贾夏夏","card":null,"mobile":"18615575679","country":"中国","province":"山东省","city":"济南市","district":"历城区","address":"山东省济南市历城区七里河路特百惠","map":null,"create_time":"1514019816","address_id":"370112"},{"id":"15701","type":"shipuser","uid":"15011","nickname":"18366158972","truename":"林小明","card":null,"mobile":"13290297810","country":"中国","province":"山东省","city":"济南市","district":"历下区","address":"东仓","map":null,"create_time":"1513577197","address_id":"370102"}]
     */

    private int status;
    private List<DateBean> date;
    private String info;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DateBean> getDate() {
        return date;
    }

    public void setDate(List<DateBean> date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static class DateBean implements Serializable{
        /**
         * id : 17672
         * type : shipuser
         * uid : 15011
         * nickname : 18366158972
         * truename : 樊兆焕
         * card : null
         * mobile : 18754176598
         * country : 中国
         * province : 山东省
         * city : 济南市
         * district : 历城区
         * address : 三威大厦2503
         * map : null
         * create_time : 1516937936
         * address_id : 370112
         */

        private String id;
        private String type;
        private String uid;
        private String nickname;
        private String truename;
        private Object card;
        private String mobile;
        private String country;
        private String province;
        private String city;
        private String district;
        private String address;
        private Object map;
        private String create_time;
        private String address_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public Object getCard() {
            return card;
        }

        public void setCard(Object card) {
            this.card = card;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getMap() {
            return map;
        }

        public void setMap(Object map) {
            this.map = map;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getAddress_id() {
            return address_id;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
        }
    }
}
