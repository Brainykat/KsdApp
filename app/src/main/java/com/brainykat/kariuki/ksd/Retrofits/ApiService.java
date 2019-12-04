package com.brainykat.kariuki.ksd.Retrofits;

import com.brainykat.kariuki.ksd.Retrofits.Pojos.Meal;
import com.brainykat.kariuki.ksd.Retrofits.Pojos.Response;
import com.brainykat.kariuki.ksd.Retrofits.Pojos.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("ksd/card/{cardNo}")
    Call<Student> GetStudent(@Path(value = "cardNo", encoded = true) String cardNo);

    @POST("ksd/Meal/{id}")
    Call<Response> PlaceOrder(@Path(value = "id", encoded = true) String id,@Body Meal comp);
}
