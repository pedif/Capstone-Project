package com.foroughi.pedram.storial.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Used for creating a new story
 * Created by Pedram on 4/15/2017.
 */

public class AddStoryDialogFragment extends DialogFragment {

    DatabaseReference dbRef;

    @BindView(R.id.dlg_et)
    EditText et;

    @BindView(R.id.dlg_cb)
    CheckBox cb;

    public static AddStoryDialogFragment newInstance() {
        return new AddStoryDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dbRef = FirebaseDatabase.getInstance().getReference("story");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View rootView = View.inflate(getActivity(), R.layout.dialog_add_story, null);
        ButterKnife.bind(this, rootView);

        et.requestFocus();
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);

        builder.setTitle(getString(R.string.title_add_story));

        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        return builder.create();

    }

    @Override
    public void onStart()
    {
        super.onStart();

        /*
            default action for dialog buttons are to dismiss their dialog after
            they're done so we need to override the default behaviour
         */
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Dont add a story if no title is set
                    if (TextUtils.isEmpty(et.getText().toString())) {
                        Snackbar.make(et, R.string.message_title_empty, Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    if (email == null)
                        email = "";
                    dbRef.push().setValue(
                            new Story(et.getText().toString(),
                                    email, cb.isChecked()
                                    , ""
                                    , 0 - new Date().getTime()));
                    dismiss();
                    }
            });
        }
    }
}

