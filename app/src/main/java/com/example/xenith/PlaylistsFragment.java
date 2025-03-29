package com.example.xenith;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsFragment extends Fragment {

    private RecyclerView playlistsRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private FloatingActionButton addPlaylistFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        playlistsRecyclerView = view.findViewById(R.id.playlists_recycler_view);
        addPlaylistFab = view.findViewById(R.id.add_playlist_fab);

        // Setup RecyclerView
        playlistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        playlistAdapter = new PlaylistAdapter(getPlaylists());
        playlistsRecyclerView.setAdapter(playlistAdapter);

        addPlaylistFab.setOnClickListener(v -> {
            showCreatePlaylistDialog();
        });

        return view;
    }

    private List<Playlist> getPlaylists() {
        // In a real app, you would load these from a database
        List<Playlist> playlists = new ArrayList<>();
        playlists.add(new Playlist("Favorites", 12));
        playlists.add(new Playlist("Workout", 8));
        playlists.add(new Playlist("Chill", 5));
        return playlists;
    }

    private void showCreatePlaylistDialog() {
        // Implement dialog to create new playlist
        Toast.makeText(getContext(), "Create new playlist", Toast.LENGTH_SHORT).show();
    }
}