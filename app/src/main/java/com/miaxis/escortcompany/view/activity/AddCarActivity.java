package com.miaxis.escortcompany.view.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;
import com.miaxis.escortcompany.R;
import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.entity.Car;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Config;
import com.miaxis.escortcompany.presenter.AddCarPresenter;
import com.miaxis.escortcompany.presenter.contract.AddCarContract;
import com.miaxis.escortcompany.util.DateUtil;
import com.miaxis.escortcompany.util.StaticVariable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class AddCarActivity extends BaseActivity implements AddCarContract.View{

    @BindView(R.id.add_car_toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_car_code)
    EditText etCarCode;
    @BindView(R.id.et_car_plate)
    EditText etCarPlate;
    @BindView(R.id.tv_car_rfid)
    TextView tvCarRfid;
    @BindView(R.id.et_car_remark)
    EditText etCarRemark;
    @BindView(R.id.iv_car_photo)
    ImageView ivCarPhoto;
    @BindView(R.id.btn_add_car)
    Button btnAddCar;

    private Company company;
    private AddCarContract.Presenter presenter;
    private String path = "";
    private static final int REQUEST_CODE_IMAGE = 0;
    private static final int REQUEST_CODE_CAMERA = 1;

    @Override
    protected int setContentView() {
        return R.layout.activity_add_car;
    }

    @Override
    protected void initData() {
        presenter = new AddCarPresenter(this, this);
        company = (Company) getIntent().getSerializableExtra("company");
    }

    @Override
    protected void initView() {
        toolbar.setTitle("添加员工");
        RxView.clicks(ivCarPhoto)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        new MaterialDialog.Builder(AddCarActivity.this)
                                .negativeText("相册")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent, REQUEST_CODE_IMAGE);
                                    }
                                })
                                .positiveText("拍照")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        //path为保存图片的路径，执行完拍照以后能保存到指定的路径下
                                        path = Environment.getExternalStorageDirectory().getPath() + File.separator + DateUtil.getCurDateTime24() + ".jpg";
                                        File file = new File(path);
                                        Uri imageUri = Uri.fromFile(file );
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        startActivityForResult(intent, REQUEST_CODE_CAMERA);
                                    }
                                }).show();
                    }
                });
        RxView.clicks(btnAddCar)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Config config = (Config) EscortCompanyApp.getInstance().get(StaticVariable.CONFIG);
                        Car car = new Car();
                        car.setCarcode(getResources().getString(R.string.car_code) + etCarCode.getText().toString());
                        car.setPlateno(etCarPlate.getText().toString());
                        car.setRemark(etCarRemark.getText().toString());
                        car.setCompid(company.getId());
                        car.setCompname(company.getCompname());
                        car.setCompno(company.getCompno());
                        car.setOpdate(DateUtil.getCurDateTime24());
                        car.setOpuser(config.getUsername());
                        presenter.addCar(car, path);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null,null);
            if (cursor != null && cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                Glide.with(this)
                        .load(path)
                        .into(ivCarPhoto);
            }
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Glide.with(this)
                    .load(path)
                    .into(ivCarPhoto);
        }

    }

    @Override
    public void addCarSuccess() {
        Toasty.success(EscortCompanyApp.getInstance().getApplicationContext(), "添加成功", 0, true).show();
        finish();
    }

    @Override
    public void addCarFailed(String message) {
        Toasty.error(EscortCompanyApp.getInstance().getApplicationContext(), message, 0, true).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.doDestroy();
    }

}
