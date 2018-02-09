package com.bghd.express.room_;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * Created by lixu on 2017/12/1.
 */
@Database(entities = {User.class},version = 1)
@TypeConverters({Converters.class})   //类型转换
public abstract class AppDatabase extends RoomDatabase{
    public abstract UserDao userDao();

}
