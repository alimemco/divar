package com.panaceasoft.firoozboard.edit;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panaceasoft.firoozboard.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestFragment extends DialogFragment {

    private String message ;
    private View mView ;
    private TextView mTextViewMessage;

    public static SuggestFragment newInstance(String message) {
        
        Bundle args = new Bundle();
        args.putString("message",message);
        SuggestFragment fragment = new SuggestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        message = getArguments().getString("message");
    }

    public SuggestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_suggest, container, false);
        initView();
        return mView;
    }

    private void initView() {
        mTextViewMessage = mView.findViewById(R.id.fragment_suggest_textView_message);
        mTextViewMessage.setText(message);
    }
}
