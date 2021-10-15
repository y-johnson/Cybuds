//package edu.coms309.cybuds.api;
//
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class ApiClientFactory {
//
//    static Retrofit apiClientSeed = null;
//
//    static Retrofit GetApiClientSeed() {
//
//        if (apiClientSeed == null) {
//
//            Retrofit retrofit = new Retrofit.Builder() //http://10.0.2.2:8080/
//                    .baseUrl("http://coms-309-028.cs.iastate.edu:8080/") //get our server url
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build(); //http://coms-309-028.cs.iastate.edu:8080
//        }
//        return apiClientSeed;
//    }
//
//    public static UserApi GetUserApi(){
//        return GetApiClientSeed().create(UserApi.class);
//    }
//
//}
