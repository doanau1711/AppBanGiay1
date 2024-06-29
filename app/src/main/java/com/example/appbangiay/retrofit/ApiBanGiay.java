package com.example.appbangiay.retrofit;

import com.example.appbangiay.model.DanhMuc;
import com.example.appbangiay.model.DanhMucModel;
import com.example.appbangiay.model.ResponseObject;
import com.example.appbangiay.model.SanPhamMoiModel;
import com.example.appbangiay.model.UserModel;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanGiay {

    // get Data
    @GET("getDanhMuc.php")
    Observable<DanhMucModel> getDanhMucChinh();

    @GET("getSpMoi.php")
    Observable<SanPhamMoiModel> getSanPhamMoiChinh();

    // Post data
    @POST("getDanhSachSp.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getDanhSachSp(
            @Field("page") int page,
            @Field("amount") int amount
    );

    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangKy(
            @Field("email") String email,
            @Field(("password")) String password,
            @Field("username") String username,
            @Field("mobile") String mobile

    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel> resetPassword(
            @Field("email") String email
    );


}
