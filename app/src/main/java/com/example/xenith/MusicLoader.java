package com.example.xenith;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import java.util.ArrayList;
import java.util.List;

public class MusicLoader {

    public interface MusicLoadCallback {
        void onMusicLoaded(List<Song> songs);
    }

    public static void loadSongs(Context context, MusicLoadCallback callback) {
        List<Song> songs = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();

        // Define the projection (columns we want to retrieve)
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,          // Full file path
                MediaStore.Audio.Media.ALBUM_ID,       // For album art
                MediaStore.Audio.Media.DISPLAY_NAME    // Alternative to TITLE
        };

        // Selection criteria (only music files)
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        // Sort order
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        try (Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder)) {

            if (cursor != null && cursor.moveToFirst()) {
                // Get column indices safely
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);

                do {
                    // Safely extract values
                    long id = cursor.getLong(idColumn);
                    String title = cursor.getString(titleColumn);
                    String artist = cursor.getString(artistColumn);
                    String album = cursor.getString(albumColumn);
                    long duration = cursor.getLong(durationColumn);
                    String path = cursor.getString(pathColumn);
                    long albumId = cursor.getLong(albumIdColumn);

                    // Create song object
                    songs.add(new Song(id, title, artist, album, albumId, duration, path));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        callback.onMusicLoaded(songs);
    }

    public static List<Song> getSongsFromFolder(Context context, String folderPath) {
        List<Song> folderSongs = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND " +
                MediaStore.Audio.Media.DATA + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + folderPath + "%"};

        try (Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);

                do {
                    folderSongs.add(new Song(
                            cursor.getLong(idColumn),
                            cursor.getString(titleColumn),
                            cursor.getString(artistColumn),
                            cursor.getString(albumColumn),
                            cursor.getLong(albumIdColumn),
                            cursor.getLong(durationColumn),
                            cursor.getString(pathColumn)
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return folderSongs;
    }
}