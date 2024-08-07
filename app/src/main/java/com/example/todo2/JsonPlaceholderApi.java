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

    @GET("todos?_limit=10")
    Call<List<Todo>> getTodos(@Query("limit") int limit, @Query("offset") int offset);

    @POST("todos")
    Call<Todo> createTodo(@Body Todo todo);

    @GET("todos/{id}")
    Call<Todo> getTodoById(@Path("id") int id);

    @PUT("todos/{id}")
    Call<Todo> updateTodo(
            @Path("id") int id,
            @Body Todo todo
    );
}
