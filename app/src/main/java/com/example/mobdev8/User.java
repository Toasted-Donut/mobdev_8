package com.example.mobdev8;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastname;

    @ColumnInfo(name = "age")
    public int age;

    public User(int uid, String firstName, String lastname, int age){
        this.uid = uid;
        this.firstName = firstName;
        this.lastname = lastname;
        this.age = age;
    }
}
