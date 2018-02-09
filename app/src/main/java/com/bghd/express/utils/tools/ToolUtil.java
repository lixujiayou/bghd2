package com.bghd.express.utils.tools;

import java.util.List;

/**
 * Created by lixu on 2017/5/12.
 * 集合数组数据工具类
 */

public class ToolUtil {
    public static boolean isEmpty(List strList){
        if(strList != null && strList.size() != 0){
            return false;
        }
        return true;
    }
}
