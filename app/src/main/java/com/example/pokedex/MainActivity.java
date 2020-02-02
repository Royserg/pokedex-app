package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Response;
import com.example.pokedex.repository.PokemonAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Pokemon> pokemonList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private PokemonAPI pokemonAPI;

    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokemonAPI = new PokemonAPI(getApplicationContext());

        recyclerView = findViewById(R.id.recycler_view);
        initAdapter();
        loadPokemon();
        initScrollListener();
    }

    private void initAdapter() {
        adapter = new PokedexAdapter(pokemonList);
        recyclerView.setAdapter(adapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == pokemonList.size() - 1) {
                        // It is the bottom of the list
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadPokemon() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    pokemonAPI.url = response.getString("next");

                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);

                        String name = result.getString("name");
                        pokemonList.add(new Pokemon(
                                        name.substring(0,1).toUpperCase() + name.substring(1),
                                        result.getString("url")
                                )
                        );
                    }
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                } catch (JSONException e) {
                    Log.e("MyApp", "Json error", e);
                }
            }
        };

        // Make API call and pass listener
        pokemonAPI.loadPokemon(listener);
    }

    private void loadMore() {
        pokemonList.add(null);
        adapter.notifyItemInserted(pokemonList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pokemonList.remove(pokemonList.size() - 1);
                int scrollPosition = pokemonList.size();
                adapter.notifyItemRemoved(scrollPosition);

                // Loads next batch of pokemons
                loadPokemon();
            }
        }, 2000);
    }
}
