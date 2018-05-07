package com.miaxis.escortcompany.presenter;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.CarModel;
import com.miaxis.escortcompany.model.entity.Car;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Config;
import com.miaxis.escortcompany.model.entity.Escort;
import com.miaxis.escortcompany.model.retrofit.CarNet;
import com.miaxis.escortcompany.model.retrofit.EscortNet;
import com.miaxis.escortcompany.model.retrofit.ResponseEntity;
import com.miaxis.escortcompany.presenter.contract.CarContract;
import com.miaxis.escortcompany.util.StaticVariable;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    public void downCar(final Company company) {
        Observable.create(new ObservableOnSubscribe<Config>() {
            @Override
            public void subscribe(ObservableEmitter<Config> e) throws Exception {
                e.onNext((Config) EscortCompanyApp.getInstance().get(StaticVariable.CONFIG));
            }
        })
                .subscribeOn(Schedulers.io())
                .compose(getProvider().<Config>bindToLifecycle())
                .observeOn(Schedulers.io())
                .flatMap(new Function<Config, ObservableSource<ResponseEntity<Car>>>() {
                    @Override
                    public ObservableSource<ResponseEntity<Car>> apply(Config config) throws Exception {
                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                                .baseUrl("http://" + config.getIp() + ":" + config.getPort())
                                .build();
                        CarNet carNet = retrofit.create(CarNet.class);
                        return carNet.downCarByCompId(company.getId());
                    }
                })
                .doOnNext(new Consumer<ResponseEntity<Car>>() {
                    @Override
                    public void accept(ResponseEntity<Car> carResponseEntity) throws Exception {
                        if (StaticVariable.SUCCESS.equals(carResponseEntity.getCode())) {
                            model.saveCar(carResponseEntity.getListData());
                        } else {
                            throw new Exception(carResponseEntity.getMessage());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseEntity<Car>>() {
                    @Override
                    public void accept(ResponseEntity<Car> carResponseEntity) throws Exception {
                        if (view != null) {
                            view.downCarSuccess(company);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view != null) {
                            view.downCarFailed(throwable.getMessage(), company);
                        }
                    }
                });
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
    public void deleteCar(final Car car) {
        Observable.create(new ObservableOnSubscribe<Config>() {
            @Override
            public void subscribe(ObservableEmitter<Config> e) throws Exception {
                e.onNext((Config) EscortCompanyApp.getInstance().get(StaticVariable.CONFIG));
            }
        })
                .subscribeOn(Schedulers.io())
                .compose(getProvider().<Config>bindToLifecycle())
                .observeOn(Schedulers.io())
                .flatMap(new Function<Config, ObservableSource<ResponseEntity>>() {
                    @Override
                    public ObservableSource<ResponseEntity> apply(Config config) throws Exception {
                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                                .baseUrl("http://" + config.getIp() + ":" + config.getPort())
                                .build();
                        CarNet carNet = retrofit.create(CarNet.class);
                        return carNet.delCar(new Gson().toJson(car));
                    }
                })
                .doOnNext(new Consumer<ResponseEntity>() {
                    @Override
                    public void accept(ResponseEntity responseEntity) throws Exception {
                        if (StaticVariable.SUCCESS.equals(responseEntity.getCode())) {
                            model.deleteCar(car);
                        } else {
                            throw new Exception(responseEntity.getMessage());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseEntity>() {
                    @Override
                    public void accept(ResponseEntity responseEntity) throws Exception {
                        if (view != null) {
                            view.deleteCarSuccess();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view != null) {
                            view.deleteCarFailed(throwable.getMessage());
                        }
                    }
                });
    }

    @Override
    public void doDestroy() {
        view = null;
    }
}
