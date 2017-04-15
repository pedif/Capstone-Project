package com.foroughi.pedram.storial.fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.foroughi.pedram.storial.R;
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
public class StoryFragment extends Fragment implements ValueEventListener, View.OnClickListener {


    private final static String KEY_ID = "id";

    DatabaseReference dbRef;
    String id;
    Story story;

    @BindView(R.id.story_tv_content)
    TextView tv_content;

    @BindView(R.id.story_et_text)
    EditText et_text;

    @BindView(R.id.story_btn_send)
    ImageButton btn_send;

    public StoryFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment HomeFragment.
     */
    public static StoryFragment newInstance(String id) {
        StoryFragment fragment = new StoryFragment();
        Bundle b  =new Bundle();
        b.putString(KEY_ID,id);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if(b!=null&&b.getString(KEY_ID)!=null) {
            id = b.getString(KEY_ID);
            dbRef = FirebaseDatabase.getInstance().getReference().child("story").child(id);
            dbRef.addValueEventListener(this);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_story, container, false);
        ButterKnife.bind(this, rootView);

        btn_send.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        story = dataSnapshot.getValue(Story.class);
        tv_content.setText(story.getContent());

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.story_btn_send){
            if(TextUtils.isEmpty(et_text.getText()))
                Snackbar.make(view,R.string.message_sign_in_cancelled,Snackbar.LENGTH_SHORT).show();
            else{
                story.setContent(story.getContent()+et_text.getText().toString());
                dbRef.setValue(story);
                et_text.setText("");
            }
        }
    }
}
