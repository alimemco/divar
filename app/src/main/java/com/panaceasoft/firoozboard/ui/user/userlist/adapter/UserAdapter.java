package com.panaceasoft.firoozboard.ui.user.userlist.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ItemUserAdapterBinding;
import com.panaceasoft.firoozboard.ui.common.DataBoundListAdapter;
import com.panaceasoft.firoozboard.utils.JalaliCalendar;
import com.panaceasoft.firoozboard.utils.Objects;
import com.panaceasoft.firoozboard.viewobject.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UserAdapter extends DataBoundListAdapter<User, ItemUserAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private UserAdapter.ItemClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;

    public UserAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, UserAdapter.ItemClickCallback itemClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = itemClickCallback;
    }

    @Override
    protected ItemUserAdapterBinding createBinding(ViewGroup parent) {
        ItemUserAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_user_adapter, parent, false,
                        dataBindingComponent);


        binding.getRoot().setOnClickListener(v -> {
            User followerUser = binding.getFollowerUser();
            if (followerUser != null && callback != null) {
                callback.onClick(followerUser);
            }

        });


        return binding;
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemUserAdapterBinding binding, User followerUser) {
        binding.setFollowerUser(followerUser);

        String startDateString = followerUser.addedDate.toString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String timeClock = hour + ":" + minute + ":" + second;
        JalaliCalendar.gDate miladiiDate = new JalaliCalendar.gDate(year, month, day);
        JalaliCalendar.gDate jalaliDate = JalaliCalendar.MiladiToJalali(miladiiDate);

        binding.joinedDateTextView.setText(timeClock + " " + jalaliDate.toString());
        binding.ratingBarInformation.setRating(followerUser.ratingDetails.totalRatingValue);
        binding.ratingCountTextView.setText(String.format("%s%s%s", "(", followerUser.ratingCount, ")"));

    }

    @Override
    protected boolean areItemsTheSame(User oldItem, User newItem) {
        return Objects.equals(oldItem.userId, newItem.userId);
    }

    @Override
    protected boolean areContentsTheSame(User oldItem, User newItem) {
        return Objects.equals(oldItem.userId, newItem.userId);
    }

    public interface ItemClickCallback {
        void onClick(User followerUser);
    }
}

