package com.miaxis.escortcompany.presenter;

import android.Manifest;
import android.app.Activity;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.LoginModel;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Config;
import com.miaxis.escortcompany.model.retrofit.LoginNet;
import com.miaxis.escortcompany.model.retrofit.ResponseEntity;
import com.miaxis.escortcompany.presenter.contract.LoginContract;
import com.miaxis.escortcompany.util.StaticVariable;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

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
 * Created by 一非 on 2018/5/3.
 */

public class LoginPresenter extends BaseActivityPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private LoginModel model;

    public LoginPresenter(LifecycleProvider<ActivityEvent> provider, LoginContract.View view) {
        super(provider);
        this.view = view;
        this.model = new LoginModel();
    }

    @Override
    public void getPermissions(Activity activity) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<Boolean>bindToLifecycle())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (view != null) {
                            if (aBoolean) {
                                view.getPermissionsSuccess();
                            } else {
                                view.getPermissionsFailed();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view != null) {
                            view.getPermissionsFailed();
                        }
                    }
                });
    }

    @Override
    public void loadConfig() {
        Observable.create(new ObservableOnSubscribe<Config>() {
            @Override
            public void subscribe(ObservableEmitter<Config> e) throws Exception {
                Config config = model.loadConfig();
                e.onNext(config);
            }
        })
                .subscribeOn(Schedulers.io())
                .compose(getProvider().<Config>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Config>() {
                    @Override
                    public void accept(Config config) throws Exception {
                        if (view != null && config.getUsername() != null) {
                            view.loadUsername(config.getUsername());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view != null) {
                            view.showConfigView();
                        }
                    }
                });
    }

    @Override
    public void login(final String username, final String password) {
        final Config config = model.loadConfig();
        Observable.just(config)
                .subscribeOn(Schedulers.io())
                .compose(getProvider().<Config>bindToLifecycle())
                .observeOn(Schedulers.io())
                .flatMap(new Function<Config, ObservableSource<ResponseEntity<Company>>>() {
                    @Override
                    public ObservableSource<ResponseEntity<Company>> apply(Config config) throws Exception {
                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                                .baseUrl("http://" + config.getIp() + ":" + config.getPort())
                                .build();
                        LoginNet loginNet = retrofit.create(LoginNet.class);
                        return loginNet.downComp(username, password, config.getOrgCode());
                    }
                })
                .doOnNext(new Consumer<ResponseEntity<Company>>() {
                    @Override
                    public void accept(ResponseEntity<Company> companyResponseEntity) throws Exception {
                        if (StaticVariable.SUCCESS.equals(companyResponseEntity.getCode())) {
                            model.saveCompanyList(companyResponseEntity.getListData());
                            model.saveUsername(username);
                        } else {
                            throw new Exception(companyResponseEntity.getMessage());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseEntity<Company>>() {
                    @Override
                    public void accept(ResponseEntity<Company> companyResponseEntity) throws Exception {
                        if (view != null) {
                            config.setUsername(username);
                            EscortCompanyApp.getInstance().put(StaticVariable.CONFIG, config);
                            view.loginSuccess();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view != null) {
                            view.loginFailed(throwable.getMessage());
                        }
                    }
                });
    }

    @Override
    public void doDestroy() {
        view = null;
    }
}
