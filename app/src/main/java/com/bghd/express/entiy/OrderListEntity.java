package com.bghd.express.entiy;

import java.util.List;

/**
 * Created by lixu on 2018/2/6.
 */

public class OrderListEntity {

    /**
     * status : 1
     * data : [{"id":"52387","is_print":"1","order_no":"998093937569","number":"0","shipuser_truename":"樊兆焕","shipuser_mobile":"18754176598","shipuser_province":"山东省","shipuser_city":"济南市","shipuser_district":"历城区","shipuser_address":"三威大厦2503","getuser_truename":"测试","getuser_mobile":"15100000000","getuser_province":"山东省","getuser_city":"济南市","getuser_district":"历城区","getuser_address":"三威大厦","express_no":"998093937569","one":"胜己出用心传递","two":"千万里珍惜所托","datou":"鲁 济南"},{"id":"51975","is_print":"1","order_no":"998093937542","number":"0","shipuser_truename":"汤红","shipuser_mobile":"15318826255","shipuser_province":"山东省","shipuser_city":"济南市","shipuser_district":"历城区","shipuser_address":"三威大厦","getuser_truename":"张腾","getuser_mobile":"18103391309","getuser_province":"河北省","getuser_city":"邢台市","getuser_district":"清河县","getuser_address":"火车站附近","express_no":"998093937542","one":"胜己出用心传递","two":"千万里珍惜所托","datou":"冀 清河"},{"id":"51749","is_print":"1","order_no":"998093937541","number":"0","shipuser_truename":"李敏","shipuser_mobile":"13356660753","shipuser_province":"山东省","shipuser_city":"济南市","shipuser_district":"历城区","shipuser_address":"山东省济南市历城区二环东路胶东水饺","getuser_truename":"湖南宏弘道图书","getuser_mobile":"0731-88931252","getuser_province":"湖南省","getuser_city":"长沙市","getuser_district":"望城区","getuser_address":"湖南省长沙市望城区白沙洲普瑞大道金桥永旺仓储物流园15栋弘道仓库","express_no":"998093937541","one":"胜己出用心传递","two":"千万里珍惜所托","datou":"湘 长沙"},{"id":"51707","is_print":"1","order_no":"998093937538","number":"0","shipuser_truename":"李庆阁","shipuser_mobile":"053188826390","shipuser_province":"山东省","shipuser_city":"济南市","shipuser_district":"历城区","shipuser_address":"山东省济南市历城区华龙路三威大厦","getuser_truename":"张艳婷","getuser_mobile":"13332622537","getuser_province":"广东省","getuser_city":"东莞市","getuser_district":"县区","getuser_address":"广东省东莞市塘厦镇平山区188工业大道26号（中控智慧制造中心）","express_no":"998093937538","one":"胜己出用心传递","two":"千万里珍惜所托","datou":" 粤 东莞"},{"id":"51705","is_print":"1","order_no":"998093937537","number":"0","shipuser_truename":"李庆阁","shipuser_mobile":"053188821390","shipuser_province":"山东省","shipuser_city":"济南市","shipuser_district":"历城区","shipuser_address":"山东省济南市历城区华龙路三威大厦","getuser_truename":"曾志祥","getuser_mobile":"021-58215233-83","getuser_province":"上海市","getuser_city":"市辖区","getuser_district":"浦东新区","getuser_address":"上海市浦东新区杨高南路1685号3A层B103/B105","express_no":"998093937537","one":"胜己出用心传递","two":"千万里珍惜所托","datou":"沪 浦东"}]
     */

    private int status;
    private List<DataBean> data;

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

    public static class DataBean {
        /**
         * id : 52387
         * is_print : 1
         * order_no : 998093937569
         * number : 0
         * shipuser_truename : 樊兆焕
         * shipuser_mobile : 18754176598
         * shipuser_province : 山东省
         * shipuser_city : 济南市
         * shipuser_district : 历城区
         * shipuser_address : 三威大厦2503
         * getuser_truename : 测试
         * getuser_mobile : 15100000000
         * getuser_province : 山东省
         * getuser_city : 济南市
         * getuser_district : 历城区
         * getuser_address : 三威大厦
         * express_no : 998093937569
         * one : 胜己出用心传递
         * two : 千万里珍惜所托
         * datou : 鲁 济南
         */

        private String id;
        private String is_print;
        private String order_no;
        private String number;
        private String shipuser_truename;
        private String shipuser_mobile;
        private String shipuser_province;
        private String shipuser_city;
        private String shipuser_district;
        private String shipuser_address;
        private String getuser_truename;
        private String getuser_mobile;
        private String getuser_province;
        private String getuser_city;
        private String getuser_district;
        private String getuser_address;
        private String express_no;
        private String one;
        private String two;
        private String datou;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_print() {
            return is_print;
        }

        public void setIs_print(String is_print) {
            this.is_print = is_print;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getShipuser_truename() {
            return shipuser_truename;
        }

        public void setShipuser_truename(String shipuser_truename) {
            this.shipuser_truename = shipuser_truename;
        }

        public String getShipuser_mobile() {
            return shipuser_mobile;
        }

        public void setShipuser_mobile(String shipuser_mobile) {
            this.shipuser_mobile = shipuser_mobile;
        }

        public String getShipuser_province() {
            return shipuser_province;
        }

        public void setShipuser_province(String shipuser_province) {
            this.shipuser_province = shipuser_province;
        }

        public String getShipuser_city() {
            return shipuser_city;
        }

        public void setShipuser_city(String shipuser_city) {
            this.shipuser_city = shipuser_city;
        }

        public String getShipuser_district() {
            return shipuser_district;
        }

        public void setShipuser_district(String shipuser_district) {
            this.shipuser_district = shipuser_district;
        }

        public String getShipuser_address() {
            return shipuser_address;
        }

        public void setShipuser_address(String shipuser_address) {
            this.shipuser_address = shipuser_address;
        }

        public String getGetuser_truename() {
            return getuser_truename;
        }

        public void setGetuser_truename(String getuser_truename) {
            this.getuser_truename = getuser_truename;
        }

        public String getGetuser_mobile() {
            return getuser_mobile;
        }

        public void setGetuser_mobile(String getuser_mobile) {
            this.getuser_mobile = getuser_mobile;
        }

        public String getGetuser_province() {
            return getuser_province;
        }

        public void setGetuser_province(String getuser_province) {
            this.getuser_province = getuser_province;
        }

        public String getGetuser_city() {
            return getuser_city;
        }

        public void setGetuser_city(String getuser_city) {
            this.getuser_city = getuser_city;
        }

        public String getGetuser_district() {
            return getuser_district;
        }

        public void setGetuser_district(String getuser_district) {
            this.getuser_district = getuser_district;
        }

        public String getGetuser_address() {
            return getuser_address;
        }

        public void setGetuser_address(String getuser_address) {
            this.getuser_address = getuser_address;
        }

        public String getExpress_no() {
            return express_no;
        }

        public void setExpress_no(String express_no) {
            this.express_no = express_no;
        }

        public String getOne() {
            return one;
        }

        public void setOne(String one) {
            this.one = one;
        }

        public String getTwo() {
            return two;
        }

        public void setTwo(String two) {
            this.two = two;
        }

        public String getDatou() {
            return datou;
        }

        public void setDatou(String datou) {
            this.datou = datou;
        }
    }
}
