package com.example.pictureplace;

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
                        @Part("locationname") String locationname,
                        @Part("locationaddress") String locationAddress,
                        @Part("latitude") String latitude,
                        @Part("longitude") String longitude,
                        @Part("content") String content,
                        @Part("disclosure") String disclosure,
                        @Part("tags") String[] tags
    );

    @GET("posting/mypin")
    Call<List<MyPinDTO>> myPin(@Header("authorization") String access_token);

    @GET("suggest/weeklyloca")
    Call<List<SuggestDTO>> getSuggestWeekly();

    @GET("suggest/popular")
    Call<List<SuggestDTO>> getSuggestPopular();

    @GET("suggest/random")
    Call<List<SuggestDTO>> getSuggestRandom();

    @GET("/location/nearest")
    Call<List<MyPinDTO>> getNearestLocations(
            @Query("centerLatitude") double centerLatitude,
            @Query("centerLongitude") double centerLongitude,
            @Query("zoom") float zoom
    );

    @GET("location/within-radius")
    Call<List<MapPinsDTO>> getMapPins(
            @Query("centerLatitude") double centerLatitude,
            @Query("centerLongitude") double centerLongitude,
            @Query("zoom") float zoom
    );

    @GET("location")
    Call<List<MyPinDTO>> postLoad(
            @Query("locationid") String locationId
    );


}
