package com.manitos.dev.gilinhobakingapp.features.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.manitos.dev.gilinhobakingapp.R;
import com.manitos.dev.gilinhobakingapp.api.models.Bake;
import com.manitos.dev.gilinhobakingapp.features.MainViewModel;

import java.util.List;

public class BakeListActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;

    public static final String BAKE_KEY = "Bake.List.Activity.bake.item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bake_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        getBakesObserver();

        observeConnection();
        observeDataError();
    }

    private void getBakesObserver() {
        mainViewModel.getBakes().observe(this, new Observer<List<Bake>>() {
            @Override
            public void onChanged(List<Bake> bakes) {
                View recyclerView = findViewById(R.id.recycler_bake_list);
                setupRecyclerView((RecyclerView) recyclerView, bakes);
            }
        });
    }

    private void observeConnection() {
        mainViewModel.getHasInternet().observe(this, hasInternet -> {
            ProgressBar pg = (ProgressBar) findViewById(R.id.progressBar);
            pg.setVisibility(View.GONE);

            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setVisibility(!hasInternet ? View.VISIBLE : View.GONE);
            tv.setText(R.string.no_internet_connection);
        });
    }

    private void observeDataError() {
        mainViewModel.getHasError().observe(this, hasError -> {
            ProgressBar pg = (ProgressBar) findViewById(R.id.progressBar);
            pg.setVisibility(View.GONE);

            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setVisibility(hasError ? View.VISIBLE : View.GONE);
            tv.setText(R.string.error_to_load_bakes);
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Bake> bakes) {
        int totalCol = this.getResources().getInteger(R.integer.bake_list_num_col);
        RecyclerView.LayoutManager lLayout = new GridLayoutManager(this, totalCol);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(new BakeListAdapter(bakes));
    }
}
