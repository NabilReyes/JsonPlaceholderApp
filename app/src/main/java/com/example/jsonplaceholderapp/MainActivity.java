package com.example.jsonplaceholderapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import org.json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<Integer> postIds = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewPosts);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(adapter);

        getPosts();

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            int postId = postIds.get(position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("postId", postId);
            startActivity(intent);
        });
    }

    void getPosts() {
        new Thread(() -> {
            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/posts");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                JSONArray posts = new JSONArray(json.toString());
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.getJSONObject(i);
                    int id = post.getInt("id");
                    titles.add("Post #" + id);  // ✅ Aquí se muestra solo "Post #1", "Post #2", etc.
                    postIds.add(id);
                }

                runOnUiThread(() -> adapter.notifyDataSetChanged());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
