package com.example.todo2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {

    @GET("tasks")
    Call<List<Task>> getTasks();

    @POST("tasks")
    Call<Task> createTask(@Body Task task);

    @GET("tasks/{id}")
    Call<Task> getTaskById(@Path("id") int id);

    @POST("tasks/{id}")
    Call<Task> updateTask(@Path("id") int id, @Body Task task);

    @POST("tasks/{id}/delete")
    Call<Void> deleteTask(@Path("id") int id);
}
// ApiService is an interface that defines the endpoints of the API.
// It includes methods signatures that Retrofit uses to create the network requests.