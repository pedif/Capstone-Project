package com.foroughi.pedram.storial.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.foroughi.pedram.storial.R;
import com.foroughi.pedram.storial.view.StoryRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;

/**
 * Base fragment used for all fragments which contain a list
 * Created by Pedram on 4/16/2017.
 */

public abstract class BaseListFragment extends Fragment {


    @BindView(R.id.list)
    RecyclerView recyclerView;

    DatabaseReference dbRef;
    StoryRecyclerAdapter adapter;

    StoryRecyclerAdapter.OnStoryClickedListener listener;

    public void setListener(StoryRecyclerAdapter.OnStoryClickedListener listener) {
        this.listener = listener;
        if(adapter!=null)
        adapter.setListener(listener);
    }
}
