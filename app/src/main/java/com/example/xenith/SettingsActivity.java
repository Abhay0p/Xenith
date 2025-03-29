package com.example.xenith;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_STORAGE_PERMISSION = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        MaterialCardView refreshLibraryCard = findViewById(R.id.refresh_library_card);
        MaterialCardView helpCard = findViewById(R.id.help_card);
        ImageButton backButton = findViewById(R.id.back_button);

        // Set click listeners
        refreshLibraryCard.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);
            } else {
                refreshMusicLibrary();
            }
        });

        helpCard.setOnClickListener(v -> sendHelpEmail());

        backButton.setOnClickListener(v -> finish());
    }

    private void refreshMusicLibrary() {
        // Implement your library refresh logic here
        Toast.makeText(this, "Refreshing music library...", Toast.LENGTH_SHORT).show();

        // You might want to broadcast this to your MainActivity
        Intent refreshIntent = new Intent("MUSIC_LIBRARY_REFRESH");
        sendBroadcast(refreshIntent);
    }

    private void sendHelpEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:S24cseu2515@bennett.edu.in"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "XENITH Music Player Help");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                refreshMusicLibrary();
            } else {
                Toast.makeText(this, "Permission needed to refresh library", Toast.LENGTH_SHORT).show();
            }
        }
    }
}