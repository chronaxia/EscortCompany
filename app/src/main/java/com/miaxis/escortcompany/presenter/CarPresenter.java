package com.miaxis.escortcompany.presenter;

import com.miaxis.escortcompany.model.CarModel;
import com.miaxis.escortcompany.model.entity.Car;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Escort;
import com.miaxis.escortcompany.presenter.contract.CarContract;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 一非 on 2018/5/4.
 */

public class CarPresenter extends BaseFragmentPresenter implements CarContract.Presenter {

    private CarContract.View view;
    private CarModel model;

    public CarPresenter(LifecycleProvider<FragmentEvent> provider, CarContract.View view) {
        super(provider);
        this.view = view;
        model = new CarModel();
    }

    @Override
    public void downCar(Company company) {
    }

    @Override
    public void loadCar(Company company) {
        Observable.just(company)
                .subscribeOn(Schedulers.io())
                .compose(getProvider().<Company>bindToLifecycle())
                .observeOn(Schedulers.io())
                .map(new Function<Company, List<Car>>() {
                    @Override
                    public List<Car> apply(Company company) throws Exception {
                        return model.loadCar(company);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Car>>() {
                    @Override
                    public void accept(List<Car> carList) throws Exception {
                        if (view != null) {
                            view.updateCar(carList);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view != null) {
                            view.loadCarFailed(throwable.getMessage());
                        }
                    }
                });
    }

    @Override
    public void doDestroy() {
        view = null;
    }
}
