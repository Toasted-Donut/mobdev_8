package com.example.mobdev8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    UserDao mUserDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("gg", getFilesDir().toString());
        Log.i("gg", getExternalFilesDir(null).toString());
        File file1 = new File(getFilesDir(),"donut.txt");
        File file2 = new File(getExternalFilesDir(null), "donut.txt");
        String text = "Donuts are great!";

        //internal
        try (FileOutputStream fos = openFileOutput(file1.getName(),MODE_PRIVATE)) {
            fos.write(text.getBytes());
            if(file1.exists()){
                Log.i("gg","internal file exists");
            }
        } catch (Exception ex) {
            Log.e("gg", ex.toString());
        }
        //external
        try (FileOutputStream fos = new FileOutputStream(file2)) {
            fos.write(text.getBytes());
        } catch (Exception ex) {
            Log.e("gg", ex.toString());
        }
        //Shared Preferences
        //TBD

        //DataBase
        AppDatabase db = Room.databaseBuilder(MainActivity.this,AppDatabase.class,"donuts-base").build();
        mUserDao = db.userDao();
        new AddUserAsync().execute("new","user","18");
    }


//    class DataAccess extends AsyncTask<Void,Void,Void>{
//        @Override
//        protected Void doInBackground(Void... voids) {
//            List<User> users = mUserDao.getAll();
//            Log.i("gg",String.valueOf(users.size()));
//            mUserDao.insertAll(new User(1,"Nikita","Egorov",19));
//            mUserDao.insertAll(new User(2,"Andrey","GigaChad",19));
//            mUserDao.insertAll(new User(3,"Vlad","Sigma",20));
//            //users = userDao.getAll();
//            Log.i("gg",String.valueOf(users.size()));
//            return null;
//        }
//    }

    //using AsyncTask for database interaction
    class AddUserAsync extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            mUserDao.insertAll(new User(mUserDao.getAll().size()+1,strings[0],strings[1],Integer.parseInt(strings[2])));
            Log.i("gg","current size: " + mUserDao.getAll().size());
            return null;
        }
    }
}
