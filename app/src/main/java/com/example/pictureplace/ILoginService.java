package com.example.pictureplace;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ILoginService {
    String loginBaseUrl = "https://www.picplace.kro.kr:443/";
    String searchBaseUrl = "https://www.picplace.kro.kr/api/search?q=%EA%B9%80%ED%8F%AC/";
    //"https://pinover.ddns.net:41001/api/login/"

    @FormUrlEncoded
    @POST("api/login")
    Call<LoginResponse> getMember(@Field("userid") String mem_id,
    @Field("password") String passwd);

    @FormUrlEncoded
    @POST("api/signup")
    Call<String> joinMember( @Field("userid") String mem_id,
                              @Field("password") String password,
                              @Field("name") String name,
                              @Query("address") String address,
                              @Query("hp") String hp);

    @FormUrlEncoded
    @POST("api/refresh")
    Call<String> refresh(@Field("refresh_token") String refresh_token);

    @GET("search")	// 전체 URL 에서 URL을 제외한 End Point를 적어준다.
    Call<String>search();






}
