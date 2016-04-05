package com.tzc.mediaplay;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    MediaPlayer player;
    SurfaceView surface;
    SurfaceHolder surfaceHolder;
    Button play, pause, stop;
    Button b;
    Button i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (Button) findViewById(R.id.button1);
        pause = (Button) findViewById(R.id.button2);
        stop = (Button) findViewById(R.id.button3);
        surface = (SurfaceView) findViewById(R.id.surface);
        i = (Button) findViewById(R.id.button5);
        surfaceHolder = surface.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setFixedSize(320, 220);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//Surface类型

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
            }
        });
        b = (Button) findViewById(R.id.button4);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File("sdcard/haha");
                try {
                    if (!file.exists()) {
                        file.createNewFile();

                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    String s = "abcdefghijklmnopqrstuvwxyzsiofjwefjldasjfsdfjweofjlksdajfsdajfowefjsdafjds";
                    fileOutputStream.write(s.getBytes("UTF-8"));
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File("/sdcard/Movies/aa.mp4");
                String insert = "insertinsert";
                try {
                    writeData(file, 0, insert.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
            }
        });

    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
//必须在surface创建后才能初始化MediaPlayer,否则不会显示图像
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDisplay(surfaceHolder);
        //设置显示视频显示在SurfaceView上
        try {
            File file = new File("/sdcard/Movies/aa.mp4");
            FileInputStream fi1 = new FileInputStream(file);
            FileDescriptor fd1 = fi1.getFD();
            player.setDataSource(fd1, 12, file.length());
            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean writeData(File file, long skip, byte[] data) {
        boolean result = false;
        try {
            RandomAccessFile rfile;
            if (file.exists()) {
                rfile = new RandomAccessFile(file, "rw");
                rfile.seek(skip);
                byte[] other = new byte[(int) (file.length() - skip)];
                rfile.read(other);
                rfile = new RandomAccessFile(file, "rw");
                rfile.seek(skip);
                rfile.write(data);
                rfile = new RandomAccessFile(file, "rw");
                rfile.seek(data.length + skip);
                rfile.write(other);
                rfile.close();
                result = true;
            }
        } catch (Exception e) {

        }
        return result;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();
        //Activity销毁时停止播放，释放资源。不做这个操作，即使退出还是能听到视频播放的声音
    }
}
