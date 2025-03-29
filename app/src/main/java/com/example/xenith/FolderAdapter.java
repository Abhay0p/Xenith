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

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private final List<MusicFolder> folders;
    private final Context context;
    private final OnFolderClickListener listener;

    public interface OnFolderClickListener {
        void onFolderClick(MusicFolder folder);
    }

    public FolderAdapter(Context context, List<MusicFolder> folders, OnFolderClickListener listener) {
        this.context = context;
        this.folders = folders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        MusicFolder folder = folders.get(position);
        holder.bind(folder, listener);
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public static class FolderViewHolder extends RecyclerView.ViewHolder {
        private final TextView folderName;
        private final TextView folderPath;
        private final ImageView folderIcon;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folder_name);
            folderPath = itemView.findViewById(R.id.folder_path);
            folderIcon = itemView.findViewById(R.id.folder_icon);
        }

        public void bind(final MusicFolder folder, final OnFolderClickListener listener) {
            folderName.setText(folder.getName());
            folderPath.setText(folder.getPath());
            folderIcon.setImageResource(R.drawable.ic_folder);

            itemView.setOnClickListener(v -> listener.onFolderClick(folder));
        }
    }
}