package com.example.xenith;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.xenith.R;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_STORAGE_PERMISSION = 101;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageButton settingsBtn, playPauseBtn;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        settingsBtn = findViewById(R.id.settings_btn);
        playPauseBtn = findViewById(R.id.play_pause_btn);

        // Setup ViewPager with tabs
        setupViewPager();

        // Request storage permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            loadSongs();
        }

        // Set click listeners
        settingsBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        playPauseBtn.setOnClickListener(v -> togglePlayPause());
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongsFragment(), getString(R.string.tab_songs));
        adapter.addFragment(new AlbumsFragment(), getString(R.string.tab_albums));
        adapter.addFragment(new PlaylistsFragment(), getString(R.string.tab_playlists));
        adapter.addFragment(new FoldersFragment(), getString(R.string.tab_folders));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Set tab icons and content descriptions
        int[] tabIcons = {
                R.drawable.ic_music_note,
                R.drawable.ic_album,
                R.drawable.ic_playlist,
                R.drawable.ic_folder
        };

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setIcon(tabIcons[i]);
                tab.setContentDescription(adapter.getPageTitle(i));
            }
        }
        String[] tabDescriptions = {
                getString(R.string.tab_songs_desc),
                getString(R.string.tab_albums_desc),
                getString(R.string.tab_playlists_desc),
                getString(R.string.tab_folders_desc)
        };

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setContentDescription(tabDescriptions[i]);
            }
        }
    }

    private void loadSongs() {
        // Implement your song loading logic here
        Toast.makeText(this, "Loading songs...", Toast.LENGTH_SHORT).show();
    }

    private void togglePlayPause() {
        isPlaying = !isPlaying;
        if (isPlaying) {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            playPauseBtn.setContentDescription(getString(R.string.pause_desc));
            // Start playback
        } else {
            playPauseBtn.setImageResource(R.drawable.ic_play);
            playPauseBtn.setContentDescription(getString(R.string.play_desc));
            // Pause playback
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSongs();
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }
}