package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ContactDropDownAdapter extends ArrayAdapter<PaymentContact> {

  public ContactDropDownAdapter(Context context, ArrayList<PaymentContact> contactList) {
    super(context, 0, contactList);
  }


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return initalize(position, convertView, parent);
  }

  @Override
  public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return initalize(position, convertView, parent);
  }

  // just a custom method to repeat inside "getView" and "getDropDownview"
  private View initalize(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.payment_contact_dropdown_item, parent, false);
    }
    TextView name, number;
    name = convertView.findViewById(R.id.contact_name);
    number = convertView.findViewById(R.id.contact_number);
    PaymentContact contact = getItem(position);
    name.setText(contact.getContactName());
    number.setText(contact.getContactPhoneNumber());
    return convertView;
  }
}
