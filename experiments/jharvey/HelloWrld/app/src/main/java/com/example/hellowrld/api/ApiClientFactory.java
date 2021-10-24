package com.example.hellowrld.api;

import retrofit2.Retrofit;

public class ApiClientFactory {

    static Retrofit apiClientSeed = null;

   static Retrofit GetApiClientSeed() {
        if (apiClientSeed == null) {
            apiClientSeed = Retrofit.Builder()  //new api client
                    .baseURL("https://jsonplaceholder.typicode.com/")
                    .addCoverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return apiClientSeed;
    }
    //add new ones for each api
    public static PostApi GetPostApi(){
       return GetApiClientSeed.create(PostApi.class);
    }

}
