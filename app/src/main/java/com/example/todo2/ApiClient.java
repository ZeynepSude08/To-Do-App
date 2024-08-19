package com.example.todo2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    // Singleton pattern to ensure a single instance of Retrofit
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

/*
ApiClient is a class responsible for configuring and providing a Retrofit instance.It sets up the Retrofit client with the necessary configurations,
such as the bse URL and any other settings(e.g. converters)
*/
