package com.t3h.mediaonline;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;



public class SongOnlineAdapter extends RecyclerView.Adapter<SongOnlineAdapter.ViewHolderSong> {
    private ISongOnlineAdapter mInterf;

    public SongOnlineAdapter(ISongOnlineAdapter interf) {
        mInterf = interf;
    }

    @Override
    public ViewHolderSong onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_music_online, parent, false);
        return new ViewHolderSong(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderSong holder, final int position) {
        ItemSongOnline data = mInterf.getData(position);
        holder.tvArtist.setText(data.getArtist());
        holder.tvNameSong.setText(data.getTitle());
        Picasso.with(holder.ivAvatar.getContext())
                .load(data.getAvatar())
                .placeholder(android.R.color.background_dark)
                .resize(60,60)
                .into(holder.ivAvatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterf.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInterf.getCount();
    }

    static class ViewHolderSong extends RecyclerView.ViewHolder {
        private TextView tvNameSong;
        private TextView tvArtist;
        private ImageView ivAvatar;

        public ViewHolderSong(View itemView) {
            super(itemView);
            tvArtist = (TextView) itemView.findViewById(R.id.tv_artist);
            tvNameSong = (TextView) itemView.findViewById(R.id.tv_name);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }

    public interface ISongOnlineAdapter {
        int getCount();

        ItemSongOnline getData(int position);

        void onClickItem(int position);
    }
}
