package com.bghd.express.room_;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by lixu on 2017/12/4.
 */

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null? null : new Date(value);
    }


    @TypeConverter
    public static Long dataToTimestamp(Date value){
        return value == null ? null : value.getTime();
    }

}
