package com.manitos.dev.gilinhobakingapp.features.bakerecipe;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.manitos.dev.gilinhobakingapp.api.models.Bake;
import com.manitos.dev.gilinhobakingapp.api.models.Step;
import com.manitos.dev.gilinhobakingapp.features.bakedetail.BakeDetailActivity;
import com.manitos.dev.gilinhobakingapp.features.components.BakeIngredientsFragment;
import com.manitos.dev.gilinhobakingapp.R;
import com.manitos.dev.gilinhobakingapp.features.home.BakeListActivity;

import java.util.ArrayList;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BakeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BakeRecipeActivity extends AppCompatActivity {

    public static final String KEY_INGREDIENTS = "BakeRecipeActivity.ingredients";
    public static final String KEY_STEP = "BakeRecipeActivity.step";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private Bake _bakeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bake_recipe_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        Intent intentBakeRecipe = getIntent();
        if (intentBakeRecipe != null) {
            _bakeItem = (Bake) intentBakeRecipe.getSerializableExtra(BakeListActivity.BAKE_KEY);
        } else  {
            throw new IllegalStateException("the bake object shouldn't be null");
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BakeListActivity.BAKE_KEY, _bakeItem);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        ArrayList<BakeUiItems> items = new ArrayList<>(_bakeItem.getSteps().size() + 1);
        items.add(new BakeUiItems("Ingredients", _bakeItem.getIngredients(), null));
        for (Step step : _bakeItem.getSteps()) {
            items.add(new BakeUiItems(step.getShortDescription(), null, step));
        }

        recyclerView.setAdapter(new BakeRecipeAdapter(this, items, mTwoPane));

        if (mTwoPane) {
            initializeIntroIfIsMaster(items.get(0));
        }
    }

    private void initializeIntroIfIsMaster(BakeUiItems items) {
        Fragment fragment = BakeIngredientsFragment.newInstance(items);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();
    }
}
