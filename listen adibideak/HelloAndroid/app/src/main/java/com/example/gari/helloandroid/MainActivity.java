package com.example.gari.helloandroid;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "ListView Title 1", "ListView Title 2", "ListView Title 3", "ListView Title 4",
            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
    };


    int[] listviewImage = new int[]{
            R.drawable.app_icon, R.drawable.el_tapon, R.drawable.favourite, R.drawable.golf_campo,
            R.drawable.home, R.drawable.log_out, R.drawable.nobody, R.drawable.pelota_golf,
    };

    int[] listviewLike = new int[]{
            R.drawable.like, R.drawable.like, R.drawable.like, R.drawable.like,
            R.drawable.like, R.drawable.like, R.drawable.like, R.drawable.like,
    };

    String[] listviewShortDescription = new String[]{
            "Android ListView Short Description brgfe gtbfe tec tge tgec tgecd ytge tgd gtfre", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_with_image_and_text);

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_discription", listviewShortDescription[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            hm.put("listview_like", Integer.toString(listviewLike[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_discription", "listview_like"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description, R.id.listview_imageeeeee};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);
    }
}