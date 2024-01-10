package com.example.sqlitefavouritebutton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class FavouriteActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    ListView listView;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);


        // পরিচয় করিয়ে দেওয়া হয়েছে ।
        Toolbar toolbar = findViewById(R.id.toolbar);
        listView = findViewById(R.id.listView);

        // পরিচয় করিয়ে দেওয়া হয়েছে ।
        databaseHelper = new DatabaseHelper(FavouriteActivity.this);
        getData();

        //======================================
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //======================================

        // Adapter কে পরিচয় করিয়ে দেওয়া হয়েছে এবং listView তে set করে দেওয়া হয়েছে ।
        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);


    } // onCreate method end here ===============

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
            imgDelete.setVisibility(View.GONE);

            if (isFavourite.equals("0")) {
                imgFavourite.setImageResource(R.drawable.ic_favorite);
            } else {
                imgFavourite.setImageResource(R.drawable.ic_fill_favorite);
            }

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

            return myView;
        }
    }


    private void getData() {
        Cursor cursor = databaseHelper.getUsersData();
        arrayList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String number = cursor.getString(2);
            int isFavourite = cursor.getInt(3);

            if (isFavourite == 1) {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


} // public class end here ======================