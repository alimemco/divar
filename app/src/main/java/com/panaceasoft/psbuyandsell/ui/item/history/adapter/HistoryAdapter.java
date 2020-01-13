package com.panaceasoft.psbuyandsell.ui.item.history.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ItemHistoryAdapterBinding;
import com.panaceasoft.psbuyandsell.ui.common.DataBoundListAdapter;
import com.panaceasoft.psbuyandsell.utils.JalaliCalendar;
import com.panaceasoft.psbuyandsell.utils.Objects;
import com.panaceasoft.psbuyandsell.viewobject.ItemHistory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HistoryAdapter extends DataBoundListAdapter<ItemHistory, ItemHistoryAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private HistoryClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;

    public HistoryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, HistoryClickCallback historyClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = historyClickCallback;
    }

    @Override
    protected ItemHistoryAdapterBinding createBinding(ViewGroup parent) {
        ItemHistoryAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_history_adapter, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener(v -> {
            ItemHistory itemHistory = binding.getHistory();
            if (itemHistory != null && callback != null) {
                callback.onClick(itemHistory);
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
    protected void bind(ItemHistoryAdapterBinding binding, ItemHistory item) {
        binding.setHistory(item);
        binding.historyNameTextView.setText(item.historyName);

        String startDateString = item.historyDate.toString();
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

        binding.dateTextView.setText(timeClock + " " + jalaliDate.toString());
    }

    @Override
    protected boolean areItemsTheSame(ItemHistory oldItem, ItemHistory newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.historyName.equals(newItem.historyName);
    }

    @Override
    protected boolean areContentsTheSame(ItemHistory oldItem, ItemHistory newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.historyName.equals(newItem.historyName);
    }

    public interface HistoryClickCallback {
        void onClick(ItemHistory itemHistory);
    }
}
