package com.panaceasoft.firoozboard.edit;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.panaceasoft.firoozboard.R;


public class SuggestFragment extends DialogFragment {

    private String message;
    private View mView;
    private TextView mTextViewMessage;
    private TextView mTextViewSubMessage;
    private ImageView mImageVieClose;

    private Button mButtonInsert;
    private Button mButtonShow;
    private  Button mButtonSpecial;

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

        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);

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
        setBackground();

        return mView;
    }

    private void setBackground() {
        Dialog dialog = getDialog() ;
        if (dialog != null)
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(dialog.getContext(), R.drawable.bg_dialog));
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            }
    }

    private void initView() {
        mTextViewMessage = mView.findViewById(R.id.fragment_suggest_textView_message);
        mTextViewSubMessage = mView.findViewById(R.id.fragment_suggest_textView_sub_message);
        mImageVieClose = mView.findViewById(R.id.fragment_suggest_close);
        mButtonInsert = mView.findViewById(R.id.fragment_suggest_insert);
        mButtonShow = mView.findViewById(R.id.fragment_suggest_show);
        mButtonSpecial = mView.findViewById(R.id.fragment_suggest_action);

        if (getActivity() != null){
            mButtonSpecial.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/IRANSansMobile_Medium.ttf"));
            mTextViewSubMessage.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/IRANSansMobile_Medium.ttf"));
        }

        mTextViewMessage.setText(message);
        mImageVieClose.setOnClickListener(view -> dismiss());

        View.OnClickListener click = view -> {
            if (listener == null) return;
            listener.onButtonClick(view);
        };
        mButtonInsert.setOnClickListener(click);
        mButtonShow.setOnClickListener(click);


        mButtonSpecial.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://instagram.com/fz.board");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.instagram.android");

            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/xxx")));
            }
        });
    }


    private OnButtonClickListener listener;

    public void seOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClick(View view);
    }
}
