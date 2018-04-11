package com.remoteconfig.test.presenter;

import com.remoteconfig.test.model.ConfigRepository;
import com.remoteconfig.test.model.TabConfig;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private ConfigRepository configRepository;

    public MainPresenter(MainContract.View view, ConfigRepository configRepository) {
        this.view = view;
        this.configRepository = configRepository;
    }

    @Override
    public void detachView() {
        view = null;
    }

    private boolean viewIsReady() {
        return view != null;
    }

    @Override
    public void getTabConfiguration() {
        if (viewIsReady()) {
            view.displayProgress();
        }

        configRepository.getTabConfig(new ConfigRepository.GetConfigCallback() {
            @Override
            public void onConfigLoaded(List<TabConfig> tabs, String message) {
                if (viewIsReady()) {
                    view.hideProgress();
                    view.displayTabs(tabs);
                    view.toastMessage(message);
                }
            }

            @Override
            public void onNoConfigAvailable(String errorMessage) {
                if (viewIsReady()) {
                    view.hideProgress();
                    view.displayErrorMessage(errorMessage);
                }
            }
        });
    }
}
