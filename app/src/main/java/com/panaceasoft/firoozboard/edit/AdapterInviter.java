package com.panaceasoft.firoozboard.edit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.edit.model.InviteModel;

import java.util.List;

public class AdapterInviter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

   private List<InviteModel> list;

   public AdapterInviter(List<InviteModel> list) {
      this.list = list;
   }

   @NonNull
   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inviter, parent, false);
      return new Holder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

      if (holder instanceof Holder) {
         ((Holder) holder).bind(list.get(position));
      }
   }

   @Override
   public int getItemCount() {
      return list.size();
   }

   class Holder extends RecyclerView.ViewHolder {

      private TextView mTextViewUser, mTextViewCount;

      public Holder(@NonNull View itemView) {
         super(itemView);

         mTextViewUser = itemView.findViewById(R.id.textView_user);
         mTextViewCount = itemView.findViewById(R.id.textView_count);
      }

      void bind(InviteModel model) {
         mTextViewUser.setText(String.format("%s\n%s ", model.getCallerUsername(), model.getCallerPhone()));
         mTextViewCount.setText(String.format("%s member", model.getInviteCount()));

      }
   }
}
