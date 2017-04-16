package com.foroughi.pedram.storial.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by Pedram on 4/14/2017.
 */

public class Story implements Parcelable {

    String id;
    String author;
    boolean participative;
    String content;
    String title;
    long hitCount;
    long inverseDate;

    public Story() {

    }

    public Story(String title, String author, boolean participative, String content, long inverseDate) {

        this.title = title;
        this.author = author;
        this.participative = participative;
        this.content = content;
        this.inverseDate = inverseDate;


    }

    public Story(Parcel parcel) {
        id = parcel.readString();
        title = parcel.readString();
        content = parcel.readString();
        author =parcel.readString();
        participative = Boolean.parseBoolean(parcel.readString());
        hitCount = parcel.readLong();
        inverseDate = parcel.readLong();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isParticipative() {
        return participative;
    }

    public void setParticipative(boolean participative) {
        this.participative = participative;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getHitCount() {
        return hitCount;
    }

    public void setHitCount(long hitCount) {
        this.hitCount = hitCount;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public String getId() {
        return id;
    }


    public void setInverseDate(long inverseDate) {
        this.inverseDate = inverseDate;
    }

    public long getInverseDate() {
        return inverseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(author);
        parcel.writeString(String.valueOf(participative));
        parcel.writeLong(hitCount);
        parcel.writeLong(inverseDate);
    }

    public final Parcelable.Creator<Story> CREATOR = new ClassLoaderCreator<Story>() {
        @Override
        public Story createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new Story(parcel);
        }

        @Override
        public Story createFromParcel(Parcel parcel) {
            return new Story(parcel);
        }

        @Override
        public Story[] newArray(int i) {
            return new Story[0];
        }
    };
}
