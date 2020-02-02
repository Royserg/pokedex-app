package com.example.pokedex.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pokedex.Pokemon;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PokemonAPI {

    private RequestQueue requestQueue;
    public String url = "https://pokeapi.co/api/v2/pokemon?limit=20";

    public PokemonAPI(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void loadPokemon(Response.Listener<JSONObject> listener) {
        if (!url.equals("null")) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("MyApp", "Pokemon List error");
                }
            });

            requestQueue.add(request);
        }
    }
}
