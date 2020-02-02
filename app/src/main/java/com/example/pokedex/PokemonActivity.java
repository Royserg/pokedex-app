package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView numberTextView;
    private TextView type1TextField;
    private TextView type2TextField;
    private String url;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        String name = getIntent().getStringExtra("name");
        url = getIntent().getStringExtra("url");
        nameTextView = findViewById(R.id.pokemon_name);
        nameTextView.setText(name);

        numberTextView = findViewById(R.id.pokemon_number);
        type1TextField = findViewById(R.id.pokemon_type1);
        type2TextField = findViewById(R.id.pokemon_type2);

        load();
    }

    public void load() {
        type1TextField.setText("");
        type2TextField.setText("");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int id = response.getInt("id");
                    numberTextView.setText(String.format("#%03d", id));

                    JSONArray types = response.getJSONArray("types");

                    for (int i = 0; i < types.length(); i++) {
                        JSONObject typeEntry = types.getJSONObject(i);
                        int slot = typeEntry.getInt("slot");
                        String type = typeEntry.getJSONObject("type").getString("name");

                        if (slot == 1) {
                            type1TextField.setText(type);
                        } else if (slot == 2) {
                            type2TextField.setText(type);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("MyApp", "Pokemon Json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyApp", "Pokemon Details error");
            }
        });

        requestQueue.add(request);
    }
}
