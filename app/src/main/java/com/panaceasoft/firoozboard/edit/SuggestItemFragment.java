package com.panaceasoft.firoozboard.edit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.edit.model.Category;

import java.util.List;


public class SuggestItemFragment extends DialogFragment {

    private Context context;
    private List<Category> categories;
    private View mView;

    public void onResume() {
        super.onResume();

        if (getDialog() == null || getDialog().getWindow() == null) return ;

        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    public SuggestItemFragment() {

    }

    public SuggestItemFragment(List<Category> categories) {
        this.categories = categories;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_suggest_item, container, false);
        initViews();
        return mView;
    }

    private void initViews() {

        for (int i = 0; i < categories.size(); i++) {

            Category category = categories.get(i);

            addButton(category.getId(), category.getName());
        }

    }

    private void addButton(String catId, String text) {
        Button button = new Button(getContext());
        button.setTextColor(Color.parseColor("#FFFFFF"));
        button.setText(text);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dpToPx(40), dpToPx(10), dpToPx(40), dpToPx(10));

        button.setLayoutParams(params);

        button.setOnClickListener(view -> {
            if (onItemClickListener != null){
                onItemClickListener.onItemClick(catId,text);
            }
            dismiss();
        });

        ((LinearLayout) mView).addView(button);

    }

    private int dpToPx(int dp) {
        Resources r = context.getResources();

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private OnItemClickListener onItemClickListener ;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public  interface OnItemClickListener{
        void onItemClick(String id , String name);
    }
}
