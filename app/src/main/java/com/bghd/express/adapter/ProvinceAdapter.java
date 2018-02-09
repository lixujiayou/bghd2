package com.bghd.express.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bghd.express.R;
import com.bghd.express.entiy.AdressEntity;
import com.bghd.express.utils.tools.StringUtils;

import java.util.List;

/**
 * Created by ${wj} on 2015/4/8 0008.
 */
public class ProvinceAdapter extends BaseAdapter implements SectionIndexer {

    private Context mContext;
    private List<AdressEntity.DateBean> list=null;

    /**构造函数*/
    public ProvinceAdapter(Context mContext, List<AdressEntity.DateBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时，调用此方法来更新ListView
     * */
    public void updateListView(List<AdressEntity.DateBean> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        final AdressEntity.DateBean province=list.get(i);
        if(view==null)
        {
            holder=new ViewHolder();
            view=LayoutInflater.from(mContext).inflate(R.layout.item,null);
            holder.tvLetter=  view.findViewById(R.id.catalog);
            holder.tvTitle=  view.findViewById(R.id.title);
            view.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) view.getTag();
        }
        //根据position获取分类的首字母的char ascii值
        int section=getSectionForPosition(i);

        //如果当前位置等于该分类首字母的Char的位置，则认为是第一次出现
        if(i==getPositionForSection(section))
        {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(province.getSortLetters());
        }
        else
        {
            holder.tvLetter.setVisibility(View.GONE);
        }
        if(!StringUtils.isEmpty(list.get(i).getLevel()) && !StringUtils.isEmpty(list.get(i).getProvince())){
            if(list.get(i).getLevel().equals("1")) {
                holder.tvTitle.setText(this.list.get(i).getProvince());
            }else if(list.get(i).getLevel().equals("2")){
                holder.tvTitle.setText(this.list.get(i).getCity());
            }else{
                holder.tvTitle.setText(this.list.get(i).getDistrict());
            }
        }

        return view;
    }

    /**
     * ViewHolder类
     */
    final static class ViewHolder{
        TextView tvLetter;
        TextView tvTitle;
    }

    /**
     * 根据ListView的当前位置获取匪类的首字母的Char ascii值
     * @param position
     * @return
     */
    public int getSectionForPosition(int position){
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     * @param section
     * @return
     */
    public int getPositionForSection(int section){
        for(int i=0;i<getCount();i++){
            String sortStr=list.get(i).getSortLetters();
            char firstChar=sortStr.toUpperCase().charAt(0);
            if(firstChar==section)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替
     * @param str
     * @return
     */
    private String getAlpha(String str){
        String sortStr=str.trim().substring(0,1).toUpperCase();
        //正则表达式，判断首字母是否是英文字母
        if(sortStr.matches("[A-Z]"))
        {
            return sortStr;
        }
        else
        {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
