package com.miaxis.escortcompany.presenter.contract;

import com.miaxis.escortcompany.model.entity.Car;

/**
 * Created by 一非 on 2018/5/7.
 */

public interface AddCarContract {
    interface View extends IBaseView {
        void addCarSuccess();
        void addCarFailed(String message);
    }

    interface Presenter extends IBasePresenter {
        void addCar(Car car, String path);
    }
}
