package id.yasira.simplenoteapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import id.yasira.simplenoteapp.model.Note;
import id.yasira.simplenoteapp.model.ResponNote;
import id.yasira.simplenoteapp.network.NoteService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailNoteActivity extends AppCompatActivity {

    public static String EXTRA_ID = "ID";
    public static String EXTRA_TITLE = "TITLE";
    public static String EXTRA_BODY = "BODY";

    Toolbar toolbar;
    TextInputEditText tietTitle;
    TextInputEditText tietBody;
    Button btnSave;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        tietTitle = findViewById(R.id.tiet_title);
        tietBody = findViewById(R.id.tiet_body);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setEnabled(false);

        if (getIntent().getIntExtra(EXTRA_ID,0) != 0){
            getSupportActionBar().setTitle("Lihat Catatan");
            btnDelete.setEnabled(true);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteNote(getIntent().getIntExtra(EXTRA_ID,0));
                }
            });

            tietTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            tietBody.setText(getIntent().getStringExtra(EXTRA_BODY));
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!tietTitle.getText().toString().isEmpty() &&!tietBody.getText().toString().isEmpty()){
                        updateNote(getIntent().getIntExtra(EXTRA_ID,0),
                                tietTitle.getText().toString(),
                                tietBody.getText().toString());
                    }else {
                        Toast.makeText(getApplicationContext(),"Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }else {
            getSupportActionBar().setTitle("Tambah Catatan");
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!tietTitle.getText().toString().isEmpty() &&!tietBody.getText().toString().isEmpty()){
                        saveNote(tietTitle.getText().toString(),
                                tietBody.getText().toString());
                    }else {
                        Toast.makeText(getApplicationContext(),"Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }


    }

    private void saveNote(String title, String body){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://natawarna.atwebpages.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NoteService noteService = retrofit.create(NoteService.class);
        Note note = new Note(title,body);

        Call<ResponNote> call = noteService.createNotes(note);
        call.enqueue(new Callback<ResponNote>() {
            @Override
            public void onResponse(Call<ResponNote> call, Response<ResponNote> response) {

                if (response.isSuccessful()) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponNote> call, Throwable t) {
                // in the method of on failure we are displaying a
                // toast message for fail to get data.
                Log.d("TAG", "onFailure: "+t.getMessage());
                Toast.makeText(DetailNoteActivity.this, "Maaf Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteNote(Integer id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://natawarna.atwebpages.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NoteService noteService = retrofit.create(NoteService.class);
        Note note = new Note(id);

        Call<ResponNote> call = noteService.deleteNotes(note);
        call.enqueue(new Callback<ResponNote>() {
            @Override
            public void onResponse(Call<ResponNote> call, Response<ResponNote> response) {

                if (response.isSuccessful()) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponNote> call, Throwable t) {
                // in the method of on failure we are displaying a
                // toast message for fail to get data.
                Log.d("TAG", "onFailure: "+t.getMessage());
                Toast.makeText(DetailNoteActivity.this, "Maaf Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNote(Integer id, String title, String body){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://natawarna.atwebpages.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NoteService noteService = retrofit.create(NoteService.class);
        Note note = new Note(id,title,body);

        Call<ResponNote> call = noteService.updateNotes(note);
        call.enqueue(new Callback<ResponNote>() {
            @Override
            public void onResponse(Call<ResponNote> call, Response<ResponNote> response) {

                if (response.isSuccessful()) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponNote> call, Throwable t) {
                // in the method of on failure we are displaying a
                // toast message for fail to get data.
                Log.d("TAG", "onFailure: "+t.getMessage());
                Toast.makeText(DetailNoteActivity.this, "Maaf Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });

    }
}