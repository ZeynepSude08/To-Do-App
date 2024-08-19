package com.example.todo2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface JsonPlaceholderApi {

    @GET("tasks?_limit=10")
    Call<List<Task>> getTasks(@Query("limit") int limit, @Query("offset") int offset);

    @POST("todos")
    Call<Task> createTask(@Body Todo todo);

    @GET("tasks/{id}")
    Call<Task> getTaskById(@Path("id") int id);

    @PUT("tasks/{id}")
    Call<Task> updateTask(
            @Path("id") int id,
            @Body Task task
    );
}
