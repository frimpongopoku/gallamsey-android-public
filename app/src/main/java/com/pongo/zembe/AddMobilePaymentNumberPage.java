package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddMobilePaymentNumberPage extends AppCompatActivity implements  GalInterfaceGuru.PaymentContactAdapterClickable{
  Button saveButton;
  RecyclerView recyclerView;
  EditText contactNameBox, contactNumberBox;
  Spinner networkDropDown;
  ArrayList<PaymentContact> allPaymentContacts;
  String name, phone, selectedNetwork;
  PhoneNumberRecyclerAdapter recyclerAdapter;
  GroundUser authenticatedUser;
  FirebaseFirestore firestore = FirebaseFirestore.getInstance();
  CollectionReference userDB = firestore.collection(Konstants.USER_COLLECTION);
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_mobile_payment_number_page);
    authenticatedUser = getIntent().getParcelableExtra("authUser");
    if(authenticatedUser !=null){
      allPaymentContacts = authenticatedUser.getMobileNumbersForPayment();
    }
    saveButton = findViewById(R.id.save_my_phone_number);
    recyclerView = findViewById(R.id.payment_mobile_recycler);
    contactNameBox = findViewById(R.id.mobile_name);
    contactNumberBox = findViewById(R.id.mobile_phone);
    networkDropDown = findViewById(R.id.mobile_network);
    ArrayAdapter<String> networkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Konstants.GH_NETWORKS);
    networkAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    networkDropDown.setAdapter(networkAdapter);
    recyclerAdapter = new PhoneNumberRecyclerAdapter(this, allPaymentContacts, this);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(recyclerAdapter);
    networkDropDown.setOnItemSelectedListener(selectNetwork);
    saveButton.setOnClickListener(savePaymentContact);
  }


  private Spinner.OnItemSelectedListener selectNetwork = new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      selectedNetwork = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
  };
  private View.OnClickListener savePaymentContact = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      if (validate()) {
        PaymentContact contact = new PaymentContact(name, phone);
        contact.setNetworkName(selectedNetwork);
        cleanup();
        allPaymentContacts.add(contact);
        authenticatedUser.setMobileNumbersForPayment(allPaymentContacts);
        saveContactsToDB();
        recyclerAdapter.notifyDataSetChanged();
        Toast.makeText(AddMobilePaymentNumberPage.this, "Contact added to list", Toast.LENGTH_SHORT).show();
      }
    }
  };

  public void saveContactsToDB() {
    userDB.document(authenticatedUser.getUserDocumentID())
      .set(authenticatedUser).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        e.printStackTrace();
        Toast.makeText(AddMobilePaymentNumberPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }


  public void cleanup() {
    contactNameBox.setText("");
    contactNumberBox.setText("");
  }

  public Boolean validate() {
    Boolean good = true;
    String name, phone;
    name = contactNameBox.getText().toString().trim();
    phone = contactNumberBox.getText().toString().trim();
    this.name = name;
    this.phone = phone;
    if (name.isEmpty()) {
      contactNameBox.setError("You need to provide a short description");
      contactNameBox.requestFocus();
      good = false;
    }

    if (phone.isEmpty()) {
      good = false;
      contactNumberBox.setError("You have not provided any payment phone number");
      contactNumberBox.requestFocus();
    }
    return good;
  }

  @Override
  public void callback(int position) {
    allPaymentContacts.remove(position);
    recyclerAdapter.notifyItemRemoved(position);
    authenticatedUser.setMobileNumbersForPayment(allPaymentContacts);
    saveContactsToDB();
  }
}
