package com.panaceasoft.firoozboard.ui.payment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.ui.common.NavigationController;
import com.panaceasoft.firoozboard.utils.Constants;

import javax.inject.Inject;


public class PaymentFragment extends DialogFragment {

    @Inject
    protected NavigationController navigationController;
    private View mView;
    private Button mButtonPay;
    private Intent intent;
    private String itemId;
    private String locationId;
    private String locationName;


    public PaymentFragment(Intent intent, String itemId, String locationId, String locationName) {
        this.intent = intent;
        this.itemId = itemId;
        this.locationId = locationId;
        this.locationName = locationName;
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
        mButtonPay.setOnClickListener(view -> {

            if (getActivity() == null) return;
            getActivity().startActivityForResult(intent, Constants.REQUEST_CODE__PAYMENT);
        });

    }

}
