package id.yasira.simplenoteapp.network;

import java.util.List;

import id.yasira.simplenoteapp.model.Note;
import id.yasira.simplenoteapp.model.ResponNote;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

public interface NoteService {

    @GET("read.php")
    Call<List<Note>> getNotes();

    @POST("create.php")
    Call<ResponNote> createNotes(@Body Note note);

    @POST("update.php")
    Call<ResponNote> updateNotes(@Body Note note);

    @HTTP(method = "DELETE", path = "delete.php", hasBody = true)
    Call<ResponNote> deleteNotes(@Body Note note);
}
