package com.dsm.platform.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import com.dsm.platform.R;

/**
 * Created by yanfa on 2016/11/11.
 */

public class MusicUtil {
    private static final Object object = new Object();
    private static MediaPlayer mMediaPlayer;
    private static final int MUSIC = 0x2001;
    private static final String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
    };

    /**
     * 获取音乐列表
     *
     * @param context
     * @param onMusicResult
     */
    public static void getMusicList(Context context, OnMusicResult onMusicResult) {
        MusicUtil.onMusicResult = onMusicResult;
        Intent intent = new Intent();
        intent.setType("audio/*"); //选择音频
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) context).startActivityForResult(intent, MUSIC);
    }

    /**
     * 获取音乐列表回调
     *
     * @param context
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (requestCode == MUSIC && data != null) {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor;
            try {
                cursor = contentResolver.query(data.getData(), projection, null, null, null);
            } catch (Exception e) {
                onResult(null, null, context.getString(R.string.plese_select_well_voice_file));
                return;
            }
            if (cursor == null) {
                onResult(null, null, context.getString(R.string.music_file_does_not_exist));
            } else {
                if (cursor.moveToFirst()) {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    Uri uri = data.getData();
                    onResult(name, uri, context.getString(R.string.local_music_selection_succeed));
                } else {
                    onResult(null, null, context.getString(R.string.music_file_does_not_exist));
                }
                cursor.close();
            }
        } else {
            onResult(null, null, context.getString(R.string.cancel_local_music_selection));
        }
    }

    private static void onResult(String name, Uri uri, String hint) {
        if (MusicUtil.onMusicResult != null) {
            MusicUtil.onMusicResult.musicResult(name, uri, hint);
        }
    }

    public interface OnMusicResult {
        void musicResult(String name, Uri uri, String hint);
    }

    private static OnMusicResult onMusicResult;

    public static void setOnMusicResult(OnMusicResult musicResult) {
        MusicUtil.onMusicResult = musicResult;
    }

    /**
     * 播放音乐
     *
     * @param context
     * @param path    路径
     */
    public static void playMusic(Context context, String path) {
        synchronized (object) {
            try {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                }
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(context, Uri.parse(path));
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止音乐播放
     */
    public static void stopMusic() {
        synchronized (object) {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
        }
    }
}
