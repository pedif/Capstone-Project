package com.foroughi.pedram.storial.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foroughi.pedram.storial.Common.FirebaseConstants;
import com.foroughi.pedram.storial.R;
import com.foroughi.pedram.storial.model.Story;
import com.foroughi.pedram.storial.view.StoryRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import timber.log.Timber;

import static com.foroughi.pedram.storial.Common.Constants.QUERY_LENGTH;
import static com.foroughi.pedram.storial.Common.Constants.STATE_DATA;
import static com.foroughi.pedram.storial.Common.Constants.STATE_LAYOUT_MANAGER;
import static com.foroughi.pedram.storial.Common.Constants.STATE_POSITION;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseListFragment {



    /**
     * Last item which was retrieved key value( used for firebase where clause)
     */
    private long lastKey;

    /**
     * Demonstrate whether a request was sent for new fetching data
     */
    private boolean loading = false;




    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(StoryRecyclerAdapter.OnStoryClickedListener listener) {
        HomeFragment fragment = new HomeFragment();
        fragment.listener = listener;
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

        adapter = new StoryRecyclerAdapter(null, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setAnimation(null);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(decoration);


        dbRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.TABLE_STORY);

        //restore previous state
        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(STATE_DATA))
                adapter.setItems(savedInstanceState.<Story>getParcelableArrayList(STATE_DATA));

            if (savedInstanceState.containsKey(STATE_LAYOUT_MANAGER)) {
                recyclerView.getLayoutManager()
                        .onRestoreInstanceState(savedInstanceState.getParcelable(STATE_LAYOUT_MANAGER));

            }

            if (savedInstanceState.containsKey(STATE_POSITION)) {
                int position = savedInstanceState.getInt(STATE_POSITION);
                if (position != RecyclerView.NO_POSITION && position < adapter.getItemCount())
                    recyclerView.scrollToPosition(position);
            }

        } else {
            loadDataAtStart();
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    /*
                     Decide whether we need to request a new fetch
                     */
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Timber.e("Loading");
                            loading = true;
                            loadDataAtEnd();
                        }
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(STATE_LAYOUT_MANAGER, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(STATE_DATA, adapter.getItems());
        outState.putInt(STATE_POSITION,
                ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    /**
     * Load the first batch of data based on order criteria
     */
    private void loadDataAtStart() {
        dbRef.orderByChild(FirebaseConstants.COLUMN_DATE).limitToFirst(QUERY_LENGTH)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null)
                            return;
                        ArrayList<Story> items = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                            Story story = data.getValue(Story.class);
                            story.setId(data.getKey());
                            items.add(story);
                            lastKey = story.getInverseDate();
                        }

                        adapter.addItems(items);
                        loading = false;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    /**
     * Load a batch of data starting from {@link #lastKey} based on order criteria
     */
    private void loadDataAtEnd() {
        dbRef.orderByChild(FirebaseConstants.COLUMN_DATE).startAt(lastKey).limitToFirst(QUERY_LENGTH)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null)
                            return;
                        ArrayList<Story> items = new ArrayList<>();
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
                            lastKey = story.getInverseDate();
                        }
                        adapter.addItems(items);
                        loading = false;
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
