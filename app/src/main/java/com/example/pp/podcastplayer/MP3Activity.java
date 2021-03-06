package com.example.pp.podcastplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MP3Activity extends AppCompatActivity {
    String path_mp3 = "";
    String path_slika = "";
    String naslov = "";
    MediaPlayer MP = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ImageView IV = (ImageView) findViewById(R.id.slika);
        ImageButton IBPlay = (ImageButton) findViewById(R.id.playbutton);
        ImageButton IBPause = (ImageButton) findViewById(R.id.pausebutton);
        TextView TVNaslov = (TextView) findViewById(R.id.naslovpesmi);
        TextView TVDur = (TextView) findViewById(R.id.duration);
        final TextView TVCurr = (TextView) findViewById(R.id.current);
        IBPause.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                path_mp3 = bundle.getString("mp3");
                path_slika = bundle.getString("slika");
                naslov = bundle.getString("naslov");
            }
        }

        if(naslov!="" && path_mp3!=""){



               MP.setAudioStreamType(AudioManager.STREAM_MUSIC);
               Uri uri = Uri.parse(path_mp3);
            try {
                MP.setDataSource(getApplicationContext(),uri);
                MP.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            TVNaslov.setText(naslov);
            Date date = new Date(MP.getDuration());
            DateFormat formatter = new SimpleDateFormat("mm:ss");
            String dateFormatted = formatter.format(date);
                TVDur.setText(dateFormatted);
                System.out.println(path_slika);
                Bitmap bitmap = BitmapFactory.decodeFile(path_slika);
                IV.setImageBitmap(bitmap);


        }

        IBPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MP.isPlaying()){
                    MP.start();
                    ImageButton IBPause = (ImageButton) findViewById(R.id.pausebutton);
                    IBPause.setVisibility(View.VISIBLE);
                    ImageButton IBPlay = (ImageButton) findViewById(R.id.playbutton);
                    IBPlay.setVisibility(View.GONE);
                    final Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (MP != null && MP.isPlaying()) {
                                        TVCurr.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Date date = new Date(MP.getCurrentPosition());
                                                DateFormat formatter = new SimpleDateFormat("mm:ss");
                                                String dateFormatted = formatter.format(date);
                                                TVCurr.setText(dateFormatted);
                                            }
                                        });
                                    } else {
                                        timer.cancel();
                                        timer.purge();
                                    }
                                }
                            });
                        }
                    }, 0, 1000);


                }
            }
        });

        IBPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MP.isPlaying()){
                    MP.pause();
                    ImageButton IBPause = (ImageButton) findViewById(R.id.pausebutton);
                    IBPause.setVisibility(View.GONE);
                    ImageButton IBPlay = (ImageButton) findViewById(R.id.playbutton);
                    IBPlay.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        MP.stop();
        finish();
        return true;
    }

}
