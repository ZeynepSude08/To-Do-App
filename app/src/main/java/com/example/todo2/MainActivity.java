package com.example.todo2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private JsonPlaceholderApi jsonPlaceholderApi;
    private LinearLayout layout;
    private AlertDialog addDialog;
    private AlertDialog editDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button add = findViewById(R.id.add);
        layout = findViewById(R.id.container);

        Retrofit retrofit = ApiClient.getClient();
        jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

        buildAddDialog();
        buildEditDialog();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.show();
            }
        });

        fetchTodos(5, 0); // Fetch existing todos
    }

    private void buildAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dailog, null);

        final EditText name = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Enter your Task");
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createTodo(name.getText().toString());
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        addDialog = builder.create();
    }

    private void buildEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dailog, null);

        final EditText name = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Edit Your Task");
        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Placeholder: Use a method to handle the update logic here
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        editDialog = builder.create();
    }

    private void addCard(final int id, String name) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.delete);
        Button edit = view.findViewById(R.id.edit);

        nameView.setText(name);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(id, name); // Pass ID and current title
            }
        });

        layout.addView(view);
    }

    private void showEditDialog(final int id, String currentTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dailog, null);

        final EditText nameEditText = view.findViewById(R.id.nameEdit);
        nameEditText.setText(currentTitle); // Pre-fill with current title

        builder.setView(view);
        builder.setTitle("Edit Your Task");
        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateTodo(id, nameEditText.getText().toString());
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog editDialog = builder.create();
        editDialog.show();
    }

    private void updateTodo(int id, String newTitle) {
        Todo updatedTodo = new Todo();
        updatedTodo.setId(id);
        updatedTodo.setTitle(newTitle);

        Call<Todo> call = jsonPlaceholderApi.updateTodo(id, updatedTodo);
        call.enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                if (!response.isSuccessful()) {
                    Log.e("MainActivity", "Error Code: " + response.code());
                    return;
                }

                // Handle successful update
                Toast.makeText(MainActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                Log.e("MainActivity", t.getMessage());
            }
        });
    }

    private void fetchTodos(int limit, int offset) {
        Call<List<Todo>> call = jsonPlaceholderApi.getTodos(limit, offset);
        call.enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                if (!response.isSuccessful()) {
                    Log.e("MainActivity", "Error Code: " + response.code());
                    return;
                }

                List<Todo> todos = response.body();
                for (Todo todo : todos) {
                    addCard(todo.getId(), todo.getTitle());
                }
                Log.d("MainActivity", "Fetched todos successfully");
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e("MainActivity", t.getMessage());
            }
        });
    }

    private void createTodo(String title) {
        Todo newTodo = new Todo();
        newTodo.setUserId(1);
        newTodo.setTitle(title);
        newTodo.setCompleted(false);

        Call<Todo> call = jsonPlaceholderApi.createTodo(newTodo);
        call.enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                if (!response.isSuccessful()) {
                    Log.e("MainActivity", "Error Code: " + response.code());
                    return;
                }

                Todo todo = response.body();
                addCard(todo.getId(), todo.getTitle());
                Log.d("MainActivity", "Created todo successfully: " + todo.getTitle());
            }

            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                Log.e("MainActivity", t.getMessage());
            }
        });
    }
}
