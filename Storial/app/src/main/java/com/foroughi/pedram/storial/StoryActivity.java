package com.foroughi.pedram.storial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.foroughi.pedram.storial.Common.Constants;
import com.foroughi.pedram.storial.fragment.StoryFragment;

public class StoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        if(!getIntent().hasExtra(Constants.EXTRA_STORY_ID)) {
            Toast.makeText(this, R.string.message_unknown_id, Toast.LENGTH_SHORT).show();
            finish();
        }
       String id =  getIntent().getStringExtra(Constants.EXTRA_STORY_ID);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, StoryFragment.newInstance(id))
                .commit();
    }
}
