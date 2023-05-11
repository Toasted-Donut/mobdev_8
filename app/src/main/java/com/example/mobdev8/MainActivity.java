package com.example.mobdev8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mobdev8.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    UserDao mUserDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(this.getLayoutInflater());
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
        AppDatabase db = Room.databaseBuilder(MainActivity.this,AppDatabase.class,"donutsBase").build();
        mUserDao = db.userDao();
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new addUserAsync().execute(binding.name.getText().toString(),binding.lastname.getText().toString(),binding.age.getText().toString());
                (Toast.makeText(MainActivity.this,"user added",Toast.LENGTH_SHORT)).show();
            }
        });
        binding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    List<String> users = new loadNamesOfUsersOlderThanAsync().execute(0).get();
                    (Toast.makeText(MainActivity.this,String.join("\n",users),Toast.LENGTH_LONG)).show();
                } catch (Exception e) {
                    Log.e("gg",e.toString());
                }
            }
        });
        binding.btnOlder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    List<String> users = new loadNamesOfUsersOlderThanAsync().execute(20).get();
                    (Toast.makeText(MainActivity.this,"старше 20:\n"+String.join("\n",users),Toast.LENGTH_LONG)).show();
                } catch (Exception e) {
                    Log.e("gg",e.toString());
                }
            }
        });


        setContentView(binding.getRoot());
    }




    //using AsyncTask for database interaction
    class addUserAsync extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            mUserDao.insertAll(new User(mUserDao.getAll().size(),strings[0],strings[1],Integer.parseInt(strings[2])));
            Log.i("gg","current size: " + mUserDao.getAll().size());
            return null;
        }
    }
    class loadAllByIdsAsync extends AsyncTask<Integer,Void,List<User>>{
        @Override
        protected List<User> doInBackground(Integer... integers) {
            return mUserDao.loadAllByIds(Arrays.stream(integers).mapToInt(Integer::intValue).toArray());
        }
    }

    class findByNameAsync extends AsyncTask<String,Void,User>{
        @Override
        protected User doInBackground(String... strings) {
            return mUserDao.findByName(strings[0],strings[1]);
        }
    }

    class loadNamesOfUsersOlderThanAsync extends AsyncTask<Integer,Void,List<String>>{
        @Override
        protected List<String> doInBackground(Integer... integers) {
            return mUserDao.loadNamesOfUsersOlderThan(integers[0]);
        }
    }

}
