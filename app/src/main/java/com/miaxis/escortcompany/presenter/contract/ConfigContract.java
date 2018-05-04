package com.miaxis.escortcompany.presenter.contract;

import com.miaxis.escortcompany.model.entity.Config;

/**
 * Created by 一非 on 2018/5/3.
 */

public interface ConfigContract {
    interface View extends IBaseView {
        void configSaveSuccess();
        void configSaveFailed();
        void fetchConfig(Config config);
        void setProgressDialogMessage(String message);
    }

    interface Presenter extends IBasePresenter {
        void configSave(String ip, String port, String orgCode);
        void loadConfig();
    }
}
