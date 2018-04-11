package com.remoteconfig.test.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {
    @GET
    Call<List<TabConfig>> getRemoteTabConfig(@Url String anEmptyString);
}
