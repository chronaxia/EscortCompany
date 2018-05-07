package com.miaxis.escortcompany.presenter.contract;

import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Config;
import com.miaxis.escortcompany.model.entity.Escort;

import java.util.List;

/**
 * Created by 一非 on 2018/5/4.
 */

public interface EscortContract {
    interface View extends IBaseView {
        void updateEscort(List<Escort> escortList);
        void loadEscortFailed(String message);
        void downEscortSuccess(Company company);
        void downEscortFailed(String message, Company company);
        void deleteEscortSuccess();
        void deleteEscortFailed(String message);
    }

    interface Presenter extends IBasePresenter {
        void downEscort(Company company);
        void loadEscort(Company company);
        void deleteEscort(Escort escort);
    }
}
