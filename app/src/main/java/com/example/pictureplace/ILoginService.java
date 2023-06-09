package com.example.pictureplace;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ILoginService {
    String BaseUrl = "https://www.picplace.kro.kr:443/";

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginDTO> getMember(@Field("userid") String mem_id,
                             @Field("password") String passwd);

    @FormUrlEncoded
    @POST("user/signup")
    Call<String> joinMember( @Field("userid") String mem_id,
                              @Field("password") String password,
                              @Field("name") String name,
                              @Field("address") String address,
                              @Field("hp") String hp);

    @FormUrlEncoded
    @POST("api/refresh")
    Call<String> refresh(@Field("refresh_token") String refresh_token);

    @GET("search")	// 전체 URL 에서 URL을 제외한 End Point를 적어준다.
    Call<String>search();

    @Multipart
    @POST("posting/upload")
    Call<String> upload(@Header("authorization") String access_token,
            @Part List<MultipartBody.Part> photo,
                        @Part("locationid") String locationid,
                        @Part("content") String content,
                        @Part("disclosure") String disclosure,
                        @Part("tags") String[] tags
    );






}
