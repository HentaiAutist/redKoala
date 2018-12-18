package com.eukalyptus.eukalyptusplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;

import com.eukalyptus.eukalyptusplayer.fragments.MainMenuFragment;
import com.eukalyptus.eukalyptusplayer.objects.MusicItem;
import com.eukalyptus.eukalyptusplayer.service.MusicService;

import java.util.ArrayList;


//volum pilya pause skache do full
//як працюе дж сан
//юзати сервіс в іншому класі
public class MainActivity extends AppCompatActivity {
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.eukalyptus.eukalyptusplayer.PlayNewAudio";

    private MusicService player;
    boolean serviceBound = false;

    ImageView backgroundImage;
    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<MusicItem> currentPlay;
    ArrayList<MusicItem> currentDisplayed;

  //  private SongListAdapter adapter;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backgroundImage = findViewById(R.id.background_image);

        //permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            loadAudio();
        }


      //  adapter = new SongListAdapter(getApplicationContext(),currentPlay,backgroundImage);
        FragmentManager fm = getSupportFragmentManager();
        MainMenuFragment mf = new MainMenuFragment(currentPlay,backgroundImage);
        fm.beginTransaction().replace(R.id.layout_fragment,mf).commit();
        //play the first audio in the ArrayList
        playAudio(currentPlay.get(0).getData());


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        loadAudio();
                    }
                } else {
                    this.finish();
                }
                return;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void playAudio(String media) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//            storage.storeAudio(currentPlay);
//            storage.storeAudioIndex(0);

            Intent playerIntent = new Intent(this, MusicService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//            storage.storeAudioIndex(0);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent();  //Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }

    private void loadAudio() {
        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            currentPlay = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

//                MediaMetadataRetriever metaRetriver;
//                Bitmap songImage;
//
//                metaRetriver = new MediaMetadataRetriever();
//                metaRetriver.setDataSource(data);
//                byte[] art;
//                try {
//                    art = metaRetriver.getEmbeddedPicture();
//                    BitmapFactory.Options opt = new BitmapFactory.Options();
//                    opt.inSampleSize = 2;
//                    songImage = BitmapFactory.decodeByteArray(art, 0, art.length,opt);
//                }
//                catch (Exception e)
//                {
//                    songImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_music_image);
//                }


                // Save to audioList
                currentPlay.add(new MusicItem(data, title, album, artist)); // , songImage
            }
        }
        cursor.close();
    }



}
