package com.bghd.express.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.bghd.express.R;
import com.bghd.express.entiy.OrderListEntity;
import com.bghd.express.entiy.TellEntity;
import com.bghd.express.utils.tools.StringUtils;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lixu on 2018/2/6.
 */

public class TellListAdapter extends BaseItemDraggableAdapter<TellEntity.DateBean,BaseViewHolder> {

    private String mType;
    private Context mContext;


    /**
     *
     * @param layoutResId
     * @param data
     * @param type 0:寄件  1：收件
     */
    public TellListAdapter(Context context, int layoutResId, @Nullable List<TellEntity.DateBean> data,String type) {
        super(layoutResId, data);
        this.mType = type;
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TellEntity.DateBean item) {
        if(!StringUtils.isEmpty(item.getTruename())) {
            helper.setText(R.id.tv_item_send_user, item.getTruename());
        }
        if(!StringUtils.isEmpty(item.getMobile())) {
            helper.setText(R.id.tv_item_send_user_phone, item.getMobile());
        }
        if(!StringUtils.isEmpty(item.getCountry())) {
            helper.setText(R.id.tv_item_send_user_adress, item.getCountry()
                    + item.getProvince()
                    + item.getCity()
                    + item.getDistrict()
                    + item.getAddress());
        }

        if(mType.equals("1")){
            helper.setImageDrawable(R.id.iv_layout_icon, ContextCompat.getDrawable(mContext,R.drawable.icon_pickup));
            helper.setText(R.id.tv_tag_text,"收件人");
        }
    }
}
