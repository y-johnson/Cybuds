package com.example.apicalls.api;

import com.example.apicalls.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @POST("")
    Call<User> addUser(@Body User newUser);

    //@GET("all")
    //Call<Iterable<User>> getAllUsers();

    @GET("all")
    Call<List<User>> getAllUsers();

}
