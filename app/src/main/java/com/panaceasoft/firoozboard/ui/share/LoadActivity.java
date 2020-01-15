package com.panaceasoft.firoozboard.ui.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.ui.item.detail.ItemActivity;
import com.panaceasoft.firoozboard.utils.Constants;

import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoadActivity extends AppCompatActivity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load);

        Uri data = this.getIntent().getData();
        if (data == null) {
            Bundle bundle = getIntent().getExtras();
            assert bundle != null;
            this.id = bundle.getString("id");
        } else {
            this.id= Objects.requireNonNull(data.getPath()).replace("/share/","");
        }

        getItem();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void getItem() {

        Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
        intent.putExtra(Constants.HISTORY_FLAG, Constants.ONE);
        intent.putExtra(Constants.ITEM_ID, id);
        startActivity(intent);
        finish();

    }
}
