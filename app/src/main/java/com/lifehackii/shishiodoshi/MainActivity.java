package com.lifehackii.shishiodoshi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 音楽開始ボタン
        Button playButton = findViewById(R.id.play);
        // リスナーをボタンに登録
        playButton.setOnClickListener( v -> {
            // 音楽再生
            audioPlay();
            // playボタンを無効にする処理
            playButton.setEnabled(false);
        });

        // 音楽停止ボタン
        Button stopButton = findViewById(R.id.stop);
        // リスナーをボタンに登録
        stopButton.setOnClickListener( v -> {
            if (mediaPlayer != null) {
                // 音楽停止
                audioStop();
                // playボタンを有効にする処理
                playButton.setEnabled(true);
            }
        });
    }

    private boolean audioSetup(){
        // インスタンスを生成
        mediaPlayer = new MediaPlayer();
        // ループ設定をオン
        mediaPlayer.setLooping(true);
        // 音楽ファイル名 or パス名
        String filePath = "Shishiodoshi_long_v2.mp3";
        boolean fileCheck = false;
        // assetsからmp3ファイルを読み込み
        try(AssetFileDescriptor assetFileDescriptor = getAssets().openFd(filePath)) {
            // MediaPlayerに読み込んだ音楽ファイルを指定
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            //音量調整を端末のボタンに任せる
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            fileCheck = true;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return fileCheck;
    }

    private void audioPlay() {
        if (mediaPlayer == null ) {
            // audioファイルを読み出し
            if (audioSetup()) {
                Toast.makeText(getApplication(), "Read audio file", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "Error read audio file", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            // 繰り返し再生する場合
            mediaPlayer.stop();
            mediaPlayer.reset();
            // リソースの解放
            mediaPlayer.release();
        }

        // 再生する
        mediaPlayer.start();
        
        // lambda
        mediaPlayer.setOnCompletionListener( mp -> {
            Log.d("debug", "end of audio");
            audioStop();
        });
    }
    private void audioStop() {
        // 再生終了
        mediaPlayer.stop();
        // リセット
        mediaPlayer.reset();
        // リソースの解放
        mediaPlayer.release();
        mediaPlayer = null;
    }

    // 以下メニュー関連
    // メニュー作成
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    // メニュー押下時の処理
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String message = "「" + item.getTitle() + "」が押されました。";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return true;
    }
}