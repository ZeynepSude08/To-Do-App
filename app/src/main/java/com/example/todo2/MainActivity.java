package com.example.todo2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;  
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TaskDao taskDao;
    private AppDatabase db;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(this);
        taskDao = db.taskDao();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new TaskAdapter();
        recyclerView.setAdapter(taskAdapter);

        // Initialize the button and set its click listener
        Button addButton = findViewById(R.id.add);
        addButton.setOnClickListener(v -> showAddTaskDialog());

        // Example: Fetch tasks
        fetchTasks();
    }

    private void insertNewTask(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> {
            long id = taskDao.insert(task);
            Log.d("MainActivity", "Task inserted with ID: " + id);
            runOnUiThread(this::fetchTasks);// Refresh the task list
        });

        Toast.makeText(MainActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
    }

    private void fetchTasks() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Task> tasks = taskDao.getAllTasks();
            Log.d("MainActivity", "Tasks fetched: " + tasks);
            runOnUiThread(() -> taskAdapter.setTasks(tasks)); // Update UI on the main thread
        });

        Toast.makeText(MainActivity.this, "Fetched todos successfully", Toast.LENGTH_SHORT).show();
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dailog, null);
        builder.setView(dialogView);

        final EditText inputTitle = dialogView.findViewById(R.id.nameEdit);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = inputTitle.getText().toString();
            if (!title.trim().isEmpty()) {
                Task newTask = new Task();
                newTask.setTitle(title);
                newTask.setCompleted(false);

                insertNewTask(newTask);
            } else {
                Toast.makeText(MainActivity.this, "Task title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
