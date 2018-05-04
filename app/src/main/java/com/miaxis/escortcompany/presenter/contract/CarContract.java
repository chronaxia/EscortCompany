package com.miaxis.escortcompany.presenter.contract;

import com.miaxis.escortcompany.model.entity.Car;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Escort;

import java.util.List;

/**
 * Created by 一非 on 2018/5/4.
 */

public interface CarContract {
    interface View extends IBaseView {
        void updateCar(List<Car> escortList);
        void loadCarFailed(String message);
        void downCarSuccess(Company company);
        void downCarFailed(String message, Company company);
    }

    interface Presenter extends IBasePresenter {
        void downCar(Company company);
        void loadCar(Company company);
    }
}
