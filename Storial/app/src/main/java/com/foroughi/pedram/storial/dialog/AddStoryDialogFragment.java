package com.foroughi.pedram.storial.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.foroughi.pedram.storial.R;
import com.foroughi.pedram.storial.model.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pedram on 4/15/2017.
 */

public class AddStoryDialogFragment extends DialogFragment {

    DatabaseReference dbRef;

    @BindView(R.id.dlg_et)
    EditText et;

    @BindView(R.id.dlg_cb)
    CheckBox cb;

    public static AddStoryDialogFragment newInstance(){
        return new AddStoryDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dbRef = FirebaseDatabase.getInstance().getReference("story");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View rootView = View.inflate(getActivity(), R.layout.dialog_add_story, null);
        ButterKnife.bind(this,rootView);

        et.requestFocus();
        InputMethodManager mgr = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);

        builder.setTitle(getString(R.string.title_add_story));

        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        if(email==null)
                            email = "";
                        dbRef.push().setValue(new Story(et.getText().toString(),email,cb.isChecked(),""));
                    }
                });


        return builder.create();
    }
}
