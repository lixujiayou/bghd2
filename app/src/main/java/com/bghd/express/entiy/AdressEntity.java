package com.bghd.express.entiy;

import com.bghd.express.utils.base.BaseEntity;

import java.util.List;

/**
 * Created by lixu on 2018/2/8.
 */

public class AdressEntity {
    //level 1:省province 2：市city  3：区district
    /**
     * status : 1
     * date : [{"id":"110000","pid":"0","title":"北京市","level":"1","province":"北京市","city":"","district":"","tree":"110000","area":"北京市,,","datou":null,"aa":1},{"id":"120000","pid":"0","title":"天津市","level":"1","province":"天津市","city":"","district":"","tree":"120000","area":"天津市,,","datou":null,"aa":1},{"id":"130000","pid":"0","title":"河北省","level":"1","province":"河北省","city":"","district":"","tree":"130000","area":"河北省,,","datou":" ","aa":1},{"id":"140000","pid":"0","title":"山西省","level":"1","province":"山西省","city":"","district":"","tree":"140000","area":"山西省,,","datou":null,"aa":1},{"id":"150000","pid":"0","title":"内蒙古自治区","level":"1","province":"内蒙古自治区","city":"","district":"","tree":"150000","area":"内蒙古自治区,,","datou":null,"aa":1},{"id":"210000","pid":"0","title":"辽宁省","level":"1","province":"辽宁省","city":"","district":"","tree":"210000","area":"辽宁省,,","datou":null,"aa":1},{"id":"220000","pid":"0","title":"吉林省","level":"1","province":"吉林省","city":"","district":"","tree":"220000","area":"吉林省,,","datou":null,"aa":1},{"id":"230000","pid":"0","title":"黑龙江省","level":"1","province":"黑龙江省","city":"","district":"","tree":"230000","area":"黑龙江省,,","datou":null,"aa":1},{"id":"310000","pid":"0","title":"上海市","level":"1","province":"上海市","city":"","district":"","tree":"310000","area":"上海市,,","datou":null,"aa":1},{"id":"320000","pid":"0","title":"江苏省","level":"1","province":"江苏省","city":"","district":"","tree":"320000","area":"江苏省,,","datou":null,"aa":1},{"id":"330000","pid":"0","title":"浙江省","level":"1","province":"浙江省","city":"","district":"","tree":"330000","area":"浙江省,,","datou":" ","aa":1},{"id":"340000","pid":"0","title":"安徽省","level":"1","province":"安徽省","city":"","district":"","tree":"340000","area":"安徽省,,","datou":" ","aa":1},{"id":"350000","pid":"0","title":"福建省","level":"1","province":"福建省","city":"","district":"","tree":"350000","area":"福建省,,","datou":" ","aa":1},{"id":"360000","pid":"0","title":"江西省","level":"1","province":"江西省","city":"","district":"","tree":"360000","area":"江西省,,","datou":" ","aa":1},{"id":"370000","pid":"0","title":"山东省","level":"1","province":"山东省","city":"","district":"","tree":"370000","area":"山东省,,","datou":" ","aa":1},{"id":"410000","pid":"0","title":"河南省","level":"1","province":"河南省","city":"","district":"","tree":"410000","area":"河南省,,","datou":" ","aa":1},{"id":"420000","pid":"0","title":"湖北省","level":"1","province":"湖北省","city":"","district":"","tree":"420000","area":"湖北省,,","datou":" ","aa":1},{"id":"430000","pid":"0","title":"湖南省","level":"1","province":"湖南省","city":"","district":"","tree":"430000","area":"湖南省,,","datou":" ","aa":1},{"id":"440000","pid":"0","title":"广东省","level":"1","province":"广东省","city":"","district":"","tree":"440000","area":"广东省,,","datou":" ","aa":1},{"id":"450000","pid":"0","title":"广西壮族自治区","level":"1","province":"广西壮族自治区","city":"","district":"","tree":"450000","area":"广西壮族自治区,,","datou":" ","aa":1},{"id":"460000","pid":"0","title":"海南省","level":"1","province":"海南省","city":"","district":"","tree":"460000","area":"海南省,,","datou":" ","aa":1},{"id":"500000","pid":"0","title":"重庆市","level":"1","province":"重庆市","city":"","district":"","tree":"500000","area":"重庆市,,","datou":" ","aa":1},{"id":"510000","pid":"0","title":"四川省","level":"1","province":"四川省","city":"","district":"","tree":"510000","area":"四川省,,","datou":" ","aa":1},{"id":"520000","pid":"0","title":"贵州省","level":"1","province":"贵州省","city":"","district":"","tree":"520000","area":"贵州省,,","datou":" ","aa":1},{"id":"530000","pid":"0","title":"云南省","level":"1","province":"云南省","city":"","district":"","tree":"530000","area":"云南省,,","datou":" ","aa":1},{"id":"540000","pid":"0","title":"西藏自治区","level":"1","province":"西藏自治区","city":"","district":"","tree":"540000","area":"西藏自治区,,","datou":" ","aa":1},{"id":"610000","pid":"0","title":"陕西省","level":"1","province":"陕西省","city":"","district":"","tree":"610000","area":"陕西省,,","datou":" ","aa":1},{"id":"620000","pid":"0","title":"甘肃省","level":"1","province":"甘肃省","city":"","district":"","tree":"620000","area":"甘肃省,,","datou":" ","aa":1},{"id":"630000","pid":"0","title":"青海省","level":"1","province":"青海省","city":"","district":"","tree":"630000","area":"青海省,,","datou":" ","aa":1},{"id":"640000","pid":"0","title":"宁夏回族自治区","level":"1","province":"宁夏回族自治区","city":"","district":"","tree":"640000","area":"宁夏回族自治区,,","datou":" ","aa":1},{"id":"650000","pid":"0","title":"新疆维吾尔自治区","level":"1","province":"新疆维吾尔自治区","city":"","district":"","tree":"650000","area":"新疆维吾尔自治区,,","datou":" ","aa":1},{"id":"710000","pid":"0","title":"台湾省","level":"1","province":"台湾省","city":"","district":"","tree":"710000","area":"台湾省,,","datou":" ","aa":0},{"id":"810000","pid":"0","title":"香港特别行政区","level":"1","province":"香港特别行政区","city":"","district":"","tree":"810000","area":"香港特别行政区,,","datou":" ","aa":0},{"id":"820000","pid":"0","title":"澳门特别行政区","level":"1","province":"澳门特别行政区","city":"","district":"","tree":"820000","area":"澳门特别行政区,,","datou":" ","aa":0}]
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

    public static class DateBean extends BaseEntity{
        /**
         * id : 110000
         * pid : 0
         * title : 北京市
         * level : 1
         * province : 北京市
         * city :
         * district :
         * tree : 110000
         * area : 北京市,,
         * datou : null
         * aa : 1
         */

        private String id;
        private String pid;
        private String title;
        private String level;
        private String province;
        private String city;
        private String district;
        private String tree;
        private String area;
        private Object datou;
        private int aa;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
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

        public String getTree() {
            return tree;
        }

        public void setTree(String tree) {
            this.tree = tree;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public Object getDatou() {
            return datou;
        }

        public void setDatou(Object datou) {
            this.datou = datou;
        }

        public int getAa() {
            return aa;
        }

        public void setAa(int aa) {
            this.aa = aa;
        }
    }
}
