package com.mickeywilliamson.baking;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mickeywilliamson.baking.Models.Recipe;

public class StepDetailActivity extends AppCompatActivity {

    private Recipe mRecipe;
    private int mStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);


        mRecipe = getIntent().getParcelableExtra(Recipe.RECIPE);
        mStep = getIntent().getIntExtra(Recipe.STEP, 0);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        //setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(mRecipe.getSteps().get(mStep).getDescription());

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(Recipe.RECIPE, mRecipe);
            arguments.putInt(Recipe.STEP, getIntent().getIntExtra(Recipe.STEP, 0));
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(Recipe.RECIPE, mRecipe);
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}