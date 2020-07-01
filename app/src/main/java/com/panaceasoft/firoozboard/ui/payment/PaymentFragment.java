package com.panaceasoft.firoozboard.ui.payment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.ui.common.NavigationController;
import com.panaceasoft.firoozboard.utils.Constants;

import java.text.DecimalFormat;

import javax.inject.Inject;


public class PaymentFragment extends DialogFragment {

    @Inject
    protected NavigationController navigationController;
    private View mView;
    private Button mButtonPay;
    private TextView mTextViewPrice;
    private Intent intent;
    private String itemId;
    private String locationId;
    private String locationName;
    private int price;


    public PaymentFragment(Intent intent, String itemId, String locationId, String locationName, int priceCategory) {
        this.intent = intent;
        this.itemId = itemId;
        this.locationId = locationId;
        this.locationName = locationName;
        this.price = priceCategory;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_payment, container, false);
        findViews();
        return mView;
    }

    private void findViews() {
        mButtonPay = mView.findViewById(R.id.fragment_payment_button);
        mTextViewPrice = mView.findViewById(R.id.fragment_payment_textView_price);

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        mTextViewPrice.setText(String.format("%s تومان",formatter.format(price)));

        mButtonPay.setOnClickListener(view -> {

            if (getActivity() == null) return;
            getActivity().startActivityForResult(intent, Constants.REQUEST_CODE__PAYMENT);
        });

    }

}
