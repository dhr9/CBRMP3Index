package com.example.dhruv.cbrmp3index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EditDialogFragment extends DialogFragment {

    public static String TITLE_KEY = "title";
    public static String PREVIOUS_NAME_KEY = "prev_name_key";
    //    ------------------------

    @BindView(R.id.ok_button)
    Button okButton;
    @BindView(R.id.cancel_button)
    Button cancelButton;
    @BindView(R.id.dialog_title)
    TextView dialogTitle;
    @BindView(R.id.edit_view)
    EditText editView;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;
    @BindView(R.id.clear_button)
    Button clearButton;

    private String title = "Edit File";
    private String prevName = "";
    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialogue_edit, container, false);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE_KEY);
            prevName = getArguments().getString(PREVIOUS_NAME_KEY);
        }

        ButterKnife.bind(this, v);

        dialogTitle.setText(title);
        editView.setText(prevName);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okButton.setVisibility(View.INVISIBLE);
                loadingIndicator.setVisibility(View.VISIBLE);

                String s = String.valueOf(editView.getText());
                if(s==null){
                    s = "";
                }

                clickListener.onClick(s);
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editView.setText("");
            }
        });

        return v;
    }

    public interface ClickListener {
        void onClick(String s);
    }

}

