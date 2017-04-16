package com.foroughi.pedram.storial.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
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
import com.foroughi.pedram.storial.dialog.AddStoryDialogFragment;
import com.foroughi.pedram.storial.model.Story;
import com.foroughi.pedram.storial.view.StoryRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foroughi.pedram.storial.Common.Constants.STATE_DATA;
import static com.foroughi.pedram.storial.Common.Constants.STATE_LAYOUT_MANAGER;
import static com.foroughi.pedram.storial.Common.Constants.STATE_POSITION;

/**
 * A simple {@link Fragment} subclass.
 */
public class WritingFragment extends BaseListFragment {

    private static final String DIALOG_TAG = "dialog";


    @BindView(R.id.fab)
    FloatingActionButton fab;

    String author;

    StoryRecyclerAdapter.OnStoryClickedListener listener;

    public WritingFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment HomeFragment.
     */
    public static WritingFragment newInstance(StoryRecyclerAdapter.OnStoryClickedListener listener) {
        WritingFragment fragment = new WritingFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_recycler_fab, container, false);
        ButterKnife.bind(this, rootView);


        adapter = new StoryRecyclerAdapter(null, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);

        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(decoration);

        dbRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.TABLE_STORY);

        author = FirebaseAuth.getInstance().getCurrentUser().getEmail();

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = AddStoryDialogFragment.newInstance();
                if (!dialogFragment.isAdded())
                    dialogFragment.show(getFragmentManager(), DIALOG_TAG);
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
     * Load data based on order criteria and listen for changes
     */
    private void loadDataAtStart() {
        if (author == null)
            return;

        dbRef.orderByChild(FirebaseConstants.COLUMN_AUTHOR).equalTo(author).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Story story = dataSnapshot.getValue(Story.class);
                story.setId(dataSnapshot.getKey());
                adapter.addItem(story);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
