package id.yasira.simplenoteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.yasira.simplenoteapp.model.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.RecyclerViewHolder>{

    private List<Note> noteList;
    private Context mContext;
    private OnItemClickListener itemClickListener;

    public NotesAdapter(List<Note> noteList, Context mContext) {
        this.noteList = noteList;
        this.mContext = mContext;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClicked(Integer id, String title, String body);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_note_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvBody.setText(note.getBody());
        holder.cvNote.setOnClickListener(v ->
                itemClickListener.onItemClicked(note.getId(),note.getTitle(), note.getBody())
        );
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        CardView cvNote;
        TextView tvTitle;
        TextView tvBody;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            cvNote = itemView.findViewById(R.id.cv_note);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvBody = itemView.findViewById(R.id.tv_body);
        }


    }
}
