package com.eukalyptus.eukalyptusplayer.fragments;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eukalyptus.eukalyptusplayer.R;
import com.eukalyptus.eukalyptusplayer.adapters.SongListAdapter;
import com.eukalyptus.eukalyptusplayer.objects.MusicItem;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class FragmentSongList extends Fragment {

    private RecyclerView listView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SongListAdapter adapter;
    private ImageView backgroundImage;

    ArrayList<MusicItem> currentPlay;



    public FragmentSongList(ArrayList<MusicItem> currentPlay, ImageView imageBack) {
        // Required empty public constructor
        this.currentPlay = currentPlay;
        backgroundImage = imageBack;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list,
                container, false);
        listView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        listView.setLayoutManager(mLayoutManager);
        adapter = new SongListAdapter(getActivity(),currentPlay,backgroundImage);

        listView.setAdapter(adapter);


        return view;
    }
}
