package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class NewErrandCreationPage extends AppCompatActivity implements OnDetailItemsClick {

  ArrayList<String> dummyDetails = new ArrayList<>();
  RecyclerView recyclerView;
  ImageView  addDetailsBtn;
  EditText detailsBox;
  DetailsListAdapter recyclerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_errand_creation_page);
    recyclerView = findViewById(R.id.recyclerview_in_details_tab);
    addDetailsBtn = findViewById(R.id.add_btn_in_details_tab);
    detailsBox = findViewById(R.id.edittext_in_details_tab);
    recyclerAdapter = new DetailsListAdapter(this,dummyDetails,this);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(recyclerAdapter);
    recyclerView.hasFixedSize();
    addDetailsBtn.setOnClickListener(addToDetailsList);
  }


  private View.OnClickListener addToDetailsList = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      if(validateDetails()){
        dummyDetails.add(detailsBox.getText().toString().trim());
        recyclerAdapter.notifyDataSetChanged();
        detailsBox.setText("");
      }
    }
  };
  public Boolean validateDetails(){
    Boolean good  =true;
    String det = detailsBox.getText().toString().trim();
    if(det.isEmpty()){
      detailsBox.setError("Add a specific instruction before you add");
      detailsBox.requestFocus();
      good = false;
    }
    return good;
  }

  @Override
  public void onDetailItemClick(int pos) {

  }
}
