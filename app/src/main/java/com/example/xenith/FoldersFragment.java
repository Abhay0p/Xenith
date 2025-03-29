package com.example.xenith;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FoldersFragment extends Fragment {

    private RecyclerView foldersRecyclerView;
    private FolderAdapter folderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders, container, false);

        foldersRecyclerView = view.findViewById(R.id.folders_recycler_view);
        foldersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            loadMusicFolders();
        } else {
            Toast.makeText(getContext(), "Storage permission required", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadMusicFolders() {
        List<MusicFolder> folders = new ArrayList<>();
        File root = Environment.getExternalStorageDirectory();

        findMusicFolders(root, folders);

        folderAdapter = new FolderAdapter(folders, folder -> {
            // Now folder.getName() will work because MusicFolder has the method
            Toast.makeText(getContext(), "Showing songs from: " + folder.getName(), Toast.LENGTH_SHORT).show();
        });

        foldersRecyclerView.setAdapter(folderAdapter);
    }

    private void findMusicFolders(File directory, List<MusicFolder> folders) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (containsMusicFiles(file)) {
                        folders.add(new MusicFolder(file.getName(), file.getAbsolutePath()));
                    }
                    findMusicFolders(file, folders);
                }
            }
        }
    }

    private boolean containsMusicFiles(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isMusicFile(file.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isMusicFile(String fileName) {
        int lastDot = fileName.lastIndexOf(".");
        if (lastDot == -1) return false;

        String extension = fileName.substring(lastDot + 1).toLowerCase();
        return extension.equals("mp3") || extension.equals("wav") ||
                extension.equals("ogg") || extension.equals("m4a") ||
                extension.equals("flac");
    }
}