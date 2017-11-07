package com.example.sjeong.pick;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mijin on 2017-11-08.
 */

public class User implements Parcelable{
    String id;
    int join_target;
    float cont_rate;
    String bank;

    public User() {
    }

    public User(Parcel in) {
        readFromParcel(in);
    }

    public User(String id, int join_target, float cont_rate, String bank) {
        this.id = id;
        this.join_target = join_target;
        this.cont_rate = cont_rate;
        this.bank = bank;
    }
    private void readFromParcel(Parcel in){
        this.id = in.readString();
        this.join_target = in.readInt();
        this.cont_rate = in.readFloat();
        this.bank = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(join_target);
        dest.writeFloat(cont_rate);
        dest.writeString(bank);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
