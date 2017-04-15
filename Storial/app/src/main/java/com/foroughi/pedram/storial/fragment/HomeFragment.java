package com.foroughi.pedram.storial.fragment;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foroughi.pedram.storial.Common.Constants;
import com.foroughi.pedram.storial.R;
import com.foroughi.pedram.storial.StoryActivity;
import com.foroughi.pedram.storial.model.Story;
import com.foroughi.pedram.storial.view.StoryRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements StoryRecyclerAdapter.OnStoryClickedListener {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    DatabaseReference dbRef;
    StoryRecyclerAdapter adapter;
    private int startIndex = 0;
    private int length = 10;
    private String lastKey;
    int total = 0;
    private boolean loading = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new StoryRecyclerAdapter(null,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration decoration=new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.divider));
        recyclerView.addItemDecoration(decoration);

        dbRef = FirebaseDatabase.getInstance().getReference().child("story");

        loadDataAtStart();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = true;
                            loadDataAtEnd();
                        }
                    }
                }
            }
        });

        return rootView;
    }


    private void loadDataAtStart() {
        dbRef.orderByKey().limitToFirst(length).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                startIndex = startIndex + length;
                if (dataSnapshot == null)
                    return;
                ArrayList<Story> items = new ArrayList<Story>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    Story story = data.getValue(Story.class);
                    story.setId(data.getKey());
                    items.add(story);
                    lastKey = data.getKey();
                }

                adapter.addItems(items);
                loading = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadDataAtEnd() {
        dbRef.orderByKey().startAt(lastKey).limitToFirst(length).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                startIndex = startIndex + length;
                if (dataSnapshot == null)
                    return;
                ArrayList<Story> items = new ArrayList<Story>();
                /*
                    Due to constraints the first item on this list is our last item in the app so we need to ignore it
                 */
                boolean firstItem = true;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (firstItem) {
                        firstItem = false;
                        continue;
                    }
                    Story story = data.getValue(Story.class);
                    story.setId(data.getKey());
                    items.add(story);
                    lastKey = data.getKey();
                }
                adapter.addItems(items);
                loading = false;
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStorySelected(String id) {
        Intent intent= new Intent(getActivity(), StoryActivity.class);
        intent.putExtra(Constants.EXTRA_STORY_ID,id);
        startActivity(intent);
    }
}
