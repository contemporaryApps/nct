package com.remoteconfig.test.model;

import android.support.annotation.NonNull;

import java.util.List;

public interface ConfigRepository {
    void getTabConfig(@NonNull GetConfigCallback getConfigCallback);

    interface GetConfigCallback {
        void onConfigLoaded(List<TabConfig> tabs, String message);

        void onNoConfigAvailable(String errorMessage);
    }
}
