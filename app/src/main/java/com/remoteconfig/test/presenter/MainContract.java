package com.remoteconfig.test.presenter;

import com.remoteconfig.test.model.TabConfig;

import java.util.List;

public interface MainContract {
    interface View {
        void displayTabs(List<TabConfig> tabs);

        void displayProgress();

        void hideProgress();

        void toastMessage(String message);

        void displayErrorMessage(String error);
    }

    interface Presenter {
        void detachView();

        void getTabConfiguration();
    }
}
