package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ErrandViewActivity extends AppCompatActivity {
  Toolbar toolbar;
  MagicBoxes dialogCreator;
  Button runErrandButton,readAbout;
  GenericErrandClass errand;
  TextView pageTitle,moneySummary,cost,allowance,dateText,userName,errandDescription,detailsText,detailsTitleBox;
  ImageView backBtn, errandImage;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_errand_view);
    initializeActivity();
    errand = getIntent().getParcelableExtra(Konstants.PASS_ERRAND_AROUND);
    if(errand != null){
      populateWithInfo(errand);
    }
  }


  public void initializeActivity(){
    detailsTitleBox = findViewById(R.id.details_title_box);
    detailsText = findViewById(R.id.details_text);
    userName = findViewById(R.id.user_name);
    errandDescription = findViewById(R.id.errand_description);
    errandImage = findViewById(R.id.errand_img);
    moneySummary = findViewById(R.id.good_money_summary);
    cost = findViewById(R.id.cost_value);
    allowance = findViewById(R.id.allowance_value);
    dateText = findViewById(R.id.post_date);
    pageTitle = findViewById(R.id.page_name);
    backBtn = findViewById(R.id.back_btn);
    toolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(toolbar);
    dialogCreator  = new MagicBoxes(this);
    runErrandButton = findViewById(R.id.run_errand);
    runErrandButton.setOnClickListener(runErrand);
    backBtn.setOnClickListener(goBack);
  }


  private void populateWithInfo(GenericErrandClass errand){
    errandDescription.setText(errand.getDescription());
    userName.setText(errand.getCreator().getUserName());
    if(errand.getErrandType().equals(Konstants.IMAGE_ERRAND)){
      Picasso.get().load(errand.getImages().get(0)).into(errandImage);
    }
    else{
      errandImage.setVisibility(View.GONE);
    }
    cost.setText("GHS "+errand.getCost());
    allowance.setText("GHS "+ errand.getAllowance());
    dateText.setText(errand.getCreatedAt());
    String total = String.valueOf(errand.getCost() + errand.getAllowance());
    String summary = "A total of GHS "+total+" will be sent to your wallet on completing this errand";
    moneySummary.setText(summary);
    pageTitle.setText(errand.getTitle());
    String details = MyHelper.mergeTextsFromArrayWithLines(errand.getDetails());
    if(details.equals(Konstants.INIT_STRING)){
      removeDetailsBox();
    }else{
      detailsText.setText(details);
    }


  }

  private void removeDetailsBox(){
    detailsTitleBox.setVisibility(View.INVISIBLE);
    detailsText.setVisibility(View.INVISIBLE);
  }

  private View.OnClickListener runErrand = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      LayoutInflater inflater = getLayoutInflater();
      View v = inflater.inflate(R.layout.start_errand_custom_dialog,null,false);
      Dialog dialog = dialogCreator.constructCustomDialog("Confirmation", v, new MagicBoxCallables() {
        @Override
        public void negativeBtnCallable() {
          Toast.makeText(ErrandViewActivity.this, "we in the negative", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void positiveBtnCallable() {
          Toast.makeText(ErrandViewActivity.this, "We in the positive", Toast.LENGTH_SHORT).show();
        }
      });
      dialog.show();
    }
  };


  private View.OnClickListener goBack = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      finish();
    }
  };

}
