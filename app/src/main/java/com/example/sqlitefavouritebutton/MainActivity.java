package com.example.sqlitefavouritebutton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ListView listView;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // পরিচয় করিয়ে দেওয়া হয়েছে ।
        listView = findViewById(R.id.listView);

        // databaseHelper কে পরিচয় করিয়ে দেওয়া হয়েছে এবং data get করা হয়েছে ।
        databaseHelper = new DatabaseHelper(MainActivity.this);
         Cursor cursor = databaseHelper.getUsersData();
        if (cursor != null){
            getData();
        }


        // Adapter কে পরিচয় করিয়ে দেওয়া হয়েছে এবং List এর মধ্যে set করে দেওয়া হয়েছে ।
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);


        // fab add এ ক্লিক করা হয়েছে ।
        findViewById(R.id.fabAdd).setOnClickListener(v -> {
            showDialogBox();
        });


        // fabFavourite এ ক্লিক করা হয়েছে ।
        findViewById(R.id.fabFavourite).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
        });

    } // onCreate method end here ==============================

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View myView = layoutInflater.inflate(R.layout.list_item, parent, false);

            // পরিচয় করিয়ে দেওয়া হয়েছে ।
            TextView tvName = myView.findViewById(R.id.tvName);
            TextView tvNumber = myView.findViewById(R.id.tvNumber);
            ImageView imgFavourite = myView.findViewById(R.id.imgFavourite);
            ImageView imgDelete = myView.findViewById(R.id.imgDelete);

            HashMap<String, String> myHashmap = arrayList.get(position);
            String id = myHashmap.get("id");
            String name = myHashmap.get("name");
            String number = myHashmap.get("number");
            String isFavourite = myHashmap.get("isFavourite");

            tvName.setText(name);
            tvNumber.setText(number);

            if (isFavourite.equals("0")) {
                imgFavourite.setImageResource(R.drawable.ic_favorite);
            } else {
                imgFavourite.setImageResource(R.drawable.ic_fill_favorite);
            }

            // Favourite button এর onClick লেখা হয়েছে ।
            imgFavourite.setOnClickListener(v -> {
                if (isFavourite.equals("0")) {
                    databaseHelper.updateAddFavourite(Integer.parseInt(id));
                    imgFavourite.setImageResource(R.drawable.ic_fill_favorite);
                    getData();
                    notifyDataSetChanged();
                } else {
                    databaseHelper.updateRemoveFavourite(Integer.parseInt(id));
                    imgFavourite.setImageResource(R.drawable.ic_favorite);
                    getData();
                    notifyDataSetChanged();
                }
            });

            // Delete button এর onClick লেখা হয়েছে ।
            imgDelete.setOnClickListener(v -> {
                databaseHelper.deleteItem(id);
                getData();
                notifyDataSetChanged();
            });


            return myView;
        }
    } // BaseAdapter end here =====================

    private void showDialogBox() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.add_user_layout, null);
        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);

        EditText edName = mView.findViewById(R.id.edName);
        EditText edNumber = mView.findViewById(R.id.edNumber);

        mView.findViewById(R.id.btnInsert).setOnClickListener(v -> {

            // ইউজার থেকে data get করা হয়েছে ।
            String name = edName.getText().toString();
            String number = edNumber.getText().toString();

            // validation দেওয়া হয়েছে ।
            if (name.isEmpty()) {
                edName.setError("Enter Your Name");
                return;
            } else if (number.isEmpty()) {
                edNumber.setError("Enter Your Number");
                return;
            } else {
                boolean isDataInsert = databaseHelper.insertData(name, number);
                if (isDataInsert) {
                    Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
                    getData();
                    myAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(this, "Data not Inserted", Toast.LENGTH_SHORT).show();
                }
            }

        });

        mView.findViewById(R.id.btnDialogClose).setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    } // ShowDialogBox end here ================

    private void getData() {
        Cursor cursor = databaseHelper.getUsersData();
        arrayList.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String number = cursor.getString(2);
                int isFavourite = cursor.getInt(3);

                hashMap = new HashMap<>();
                hashMap.put("id", "" + id);
                hashMap.put("name", name);
                hashMap.put("number", number);
                hashMap.put("isFavourite", "" + isFavourite);
                arrayList.add(hashMap);
            }
        }
    }

    @Override
    protected void onResume() {
        getData();
        myAdapter.notifyDataSetChanged();
        super.onResume();

    }


} // public class end here ====================================