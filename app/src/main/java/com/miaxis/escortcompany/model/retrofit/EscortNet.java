package com.miaxis.escortcompany.model.retrofit;

import com.miaxis.escortcompany.model.entity.Escort;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 一非 on 2018/5/4.
 */

public interface EscortNet {
    @GET("yygl/api/downEscortByCompId")
    Observable<ResponseEntity<Escort>> downEscortByCompId(@Query("compId") Integer compId, @Query("sjc") String sjc);
    @GET("yygl/api/delEscort")
    Observable<ResponseEntity> delEscort(@Query("escortno") String escortno, @Query("opUser") String opUser);
    @GET("yygl/api/addEscort")
    Observable<ResponseEntity> addEscort(@Query("jsonEscorter") String jsonEscorter);
}
