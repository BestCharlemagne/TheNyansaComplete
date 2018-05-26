package com.candeapps.thenyansacomplete.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mchan on 12/03/17.
 */
public class CookieParcelable implements Parcelable {
    private String  voyanceHostUrl;
    private String  data = "Nothing";

    public CookieParcelable(String voyanceHostUrl, String data) {
        this.voyanceHostUrl = voyanceHostUrl;
        this.data = data;
    }

    private CookieParcelable(Parcel parcel) {
        voyanceHostUrl = parcel.readString();
        data = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(voyanceHostUrl);
        dest.writeString(data);
    }

    public static final Parcelable.Creator<CookieParcelable> CREATOR = new Parcelable.Creator<CookieParcelable>() {
        @Override
        public CookieParcelable createFromParcel(Parcel source) {
            return new CookieParcelable(source);
        }

        @Override
        public CookieParcelable[] newArray(int size) {
            return new CookieParcelable[size];
        }
    };

    public String getData() {
        return data;
    }

    public String getVoyanceHostUrl() {
        return voyanceHostUrl;
    }
}

