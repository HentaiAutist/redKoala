package com.eukalyptus.eukalyptusplayer.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.eukalyptus.eukalyptusplayer.R;
import com.eukalyptus.eukalyptusplayer.interfaces.SongListOnClick;
import com.eukalyptus.eukalyptusplayer.objects.MusicItem;
import com.eukalyptus.eukalyptusplayer.service.MusicService;
import com.eukalyptus.eukalyptusplayer.utils.StorageUtil;

import java.util.ArrayList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MyViewHolder> implements Filterable {

    private Context context;
    public static LayoutInflater lInflater;
    public static ArrayList<MusicItem> musicList;//List of music
    private static ArrayList<MusicItem> musicListFiltered;
    private int mDataset;
    private SongListOnClick serviceManager;

    private ImageView backgroundImage;

    private ImageView prevPause_img = null;
    private ImageView Pause_img;

    private int audioIndex = -1;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    musicListFiltered = musicList;
                } else {
                    ArrayList<MusicItem> filteredList = new ArrayList<>();
                    for (MusicItem row : musicList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getArtist().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    musicListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = musicListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                musicListFiltered = (ArrayList<MusicItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView MusicTittle;
        public TextView MusicAuthor;
        public ImageView Play_Pause;
        public ImageView MusicImage;

        public MyViewHolder(View view) {
            super(view);
            MusicTittle = view.findViewById(R.id.cmName);
            MusicAuthor = view.findViewById(R.id.cmAuthor);
            Play_Pause = (ImageView) view.findViewById(R.id.cmPlay_Pause);
            MusicImage = (ImageView) view.findViewById(R.id.cmImage);
        }

    }



    public SongListAdapter(Context context, ArrayList<MusicItem> arrayList, ImageView  img, SongListOnClick songListOnClick) {
        this.context = context;
        lInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.serviceManager = songListOnClick;
        this.musicList = arrayList;
        this.musicListFiltered = arrayList;
        backgroundImage = img;
      // service = context.;
    }



    MusicItem getMusicItem(int position){
        return ((MusicItem) getItem(position));
    }

    public int getCount() {
        return musicListFiltered.size();
    }

    public MusicItem getItem(int position) {
        return  musicListFiltered.get(position);
    }

    @NonNull
    @Override
    public SongListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view fill
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter, parent, false);
        MyViewHolder vh = new MyViewHolder(view);

        vh.Play_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePlay_Pause(Integer.parseInt(String.valueOf(view.getTag())), view, serviceManager.playingCheck());
            }
        });

//        vh.Play_Pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(GlobalVar.Tag!=(int) v.getTag()) {
//                    GlobalVar.musicSrv.setSong(Integer.parseInt(v.getTag().toString()));
//                    GlobalVar.musicSrv.Play();
//                    GlobalVar.Tag = (int) v.getTag();
//                    changePlay_Pause(GlobalVar.Tag, v, true, MusicControlFrag.mcPlay_Pause_but);
//                }else{
//                    GlobalVar.Tag = (int) v.getTag();
//                    if( GlobalVar.musicSrv.isPng()){
//                        GlobalVar.musicSrv.pausePlayer();
//                        changePlay_Pause(GlobalVar.Tag, v,  false, MusicControlFrag.mcPlay_Pause_but);
//                    }else{
//                        GlobalVar.musicSrv.go();
//                        changePlay_Pause(GlobalVar.Tag, v,  true, MusicControlFrag.mcPlay_Pause_but);
//                    }
//                }
//            }
//        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SongListAdapter.MyViewHolder holder, int position) {
        //set data

        MusicItem item = getMusicItem(position);

        holder.MusicTittle.setText(item.getTitle());
        holder.MusicAuthor.setText(item.getArtist());
        holder.Play_Pause.setTag(position);
        holder.MusicImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.default_music_image));   //item.getImage()

        if(position == audioIndex)
            holder.Play_Pause.setImageResource(R.drawable.icon_pause);
        else
            holder.Play_Pause.setImageResource(R.drawable.icon_play);

//        if (GlobalVar.LastPlayedID == item.getID()) {
//            if(GlobalVar.musicSrv.isPng())
//                holder.Play_Pause.setImageResource(R.drawable.pause);
//            else
//                holder.Play_Pause.setImageResource(R.drawable.play);
//            prevPause_img = holder.Play_Pause;
//        }else
//            holder.Play_Pause.setImageResource(R.drawable.play);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return musicListFiltered.size();
    }

    public void changePlay_Pause(int tag, View v, boolean ispng) {    // ImageView mcPlay
        if (!ispng) {
//           backgroundImage.setImageBitmap(GlobalVar.songList.get(GlobalVar.Tag).getImage())
            StorageUtil storage = new StorageUtil(context);
            audioIndex = Integer.parseInt(String.valueOf(v.getTag()));
            Pause_img = v.findViewWithTag(tag);
            Pause_img.setImageResource(R.drawable.icon_pause);
            if (prevPause_img==null)
                prevPause_img = v.findViewWithTag(tag);
            else {
                if (prevPause_img.getTag().equals(Pause_img.getTag())) {
                    //resume
                } else {
                    //play new
                    storage.storeAudio(musicListFiltered);
                    storage.storeAudioIndex(tag);
                    try {
//                        service.playNew();
                        Log.e("DDDDDD", "DDDDDDDDDDDDDDDD");
                        serviceManager.playNewSong();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    prevPause_img.setImageResource(R.drawable.icon_play);
                    prevPause_img = v.findViewWithTag(tag);
                }
            }
        } else {
           //pause
            audioIndex = -1;
            Pause_img.setImageResource(R.drawable.play);
            //prevPause_img = null;
        }
    }

}



