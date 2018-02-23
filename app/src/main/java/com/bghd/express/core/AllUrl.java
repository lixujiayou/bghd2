package com.bghd.express.core;

/**
 * Created by lixu on 2017/12/7.
 */

public class AllUrl {

    public final static String mainUrl = "http://www.baigehuidi.com/Home/jiekou/";
    //登录
    //mobile=18366158972&password=1111111
    public final static String mLoginUrl = "login";
    //模糊查询 order_no
    //获取订单 page=1&size=5&uid=15011&type=down
    public final static String mGetOrderList = "dingdan";


    //    修改通讯录
    //id  通讯录ID
    //    truename  真实姓名
    //    mobile   手机号
    //    address_id  三级联动地址id 一户给你发获取地址的接口
    //    address   详细地址
    public final static String mChangeTell = "delnotes";

    //获取通讯录
    //truename 模糊查询
    //    page
    //    size
    //    uid
    //    type（shipuser|getuser）  寄件人|收件人
    public final static String mGetTellList = "notes";


    //添加通讯录
    //      uid
    //    type   （shipuser|getuser）  寄件人|收件人
    //    truename  真实姓名
    //    mobile   手机号
    //    address_id   获取三级联动的地址
    //base64
    //address
    public final static String addTellList = "addnotes";

    //删除通讯录
    //      id
    public final static String removeTell = "delnotes";

    //轮播图  无参数
    public final static String getShowImgList = "apppic";


    //日结
    //只穿一个uid 就行
    public final static String rijie = "rijie";


    //下单
    /**
     *uid
     *
     *shipuser_address_id   寄件人地址id
     getuser_address_id    收件人地址id
     shipuser_truename   发件人姓名
     shipuser_mobile      发件人手机号
     shipuser_address   发件人详细地址
     getuser_truename  收件人姓名
     getuser_mobile     收件人手机号
     getuser_address    收件人详细地址
     order_price   实付款
     order_weight    重量
     manual    手动录入单号
     express_no    手动录入单号
     */
    public final static String addOrder = "save";

    //获取订单打印状态
    /**
     * uid
     order_no 订单号
     */
    public final static String getPrintStatus = "dayin";


    //省市区
    //id : 上级id   0：省
    public final static String mGetAdressList = "district";

}
