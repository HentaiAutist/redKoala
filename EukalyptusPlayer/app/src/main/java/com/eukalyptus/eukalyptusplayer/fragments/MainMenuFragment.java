package com.eukalyptus.eukalyptusplayer.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eukalyptus.eukalyptusplayer.R;
import com.eukalyptus.eukalyptusplayer.objects.MusicItem;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class MainMenuFragment extends Fragment implements View.OnClickListener {


    public MainMenuFragment( ArrayList<MusicItem> currentPlay, ImageView backgroundImage) {
        this.currentPlay = currentPlay;
        this.backgroundImage = backgroundImage;
    }



    private TextView all;
    private ImageView catalogs;
    private ImageView playlists;
    private ImageView albums;
    private ImageView artists;
    private ImageView web_search;
    private ImageView settings;
    private FragmentTransaction fTrans;
    private FragmentSongList songsListFrag;
    private ArrayList<MusicItem> currentPlay;
    private ImageView backgroundImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu,container,false);
        all = view.findViewById(R.id.All);
        catalogs = view.findViewById(R.id.Catalogs);
        playlists = view.findViewById(R.id.PlLists);
        albums = view.findViewById(R.id.Albums);
        artists = view.findViewById(R.id.Artists);
        web_search = view.findViewById(R.id.WebSearch);
        settings = view.findViewById(R.id.Setting);



        all.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        songsListFrag = new FragmentSongList(currentPlay, backgroundImage);
        fTrans = getActivity().getSupportFragmentManager().beginTransaction();
        switch(v.getId()) {
            case R.id.All:
//                GlobalVar.active_frag = 1;
                fTrans.replace(R.id.layout_fragment, songsListFrag);
                fTrans.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                break;
            default:
                break;
        }
    }



}
