package com.t3h.mediaonline;

import com.google.gson.annotations.SerializedName;


public class ItemSongOnline  {
    @SerializedName("Title")
    private String title;

    @SerializedName("Avatar")
    private String avatar;

    @SerializedName("UrlJunDownload")
    private String link;

    @SerializedName("LyricsUrl")
    private String lyricsUrl;


    @SerializedName("Artist")
    private String artist;

    public String getTitle() {
        return title;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLink() {
        return link;
    }

    public String getLyricsUrl() {
        return lyricsUrl;
    }

    public String getArtist() {
        return artist;
    }
}
