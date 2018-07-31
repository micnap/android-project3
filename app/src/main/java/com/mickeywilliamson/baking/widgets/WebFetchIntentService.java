package com.mickeywilliamson.baking.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mickeywilliamson.baking.data.RequestInterface;
import com.mickeywilliamson.baking.models.Ingredient;
import com.mickeywilliamson.baking.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebFetchIntentService extends IntentService {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public static ArrayList<Ingredient> sIngredients;

    public WebFetchIntentService() {
        super("WebFetchIntentService");
    }

    public WebFetchIntentService(String name, int appWidgetId) {
        super(name);
        this.appWidgetId = appWidgetId;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Recipe.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<ArrayList<Recipe>> call = request.getJSON();
        call.enqueue(new Callback<ArrayList<Recipe>>() {

            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                sIngredients = response.body().get(0).getIngredients();
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

        Intent recipeUpdateIntent = new Intent();
        recipeUpdateIntent.setAction(RecipeWidgetProvider.DATA_FETCHED);
        recipeUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        sendBroadcast(recipeUpdateIntent);
        this.stopSelf();
    }
}
