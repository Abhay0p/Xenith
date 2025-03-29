package com.example.xenith;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final List<Song> songs;
    private final Context context;
    private final OnSongClickListener listener;

    public interface OnSongClickListener {
        void onSongClick(Song song);
        void onSongLongClick(Song song);
    }

    public SongAdapter(Context context, List<Song> songs, OnSongClickListener listener) {
        this.context = context;
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.bind(song, listener);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        private final TextView songTitle;
        private final TextView songArtist;
        private final TextView songDuration;
        private final ImageView songArt;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
            songArtist = itemView.findViewById(R.id.song_artist);
            songDuration = itemView.findViewById(R.id.song_duration);
            songArt = itemView.findViewById(R.id.song_art);
        }

        public void bind(final Song song, final OnSongClickListener listener) {
            songTitle.setText(song.getTitle());
            songArtist.setText(song.getArtist());
            songDuration.setText(formatDuration(song.getDuration()));

            // Load album art - you'd implement this using Glide/Picasso
            songArt.setImageResource(R.drawable.default_art);

            itemView.setOnClickListener(v -> listener.onSongClick(song));
            itemView.setOnLongClickListener(v -> {
                listener.onSongLongClick(song);
                return true;
            });
        }

        private String formatDuration(long duration) {
            long minutes = (duration / 1000) / 60;
            long seconds = (duration / 1000) % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
