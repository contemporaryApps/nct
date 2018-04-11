package com.remoteconfig.test.model;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigRepositoryImpl implements ConfigRepository {

    //Retrofit wants a base url
    private static final String DEFAULT_URL = "https://example.com/";

    //This is the url used for the json-config file.
    private static final String FULL_JSON_PATH = "https://api.myjson.com/bins/18jn8r";

    //Alternative with 6 tabs
    //private static final String FULL_JSON_PATH = "https://api.myjson.com/bins/un067";

    private static final String STORE_KEY = "local_tab_config";
    private static final String CONFIG_DEFAULT = "[]";
    private static final String REMOTE_CONFIG = "Remote config";
    private static final String LOCAL_CONFIG_CORRUPT = "Local config corrupt";
    private static final String CACHED_CONFIG = "Cached config";
    private static final String CONFIG_SIZE_IS_ZERO = "Config size is zero";

    private static ConfigRepositoryImpl INSTANCE = null;
    private final Api service;
    private final SharedPreferences sharedPreferences;

    private Type listType = new TypeToken<List<TabConfig>>() {
    }.getType();
    private Gson gson;

    private ConfigRepositoryImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;

        gson = new Gson();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DEFAULT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Api.class);
    }

    public static ConfigRepositoryImpl getInstance(SharedPreferences sharedPreferences) {
        if (INSTANCE == null) {
            INSTANCE = new ConfigRepositoryImpl(sharedPreferences);
        }
        return INSTANCE;
    }

    @Override
    public void getTabConfig(@NonNull final GetConfigCallback getConfigCallback) {
        //First try and get the remote config file
        //If successful save it locally and pass it back to caller
        //If failure try and get the local version
        service.getRemoteTabConfig(FULL_JSON_PATH).enqueue(new Callback<List<TabConfig>>() {
            @Override
            public void onResponse(Call<List<TabConfig>> call, Response<List<TabConfig>> response) {
                if (response.isSuccessful()) {
                    saveLocalConfig(response.body());
                    getConfigCallback.onConfigLoaded(response.body(), REMOTE_CONFIG);
                } else {
                    getLocalTabConfig(getConfigCallback);
                }
            }

            @Override
            public void onFailure(Call<List<TabConfig>> call, Throwable t) {
                getLocalTabConfig(getConfigCallback);
            }
        });
    }

    private void getLocalTabConfig(@NonNull final GetConfigCallback getConfigCallback) {
        List<TabConfig> config = null;
        try {
            config = gson.fromJson(sharedPreferences.getString(STORE_KEY, CONFIG_DEFAULT), listType);
        } catch (JsonSyntaxException e) {
            getConfigCallback.onNoConfigAvailable(LOCAL_CONFIG_CORRUPT);
        }

        if (config != null && config.size() > 0) {
            getConfigCallback.onConfigLoaded(config, CACHED_CONFIG);
        } else {
            getConfigCallback.onNoConfigAvailable(CONFIG_SIZE_IS_ZERO);
        }
    }

    private void saveLocalConfig(List<TabConfig> response) {
        sharedPreferences.edit().putString(STORE_KEY, gson.toJson(response, listType)).apply();
    }
}
