package com.miaxis.escortcompany.presenter.contract;

import android.app.Activity;

/**
 * Created by 一非 on 2018/5/3.
 */

public interface LoginContract {
    interface View extends IBaseView {
        void showLoginView();
        void showConfigView();
        void getPermissionsSuccess();
        void getPermissionsFailed();
        void loginSuccess();
        void loginFailed(String message);
        void loadUsername(String username);
    }

    interface Presenter extends IBasePresenter {
        void getPermissions(Activity activity);
        void loadConfig();
        void login(String username, String password);
    }
}
