package com.udacity.maluleque.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    private String id;
    private String key;
    private int size;
    private String name;
    private String site;

    public Trailer() {
    }

    public Trailer(String id, String key, int size, String name, String site) {
        this.id = id;
        this.key = key;
        this.size = size;
        this.name = name;
        this.site = site;
    }

    protected Trailer(Parcel in) {
        id = in.readString();
        key = in.readString();
        size = in.readInt();
        name = in.readString();
        site = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeInt(size);
        dest.writeString(name);
        dest.writeString(site);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
