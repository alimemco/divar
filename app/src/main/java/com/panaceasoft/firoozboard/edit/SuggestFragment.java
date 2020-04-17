package com.panaceasoft.firoozboard.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.panaceasoft.firoozboard.R;


public class SuggestFragment extends DialogFragment {

    private String message;
    private View mView;
    private TextView mTextViewMessage;
    private ImageView mImageVieClose;

    private Button mButtonInsert;
    private Button mButtonShow;

    public static SuggestFragment newInstance(String message) {

        Bundle args = new Bundle();
        args.putString("message", message);
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
        mView = inflater.inflate(R.layout.fragment_suggest, container, false);
        initView();
        return mView;
    }

    private void initView() {
        mTextViewMessage = mView.findViewById(R.id.fragment_suggest_textView_message);
        mImageVieClose = mView.findViewById(R.id.fragment_suggest_close);
        mButtonInsert = mView.findViewById(R.id.fragment_suggest_insert);
        mButtonShow = mView.findViewById(R.id.fragment_suggest_show);

        mTextViewMessage.setText(message);
        mImageVieClose.setOnClickListener(view -> dismiss());

        View.OnClickListener click = view -> {
            if (listener == null) return;
            listener.onButtonClick(view);
        };
        mButtonInsert.setOnClickListener(click);
        mButtonShow.setOnClickListener(click);
    }


    private OnButtonClickListener listener;

    public void seOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClick(View view);
    }
}
