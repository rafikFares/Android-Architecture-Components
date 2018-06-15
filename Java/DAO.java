package com.example.XXX.myroomwithrxjava;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface DAO {

    @Query("SELECT * FROM users")
    Maybe<List<User>> getAll();

    @Query("SELECT * FROM users where name LIKE  :userName")
    Maybe<User> findByName(String userName);

    @Query("SELECT COUNT(*) from users")
    Single<Integer> countUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users where name LIKE  :userName")
    void deleteByName(String userName);

    @Query("DELETE FROM users")
    void deleteAllUsers();
}
