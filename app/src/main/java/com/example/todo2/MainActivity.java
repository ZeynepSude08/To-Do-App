package com.example.todo2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Retrofit and ApiService
        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        // Initialize the button and set its click listener
        Button addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog(); // Show dialog to add a new task
            }
        });

        // Example: Fetch tasks
        fetchTasks();
    }

    private void fetchTasks() {
        Call<List<Task>> call = apiService.getTasks();
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful()) {
                    List<Task> tasks = response.body();
                    // Handle the tasks list
                    Log.d("MainActivity", "Tasks fetched: " + tasks);
                } else {
                    Log.e("MainActivity", "Error Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("MainActivity", t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to fetch tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createTask(Task task) {
        Call<Task> call = apiService.createTask(task);
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful()) {
                    Task createdTask = response.body();
                    Log.d("MainActivity", "Task created: " + createdTask.getTitle());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("MainActivity", "Error Code: " + response.code() + ", Error Body: " + errorBody);
                        Toast.makeText(MainActivity.this, "Failed to create task", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Log.e("MainActivity", t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to create task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTask(int id, Task task) {
        Call<Task> call = apiService.updateTask(id, task);
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful()) {
                    Task updatedTask = response.body();
                    // Handle the updated task
                    Log.d("MainActivity", "Task updated: " + updatedTask.getTitle());
                } else {
                    Log.e("MainActivity", "Error Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Log.e("MainActivity", t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to update task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteTask(int id) {
        Call<Void> call = apiService.deleteTask(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful deletion
                    Log.d("MainActivity", "Task deleted successfully");
                } else {
                    Log.e("MainActivity", "Error Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MainActivity", t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to delete task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Define this method to show a dialog for adding a new task
    private void showAddTaskDialog() {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");

        // Inflate the custom view for the dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dailog, null);
        builder.setView(dialogView);

        // Find the EditText in the inflated layout
        final EditText inputTitle = dialogView.findViewById(R.id.nameEdit);

        // Set up the dialog's buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the task title from the input field
                String title = inputTitle.getText().toString();
                if (!title.trim().isEmpty()) {
                    // Create a new Task object
                    Task newTask = new Task();
                    newTask.setTitle(title);
                    newTask.setCompleted(false);

                    // Call the API to create the task
                    createTask(newTask);
                } else {
                    Toast.makeText(MainActivity.this, "Task title cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


