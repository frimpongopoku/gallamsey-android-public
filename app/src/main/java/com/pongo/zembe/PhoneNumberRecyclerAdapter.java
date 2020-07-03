package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhoneNumberRecyclerAdapter extends RecyclerView.Adapter<PhoneNumberRecyclerAdapter.PhoneNumberRecyclerViewHolder> {

  Context context;
  ArrayList<PaymentContact> phoneNumbersArray;
  GalInterfaceGuru.PaymentContactAdapterClickable onClickListener;


  public void emptyRecycler(){
    this.phoneNumbersArray = new ArrayList<PaymentContact>();
  }
  public PhoneNumberRecyclerAdapter(Context context, ArrayList<PaymentContact> phoneNumbersArray, GalInterfaceGuru.PaymentContactAdapterClickable listener) {
    this.context = context;
    this.phoneNumbersArray = phoneNumbersArray;
    onClickListener = listener;
  }

  @NonNull
  @Override
  public PhoneNumberRecyclerViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.phone_numbers_recycler_item, parent, false);
    return new PhoneNumberRecyclerViewHolder(v, onClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull PhoneNumberRecyclerViewHolder holder, int position) {
    PaymentContact contact = phoneNumbersArray.get(position);
    holder.name.setText(contact.getContactName());
    holder.number.setText(contact.getContactPhoneNumber());
    holder.network.setText(contact.getNetworkName());
  }

  @Override
  public int getItemCount() {
    return phoneNumbersArray.size();
  }

  public class PhoneNumberRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView name, number, network;
    RelativeLayout layout ;
    GalInterfaceGuru.PaymentContactAdapterClickable onClickListener;

    public PhoneNumberRecyclerViewHolder(@NonNull View itemView, final GalInterfaceGuru.PaymentContactAdapterClickable listener) {
      super(itemView);
      layout = itemView.findViewById(R.id.recycler_contact_item);
      name = itemView.findViewById(R.id.contact_name);
      number = itemView.findViewById(R.id.contact_phone);
      network = itemView.findViewById(R.id.network);
      onClickListener = listener;

      layout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          listener.callback(getAdapterPosition());
        }
      });
    }
  }
}
