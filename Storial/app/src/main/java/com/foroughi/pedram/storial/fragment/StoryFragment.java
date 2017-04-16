package com.foroughi.pedram.storial.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.foroughi.pedram.storial.R;
import com.foroughi.pedram.storial.model.Story;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

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

    @BindView(R.id.story_tv_title)
    TextView tv_title;

    @BindView(R.id.story_send_container)
    View view_send_container;

    private boolean firstHit;


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
        firstHit=true;
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
        tv_title.setText(story.getTitle());
        if(!story.isParticipative()){
            view_send_container.setVisibility(View.GONE);
        }

        if(firstHit) {
            story.setHitCount(story .getHitCount() -1);
            dbRef.setValue(story);
            firstHit=false;
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.story_btn_send){
            if(!TextUtils.isEmpty(et_text.getText())){
                String delimiter = " ";
                if(story.getContent().length()==0)
                    delimiter  = "";

                story.setContent(story.getContent()+delimiter+et_text.getText().toString()+".");
                dbRef.setValue(story);
                et_text.setText("");
            }
        }else{
            PopupMenu popup = new PopupMenu(getActivity(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.story, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
            popup.show();
        }
    }
}
