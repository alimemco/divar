package com.panaceasoft.firoozboard.ui.item.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ItemItemVerticalWithUserBinding;
import com.panaceasoft.firoozboard.ui.common.DataBoundListAdapter;
import com.panaceasoft.firoozboard.ui.common.DataBoundViewHolder;
import com.panaceasoft.firoozboard.utils.Constants;
import com.panaceasoft.firoozboard.utils.Objects;
import com.panaceasoft.firoozboard.viewobject.Item;

import java.text.DecimalFormat;

/**
 * Created by Panacea-Soft on 9/18/18.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class ItemVerticalListAdapter extends DataBoundListAdapter<Item, ItemItemVerticalWithUserBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;

    public ItemVerticalListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                   NewsClickCallback callback, DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemItemVerticalWithUserBinding createBinding(ViewGroup parent) {
        ItemItemVerticalWithUserBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_item_vertical_with_user, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Item item = binding.getItem();
            if (item != null && callback != null) {
                callback.onClick(item);
            }
        });

        
        return binding;
    }

    // For general animation
    @Override
    public void bindView(DataBoundViewHolder<ItemItemVerticalWithUserBinding> holder, int position) {
        super.bindView(holder, position);


        //setAnimation(holder.itemView, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemItemVerticalWithUserBinding binding, Item item) {
        binding.setItem(item);
        if (!item.price.equals("0")) {
            String price = new DecimalFormat("###,###,###,###").format(Integer.parseInt(item.price));
                String currencyPrice = price + " " + binding.getRoot().getResources().getString(R.string.item_entry_detail__price_symbol);
                binding.priceTextView.setText(currencyPrice);

        } else {
            binding.priceTextView.setText(item.itemPriceType.name);
        }

       // binding.priceTextView.setText(item.price.toString() + " " + binding.getRoot().getResources().getString(R.string.item_entry_detail__price_symbol));



        //binding.imageviewindex.s
        if(item.isSoldOut.equals(Constants.ONE)){
            binding.isSoldTextView.setVisibility(View.VISIBLE);
        }else {
            binding.isSoldTextView.setVisibility(View.GONE);
        }
//        binding.ratingValueTextView.setText(String.valueOf(item.ratingDetails.totalRatingValue));
//        binding.reviewValueTextView.setText(String.format("( %s %s )", String.valueOf(item.ratingDetails.totalRatingCount), binding.getRoot().getResources().getString(R.string.dashboard_review)));
    }

    @Override
    protected boolean areItemsTheSame(Item oldItem, Item newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.title.equals(newItem.title)
                && oldItem.isFavourited.equals(newItem.isFavourited)
                && oldItem.favouriteCount.equals(newItem.favouriteCount)
                && oldItem.isSoldOut.equals(newItem.isSoldOut);
    }

    @Override
    protected boolean areContentsTheSame(Item oldItem, Item newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.title.equals(newItem.title)
                && oldItem.isFavourited.equals(newItem.isFavourited)
                && oldItem.favouriteCount.equals(newItem.favouriteCount)
                && oldItem.isSoldOut.equals(newItem.isSoldOut);
    }

    public interface NewsClickCallback {
        void onClick(Item item);

    }


}
