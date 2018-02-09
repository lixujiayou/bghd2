package com.bghd.express.adapter;

import android.support.annotation.Nullable;

import com.bghd.express.R;
import com.bghd.express.entiy.OrderListEntity;
import com.bghd.express.room_.User;
import com.bghd.express.utils.tools.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lixu on 2018/2/6.
 * 搜索历史记录adapter
 */

public class SearchHistoryAdapter extends BaseQuickAdapter<User,BaseViewHolder>{
    public SearchHistoryAdapter(int layoutResId, @Nullable List<User> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        if(!StringUtils.isEmpty(item.getFirstName())) {
            helper.setText(R.id.tv_tag, item.getFirstName());
        }
    }
}
