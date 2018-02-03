package com.example.oways.firebaserealtimedatabase;

/**
 * Created by Oways on 02-Feb-18.
 */

public class Artist {
String artistId;
String artistName;
String artistGenre;

    public Artist() {
    }

    public Artist(String artistId, String artistName, String artistGenre) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistGenre = artistGenre;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistGenre() {
        return artistGenre;
    }
}
