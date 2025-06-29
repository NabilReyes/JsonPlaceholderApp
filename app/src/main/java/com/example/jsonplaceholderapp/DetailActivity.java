package com.example.jsonplaceholderapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    TextView textTitle, textBody;
    Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textTitle = findViewById(R.id.textTitle);
        textBody = findViewById(R.id.textBody);
        btnSalvar = findViewById(R.id.btnSalvar); // Aún no tiene funcionalidad

        int postId = getIntent().getIntExtra("postId", 1);
        getPostDetail(postId);
    }

    void getPostDetail(int id) {
        new Thread(() -> {
            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/posts/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                JSONObject post = new JSONObject(json.toString());
                String title = post.getString("title");
                String body = post.getString("body");

                runOnUiThread(() -> {
                    textTitle.setText(title);
                    textBody.setText(body);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

