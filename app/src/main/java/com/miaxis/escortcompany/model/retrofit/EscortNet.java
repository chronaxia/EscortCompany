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
}
