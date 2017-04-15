package com.foroughi.pedram.storial.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Pedram on 4/14/2017.
 */

public class Story {

    String id;
    String author;
    boolean participative;
    String content;
    String title;
    boolean finished;
    long hitCount;


    public Story() {

    }

    public Story(String title, String author, boolean participative, String content) {

        this.title = title;
        this.author = author;
        this.participative = participative;
        this.content = content;


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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
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
}
