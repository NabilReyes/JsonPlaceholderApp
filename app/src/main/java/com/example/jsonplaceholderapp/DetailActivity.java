package com.example.jsonplaceholderapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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

        // Enlazar vistas
        textTitle = findViewById(R.id.textTitle);
        textBody = findViewById(R.id.textBody);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Obtener el ID del post desde el intent
        int postId = getIntent().getIntExtra("postId", 1);

        // Cargar detalles del post
        getPostDetail(postId);

        // Acción del botón salvar
        btnSalvar.setOnClickListener(v -> {
            String titulo = textTitle.getText().toString();
            String cuerpo = textBody.getText().toString();

            // Aquí solo muestra mensaje, pero podrías guardarlo
            Toast.makeText(this, "Post guardado:\n" + titulo, Toast.LENGTH_SHORT).show();
        });
    }

    void getPostDetail(int id) {
        new Thread(() -> {
            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/posts/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.connect();

                if (conn.getResponseCode() == 200) {
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

                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Error al obtener el post", Toast.LENGTH_SHORT).show()
                    );
                }

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
                e.printStackTrace();
            }
        }).start();
    }
}

