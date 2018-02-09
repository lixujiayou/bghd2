package com.bghd.express.adapter;

import android.support.annotation.Nullable;

import com.bghd.express.R;
import com.bghd.express.entiy.OrderListEntity;
import com.bghd.express.utils.tools.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lixu on 2018/2/6.
 */

public class OrderListAdapter extends BaseQuickAdapter<OrderListEntity.DataBean,BaseViewHolder>{
    public OrderListAdapter(int layoutResId, @Nullable List<OrderListEntity.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListEntity.DataBean item) {
        if(!StringUtils.isEmpty(item.getShipuser_truename())){
            helper.setText(R.id.tv_item_send_user, item.getShipuser_truename());
        }

        if(!StringUtils.isEmpty(item.getShipuser_mobile())) {
            helper.setText(R.id.tv_item_send_user_phone, item.getShipuser_mobile());
        }
        if(!StringUtils.isEmpty(item.getShipuser_province()) && !StringUtils.isEmpty(item.getShipuser_city()) && !StringUtils.isEmpty(item.getShipuser_district()) && !StringUtils.isEmpty(item.getShipuser_address())) {
            helper.setText(R.id.tv_item_send_user_adress, item.getShipuser_province()
                    + item.getShipuser_city()
                    + item.getShipuser_district()
                    + item.getShipuser_address());
        }

        if(!StringUtils.isEmpty(item.getGetuser_truename())) {
            helper.setText(R.id.tv_item_accept_user, item.getGetuser_truename());
        }

        if(!StringUtils.isEmpty(item.getGetuser_mobile())) {
            helper.setText(R.id.tv_item_accept_user_phone, item.getGetuser_mobile());
        }
        if(!StringUtils.isEmpty(item.getGetuser_province()) && !StringUtils.isEmpty(item.getGetuser_city()) && !StringUtils.isEmpty(item.getGetuser_district()) && !StringUtils.isEmpty(item.getGetuser_address())) {
            helper.setText(R.id.tv_item_accept_user_adress, item.getGetuser_province()
                    + item.getGetuser_city()
                    + item.getGetuser_district()
                    + item.getGetuser_address());
        }

        if(!StringUtils.isEmpty(item.getExpress_no())) {
            helper.setText(R.id.tv_item_order_code, item.getExpress_no());
        }
        if(!StringUtils.isEmpty(item.getIs_print())) {
            if (item.getIs_print().equals("1")) {
                helper.setVisible(R.id.iv_item_isprint, true);
            } else {
                helper.setVisible(R.id.iv_item_isprint, false);
            }
        }

    }
}
