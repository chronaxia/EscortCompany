package com.miaxis.escortcompany.model.retrofit;

import com.miaxis.escortcompany.model.entity.Car;
import com.miaxis.escortcompany.model.entity.Escort;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by 一非 on 2018/5/7.
 */

public interface CarNet {
    @GET("yygl/api/downCarByCompId")
    Observable<ResponseEntity<Car>> downCarByCompId(@Query("compId") Integer compId);
    @GET("yygl/api/delCar")
    Observable<ResponseEntity> delCar(@Query("jsonCar") String jsonCar);
    @Multipart
    @POST("yygl/api/addCar")
    Observable<ResponseEntity> addCar(@Query("jsonCar") String jsonCar, @Part MultipartBody.Part file);
}
