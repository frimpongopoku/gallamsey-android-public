package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhoneNumberRecyclerAdapter extends RecyclerView.Adapter<PhoneNumberRecyclerAdapter.PhoneNumberRecyclerViewHolder> {

  Context context;
  ArrayList<PaymentContact> phoneNumbersArray;

  public PhoneNumberRecyclerAdapter(Context context, ArrayList<PaymentContact> phoneNumbersArray) {
    this.context = context;
    this.phoneNumbersArray = phoneNumbersArray;
  }

  @NonNull
  @Override
  public PhoneNumberRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new PhoneNumberRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.phone_numbers_recycler_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull PhoneNumberRecyclerViewHolder holder, int position) {
    PaymentContact contact = phoneNumbersArray.get(position);
    holder.name.setText(contact.getContactName());
    holder.number.setText(contact.getContactPhoneNumber());
  }

  @Override
  public int getItemCount() {
    return phoneNumbersArray.size();
  }

  public class PhoneNumberRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView name, number;

    public PhoneNumberRecyclerViewHolder(@NonNull View itemView) {
      super(itemView);
      name = itemView.findViewById(R.id.contact_name);
      number = itemView.findViewById(R.id.contact_phone);
    }
  }
}
