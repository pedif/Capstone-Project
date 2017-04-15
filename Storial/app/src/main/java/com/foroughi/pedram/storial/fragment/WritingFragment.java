package com.foroughi.pedram.storial.fragment;


import android.content.Intent;
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

import com.foroughi.pedram.storial.Common.Constants;
import com.foroughi.pedram.storial.R;
import com.foroughi.pedram.storial.StoryActivity;
import com.foroughi.pedram.storial.dialog.AddStoryDialogFragment;
import com.foroughi.pedram.storial.model.Story;
import com.foroughi.pedram.storial.view.StoryRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
public class WritingFragment extends Fragment implements StoryRecyclerAdapter.OnStoryClickedListener {

    private static final String DIALOG_TAG = "dialog";
    @BindView(R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;


    DatabaseReference dbRef;
    StoryRecyclerAdapter adapter;
    private int startIndex = 0;
    private int length = 10;
    private String lastKey;
    int total = 0;
    private boolean loading = false;
    String email;

    public WritingFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment HomeFragment.
     */
    public static WritingFragment newInstance() {
        WritingFragment fragment = new WritingFragment();
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


        adapter = new StoryRecyclerAdapter(null, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        DividerItemDecoration decoration=new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.divider));
        recyclerView.addItemDecoration(decoration);

        dbRef = FirebaseDatabase.getInstance().getReference().child("story");

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        loadDataAtStart();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = AddStoryDialogFragment.newInstance();
                if(!dialogFragment.isAdded())
                dialogFragment.show(getFragmentManager(),DIALOG_TAG);
            }
        });

        return rootView;
    }


    private void loadDataAtStart() {
        if(email==null)
            return;

//        dbRef.orderByChild("author").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                startIndex = startIndex + length;
//                if (dataSnapshot == null)
//                    return;
//                ArrayList<Story> items = new ArrayList<Story>();
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//
//                    items.add(0,data.getValue(Story.class));
//                    lastKey = data.getKey();
//                }
//
//                adapter.addItems(items);
//                loading = false;
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
        dbRef.orderByChild("author").equalTo(email).addChildEventListener(new ChildEventListener() {
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

    @Override
    public void onStorySelected(String id) {
        Intent intent= new Intent(getActivity(), StoryActivity.class);
        intent.putExtra(Constants.EXTRA_STORY_ID,id);
        startActivity(intent);
    }

}
