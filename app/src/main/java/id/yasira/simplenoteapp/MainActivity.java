package id.yasira.simplenoteapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import id.yasira.simplenoteapp.model.Note;
import id.yasira.simplenoteapp.network.NoteService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout srlNote;
    Button btnCreateNote;
    RecyclerView rvNotes;
    NotesAdapter notesAdapter;
    List<Note> noteList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        noteList = new ArrayList<>();
        srlNote = findViewById(R.id.srl_note);
        btnCreateNote = findViewById(R.id.btn_add_note);
        rvNotes = findViewById(R.id.rv_note);
        getAllCourses();

        srlNote.setOnRefreshListener(() -> {
            getAllCourses();
        });

        btnCreateNote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DetailNoteActivity.class);
            startActivity(intent);
        });

    }

    private void getAllCourses() {
        srlNote.setRefreshing(true);
        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://natawarna.atwebpages.com/")
                // on below line we are calling add
                // Converter factory as Gson converter factory.
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        NoteService noteService = retrofit.create(NoteService.class);

        // on below line we are calling a method to get all the courses from API.
        Call<List<Note>> call = noteService.getNotes();

        // on below line we are calling method to enqueue and calling
        // all the data from array list.
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                // inside on response method we are checking
                // if the response is success or not.
                if (response.isSuccessful()) {

                    // below line is to add our data from api to our array list.
                    noteList = response.body();
                    srlNote.setRefreshing(false);

                    // below line we are running a loop to add data to our adapter class.
                    for (int i = 0; i < noteList.size(); i++) {
                        notesAdapter = new NotesAdapter(noteList, MainActivity.this);

                        // below line is to set layout manager for our recycler view.
                        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);

                        // setting layout manager for our recycler view.
                        rvNotes.setLayoutManager(manager);

                        // below line is to set adapter to our recycler view.
                        rvNotes.setAdapter(notesAdapter);
                    }

                    notesAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClicked(Integer id, String title, String body) {
                            Intent intent = new Intent(MainActivity.this, DetailNoteActivity.class);
                            intent.putExtra(DetailNoteActivity.EXTRA_ID,id);
                            intent.putExtra(DetailNoteActivity.EXTRA_TITLE, title);
                            intent.putExtra(DetailNoteActivity.EXTRA_BODY, body);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                // in the method of on failure we are displaying a
                // toast message for fail to get data.
                Log.d("TAG", "onFailure: "+t.getMessage());
                Toast.makeText(MainActivity.this, "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}