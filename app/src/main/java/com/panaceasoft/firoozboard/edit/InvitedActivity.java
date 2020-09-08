package com.panaceasoft.firoozboard.edit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.panaceasoft.firoozboard.PsApp;
import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.edit.model.InviteModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitedActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_invited);

      PsApp.getApi().getAllInviter().enqueue(new Callback<List<InviteModel>>() {
         @Override
         public void onResponse(Call<List<InviteModel>> call, Response<List<InviteModel>> response) {
            if (response.isSuccessful()) {
               if (response.body() == null) return;

               RecyclerView recyclerView = findViewById(R.id.recyclerView_inviter);
               recyclerView.setLayoutManager(new LinearLayoutManager(InvitedActivity.this));
               recyclerView.setAdapter(new AdapterInviter(response.body()));
            }
         }

         @Override
         public void onFailure(Call<List<InviteModel>> call, Throwable t) {
            Toast.makeText(InvitedActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
         }
      });
   }
}
