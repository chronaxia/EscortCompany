package com.miaxis.escortcompany.presenter;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.CarModel;
import com.miaxis.escortcompany.model.entity.Car;
import com.miaxis.escortcompany.model.entity.Config;
import com.miaxis.escortcompany.model.retrofit.CarNet;
import com.miaxis.escortcompany.model.retrofit.ResponseEntity;
import com.miaxis.escortcompany.presenter.contract.AddCarContract;
import com.miaxis.escortcompany.util.StaticVariable;
import com.miaxis.escortcompany.view.activity.BaseActivity;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 一非 on 2018/5/7.
 */

public class AddCarPresenter extends BaseActivityPresenter implements AddCarContract.Presenter {

    private AddCarContract.View view;
    private CarModel model;

    public AddCarPresenter(LifecycleProvider<ActivityEvent> provider, AddCarContract.View view) {
        super(provider);
        this.view = view;
        model = new CarModel();
    }

    @Override
    public void addCar(final Car car, final String path) {
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
                        File file = new File(path);
                        RequestBody requestBody = RequestBody.create(MediaType.parse(StaticVariable.getMimeType(path)), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
//                        MultipartBody.Builder builder = new MultipartBody.Builder()
//                                .setType(MultipartBody.FORM);
//                        File file = new File(path);
//                        RequestBody voiceRequestBody = RequestBody.create(MediaType.parse(StaticVariable.getMimeType(path)), file);
//                        builder.addFormDataPart("file", file.getName(), voiceRequestBody);
//                        List<MultipartBody.Part> parts = null;
//                        try {
//                            parts = builder.build().parts();
//                        } catch (Exception e) {
//                            parts = builder.addFormDataPart("t", "1").build().parts();
//                        }
                        CarNet carNet = retrofit.create(CarNet.class);
                        return carNet.addCar(URLDecoder.decode(new Gson().toJson(car), "utf-8"), part);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<ResponseEntity>() {
                    @Override
                    public void accept(ResponseEntity responseEntity) throws Exception {
                        if (StaticVariable.SUCCESS.equals(responseEntity.getCode())) {
                        } else {
                            throw new Exception(responseEntity.getMessage());
                        }
                    }
                })
                .subscribe(new Consumer<ResponseEntity>() {
                    @Override
                    public void accept(ResponseEntity responseEntity) throws Exception {
                        if (view != null) {
                            view.addCarSuccess();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view != null) {
                            view.addCarFailed(throwable.getMessage());
                        }
                    }
                });
    }

    @Override
    public void doDestroy() {
        view = null;
    }
}
