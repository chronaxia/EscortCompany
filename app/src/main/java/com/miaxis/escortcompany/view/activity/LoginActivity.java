package com.miaxis.escortcompany.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.miaxis.escortcompany.R;
import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.entity.Escort;
import com.miaxis.escortcompany.presenter.LoginPresenter;
import com.miaxis.escortcompany.presenter.contract.LoginContract;
import com.miaxis.escortcompany.util.StaticVariable;
import com.miaxis.escortcompany.view.fragment.ConfigFragment;

import java.util.Observable;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity implements LoginContract.View, ConfigFragment.OnConfigClickListener{

    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.fl_config)
    FrameLayout flConfig;
    @BindView(R.id.iv_config)
    ImageView ivConfig;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    private LoginContract.Presenter presenter;
    private MaterialDialog materialDialog;

    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        presenter = new LoginPresenter(this, this);
        presenter.getPermissions(this);
    }

    @Override
    protected void initView() {
        materialDialog = new MaterialDialog.Builder(this)
                .content("正在登陆...")
                .cancelable(false)
                .build();
        tvVersion.setText(tvVersion.getText() + StaticVariable.getVersion());
        RxView.clicks(ivConfig)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        showConfigView();
                    }
                });
        RxView.clicks(btnLogin)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (checkNotNull()) {
                            materialDialog.show();
                            presenter.login(etUsername.getText().toString(), etPassword.getText().toString());
                        } else {
                            Toasty.error(EscortCompanyApp.getInstance().getApplicationContext(), "请输入用户名或密码", 0, true).show();
                        }
                    }
                });
    }

    private boolean checkNotNull() {
        if (!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void loginSuccess() {
        materialDialog.dismiss();
        Toasty.success(EscortCompanyApp.getInstance().getApplicationContext(), "登陆成功", 0, true).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void loginFailed(String message) {
        materialDialog.dismiss();
        Toasty.error(EscortCompanyApp.getInstance().getApplicationContext(), message, 0, true).show();
    }

    @Override
    public void onConfigSave() {
        showLoginView();
    }

    @Override
    public void onConfigCancel() {
        showLoginView();
    }

    @Override
    public void showLoginView() {
        ivConfig.setVisibility(View.VISIBLE);
        llLogin.setVisibility(View.VISIBLE);
        flConfig.setVisibility(View.GONE);
    }

    @Override
    public void showConfigView() {
        ivConfig.setVisibility(View.INVISIBLE);
        llLogin.setVisibility(View.GONE);
        flConfig.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_config, ConfigFragment.newInstance()).commit();
    }

    @Override
    public void getPermissionsSuccess() {
        EscortCompanyApp.getInstance().initDbHelp();
        presenter.loadConfig();
    }

    @Override
    public void getPermissionsFailed() {
        Toasty.error(this, "拒绝权限将无法正常运行", 0, true).show();
        finish();
    }

    @Override
    public void loadUsername(String username) {
        etUsername.setText(username);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.doDestroy();
    }
}
