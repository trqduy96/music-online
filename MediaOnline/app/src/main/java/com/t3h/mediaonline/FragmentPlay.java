package com.t3h.mediaonline;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class FragmentPlay extends Fragment implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, Animation.AnimationListener {
    private ItemSongOnline songOnline;
    private ImageView avatarImage;
    private TextView name;
    private TextView artist;
    private TextView progress;
    private SeekBar seekBar;
    private TextView duration;
    private Context context;
    private PlayerMusic playerMusic;
    private SimpleDateFormat setText;
    private long durations;
    private RotateAnimation animation;
    private ImageView btnControl;
    private ImageView btnPlayNext;
    private ImageView btnPlayLast;
    private MyAsyn asyntask;
    private boolean isPause = false;
    private float start = 0f;
    private float d;


    public void setSongOnline(ItemSongOnline songOnline) {
        this.songOnline = songOnline;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music, container, false);

        this.context = view.getContext();
        avatarImage = (ImageView) view.findViewById(R.id.image_view_album);
        name = (TextView) view.findViewById(R.id.text_view_name);
        artist = (TextView) view.findViewById(R.id.text_view_artist);
        progress = (TextView) view.findViewById(R.id.text_view_progress);
        duration = (TextView) view.findViewById(R.id.text_view_duration);
        btnControl = (ImageView) view.findViewById(R.id.button_play_toggle);
        btnPlayNext = (ImageView) view.findViewById(R.id.button_play_next);
        btnPlayLast = (ImageView) view.findViewById(R.id.button_play_last);
        animation = createAnimation(start);
        avatarImage.startAnimation(animation);
        btnControl.setOnClickListener(this);
        btnPlayNext.setOnClickListener(this);
        btnPlayLast.setOnClickListener(this);
        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);
        name.setText(songOnline.getTitle());
        artist.setText(songOnline.getArtist());
        Picasso.with(avatarImage.getContext())
                .load(songOnline.getAvatar())
                .placeholder(android.R.color.background_dark)
                .resize(300, 300)
                .into(avatarImage);
        renewSong(songOnline);


        return view;
    }


    void renewSong(ItemSongOnline songOnline) {

        if (playerMusic != null) {
            playerMusic.release();
        } else {
            playerMusic = new PlayerMusic();
        }

        playerMusic.inits(context, songOnline.getLink());
        if (playerMusic.getMediaPlayer() == null) {
            return;
        }
        playerMusic.prepare(this);
        if (playerMusic.getMediaPlayer() == null) {
            return;
        }
        durations = playerMusic.getMediaPlayer().getDuration();

        playerMusic.play();
        duration.setText(setText.format(durations));
        initsAsyntask();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setText = new SimpleDateFormat("mm:ss");
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        if (asyntask != null) {
            asyntask.isRunning = false;
            asyntask.cancel(true);
            asyntask = null;
        }
        if (playerMusic != null) {
            playerMusic.release();
        }
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
        btnControl.setImageResource(R.drawable.ic_replay);


    }

    private void initsAsyntask() {
        final int total = playerMusic.getMediaPlayer().getDuration();
        asyntask =
                new MyAsyn(total);
        asyntask.execute();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int percent = seekBar.getProgress();
        int total = playerMusic.getMediaPlayer().getDuration();
        playerMusic.seek(total * percent / 100);

    }


    private RotateAnimation createAnimation(float start) {
        RotateAnimation rotate = new RotateAnimation(start, start + 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


        rotate.setInterpolator(context, android.R.anim.linear_interpolator);

        rotate.setFillAfter(true);
        rotate.setDuration(5000);
        rotate.setRepeatCount(RotateAnimation.INFINITE);
        rotate.setRepeatMode(RotateAnimation.RESTART);
        rotate.setAnimationListener(this);
        return rotate;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_play_toggle:
                if (isPause == false) {
                    if (playerMusic == null) {
                        return;
                    }
                    playerMusic.pause();
                    isPause = true;
                    btnControl.setImageResource(R.drawable.ic_play);
                    asyntask.isRunning = false;
                    asyntask.cancel(true);
                    asyntask = null;
                    animation.cancel();
                    animation = null;


                } else {
                    if (playerMusic == null) {
                        return;
                    }
                    btnControl.setImageResource(R.drawable.ic_pause);
                    playerMusic.play();
                    isPause = false;
                    animation = createAnimation(start);
                    initsAsyntask();
                    avatarImage.startAnimation(animation);
                }
                break;
            case R.id.button_play_next:
                int positionNext = playerMusic.getMediaPlayer().getCurrentPosition();
                playerMusic.seek(positionNext + 5000);
                break;
            case R.id.button_play_last:
                int positionLast = playerMusic.getMediaPlayer().getCurrentPosition();
                playerMusic.seek(positionLast - 5000);
                break;
            default:
                break;
        }
//        if (v.getId() == R.id.button_play_toggle) {
//            if (isPause == false) {
//                if (playerMusic == null) {
//                    return;
//                }
//                playerMusic.pause();
//                isPause = true;
//                btnControl.setImageResource(R.drawable.ic_play);
//                asyntask.isRunning = false;
//                asyntask.cancel(true);
//                asyntask = null;
//                animation.cancel();
//                animation = null;
//
//
//            } else {
//                if (playerMusic == null) {
//                    return;
//                }
//                btnControl.setImageResource(R.drawable.ic_pause);
//                playerMusic.play();
//                isPause = false;
//                animation = createAnimation(start);
//                initsAsyntask();
//                avatarImage.startAnimation(animation);
//            }
//        }

    }

    @Override
    public void onAnimationStart(Animation animation) {
        Log.d("TAG", "onAnimationStart: " + animation.getDuration());
        avatarImage.setRotation(avatarImage.getRotation() % 360);
        d = 0;
        Log.d("TAG", "onAnimationStart: d= " + d);
    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (d > 5) {
            start = (d % 5) * 360 / 5 + start;
            avatarImage.setRotation(start);


        } else if (d <= 5) {
            start = (d * 360 / 5) + start;
            avatarImage.setRotation(start);
        }
        Log.d("TAG", "onAnimationStart: d= " + d);
        Log.d("TAG", "onAnimationStart: " + avatarImage.getRotation());


    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        d = 0;


    }

    private class MyAsyn extends AsyncTask<Void, Integer, Void> {
        private boolean isRunning = true;
        private int total;

        public MyAsyn(int total) {
            this.total = total;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (isRunning) {
                publishProgress(
                        playerMusic.getMediaPlayer()
                                .getCurrentPosition());
                if (animation != null) {
                    d = d + 0.5f;
                }
                SystemClock.sleep(500);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.setText(setText.format(values[0]));
            int persent = values[0] * 100 / total;
            seekBar.setProgress(persent);
        }


    }


    @Override
    public void onDestroy() {
        if (asyntask != null) {
            asyntask.isRunning = false;
            asyntask.cancel(true);
            asyntask = null;
        }

        if (playerMusic != null) {
            playerMusic.release();
        }
        super.onDestroy();
    }
}
