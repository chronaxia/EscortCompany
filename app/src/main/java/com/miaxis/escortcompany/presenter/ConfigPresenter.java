package com.miaxis.escortcompany.presenter;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.LoginModel;
import com.miaxis.escortcompany.model.entity.Config;
import com.miaxis.escortcompany.model.retrofit.LoginNet;
import com.miaxis.escortcompany.model.retrofit.ResponseEntity;
import com.miaxis.escortcompany.presenter.contract.ConfigContract;
import com.miaxis.escortcompany.util.StaticVariable;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 一非 on 2018/5/3.
 */

public class ConfigPresenter extends BaseFragmentPresenter implements ConfigContract.Presenter {

    private ConfigContract.View view;
    private LoginModel model;

    public ConfigPresenter(LifecycleProvider<FragmentEvent> provider, ConfigContract.View view) {
        super(provider);
        this.view = view;
        this.model = new LoginModel();
    }

    @Override
    public void configSave(String ip, String port, String orgCode) {
        final Config config = new Config(1L, ip, port, orgCode, null);
        Observable.just(config)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Config>() {
                    @Override
                    public void accept(Config config) throws Exception {
                        EscortCompanyApp.getInstance().clearAndRebuildDatabase();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Config>() {
                    @Override
                    public void accept(Config config) throws Exception {
                        if (view != null) {
                            view.setProgressDialogMessage("清空数据成功，正在下载机构信息...");
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Config, ObservableSource<ResponseEntity>>() {
                    @Override
                    public ObservableSource<ResponseEntity> apply(Config config) throws Exception {
                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                                .baseUrl("http://" + config.getIp() + ":" + config.getPort())
                                .build();
                        LoginNet loginNet = retrofit.create(LoginNet.class);
                        return loginNet.verifyComp(config.getOrgCode());
                    }
                })
                .doOnNext(new Consumer<ResponseEntity>() {
                    @Override
                    public void accept(ResponseEntity responseEntity) throws Exception {
                        if (StaticVariable.SUCCESS.equals(responseEntity.getCode())) {
                            model.saveConfig(config);
                            EscortCompanyApp.getInstance().put(StaticVariable.CONFIG, config);
                        } else {
                            throw new Exception("连接错误或机构号未存在");
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseEntity>() {
                    @Override
                    public void accept(ResponseEntity responseEntity) throws Exception {
                        if (view != null) {
                            view.setProgressDialogMessage("连接成功");
                            view.configSaveSuccess();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view != null) {
                            view.setProgressDialogMessage(throwable.getMessage());
                            view.configSaveFailed();
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
                .compose(getProvider().<Config>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Config>() {
                    @Override
                    public void accept(Config config) throws Exception {
                        if (view != null) {
                            view.fetchConfig(config);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    @Override
    public void doDestroy() {
        view = null;
    }
}
