package com.bghd.express.room_;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;


/**
 * Created by lixu on 2017/12/1.
 */
//默认情况下，Room使用类名作为数据库表名。如果你想表都有一个不同的名称，就可以在@Entity中使用tableName参数指定
//防止表有两行包含FirstName和LastName列值相同的一组
//不能使用直接关系，所以就要用到foreignKeys（外键）。

@Entity(tableName = "users"
        ,indices = {@Index(value = {"first_name"},unique = true)})
public class User {

  //  private int uid;

    //和tableName作用类似； @ColumnInfo注解是改变成员变量对应的数据库的字段名称。
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "first_name")
    private String firstName;



    public User(String firstName) {
        this.firstName = firstName;
    }

//    public int getUid() {
//        return uid;
//    }
//
//    public void setUid(int uid) {
//        this.uid = uid;
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }




}
