package com.bghd.express.room_;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by lixu on 2017/12/1.
 */
@Dao
public interface UserDao {

    /**
     *  Insert
     */
    //插入多条数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)//重复替换
    void insertAll(User... users);

    //插入固定条数数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBothUsers(User user1, User user2);
    //插入固定1条数数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneUser(User user1);

    // ?
    @Insert
    void insertUsersAndFriend(User user, List<User> friends);

    /**
     * update
     */
    @Update
    void updateUsers(User... users);

    /**
     *
     * delete
     */
    /*@Delete()
    void deleteAll();*/

    @Delete
    void delete(User... user);
    @Delete
    void delete(List<User> users);

  /*  @Delete
    void deleteUsers(User... users);*/

    /**
     *
     * query
     */

    //只需要几个字段的查询

   // @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM users")
    List<User> getAll();

    /*@Query("SELECT * FROM users WHERE uid IN (:ids)")
    List<User> loadAllByIds(int[] ids);

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    User fundByName(String first,String last);

    //获取所有年龄大于minAge的用户
    @Query("SELECT * FROM users WHERE age > :minAge")
    List<User> loadAllUsersOlderThan(int minAge);

    //获取所有年龄在minAge和maxAge之间的用户
    @Query("SELECT * FROM users WHERE age BETWEEN :minAge AND maxAge")
    List<User> loadAllUsersBetweenAges(int minAge,int maxAge);

    //查询某n个地区的所有用户查询某两个地区的所有用户
    @Query("SELECT * FROM users WHERE regin in :regions")
    List<User> loadUsersFromRegions(List<String> regions);

    //前面提到了LiveData，可以异步的获取数据，那么我们的Room也是支持异步查询的。
    @Query("SELECT * FROM users WHERE region in :regions")
    LiveData<List<User>> loadUsersFromRegionsSync(List<String> regions);*/





}
