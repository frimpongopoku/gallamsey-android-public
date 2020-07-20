package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ErrandViewActivity extends AppCompatActivity {
  Toolbar toolbar;
  MagicBoxes dialogCreator;
  Button runErrandButton, readAbout;
  GenericErrandClass errand;
  TextView pageTitle, moneySummary, cost, allowance, dateText, userName, errandDescription, detailsText, detailsTitleBox;
  ImageView backBtn, errandImage, options;
  ChipGroup tagGroup;
  Context thisActivity = this;
  TagCollection tagCollection;
  GroundUser authenticatedUser;
  GalFirebaseHelper firebaseHelper = new GalFirebaseHelper();
  CollectionReference errandDB = FirebaseFirestore.getInstance().collection(Konstants.ERRAND_COLLECTION);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_errand_view);
    initializeActivity();
    errand = getIntent().getParcelableExtra(Konstants.PASS_ERRAND_AROUND);
    tagCollection = getIntent().getParcelableExtra(Konstants.PASS_TAGS);
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    if (errand != null) {
      populateWithInfo(errand);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    listenForChangesInDocument();
  }

  public void initializeActivity() {
    options = findViewById(R.id.options);
    tagGroup = findViewById(R.id.errand_view_chipGroup);
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
    dialogCreator = new MagicBoxes(this);
    runErrandButton = findViewById(R.id.run_errand);
    runErrandButton.setOnClickListener(runErrand);
    backBtn.setOnClickListener(goBack);
    options.setOnClickListener(openDropdown);
  }

  private void goToEditPage() {
    Intent page = new Intent(this, NewErrandCreationPage.class);
    page.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
    page.putExtra(Konstants.EDIT_MODE, Konstants.EDIT_MODE);
    page.putExtra(Konstants.PASS_ERRAND_AROUND, errand);
    page.putExtra(Konstants.PASS_TAGS, tagCollection);
    startActivity(page);
  }

  private View.OnClickListener openDropdown = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      PopupMenu menu = new PopupMenu(thisActivity, view);
      if (authenticatedUser != null && authenticatedUser.getUserDocumentID().equals(errand.getCreator().getUserPlatformID())) {
        //just check if the current signed in user is the one that created the errand show them the edit & delete menu
        menu.inflate(R.menu.menu_for_errand_view);
      } else {
        menu.inflate(R.menu.no_auth_menu_for_errand_view);
      }

      menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
          switch (menuItem.getItemId()) {
            case R.id.edit: {
              goToEditPage();
              break;
            }
            case R.id.delete: {
              Toast.makeText(thisActivity, "You have clicked delete", Toast.LENGTH_SHORT).show();
              break;
            }
            case R.id.share: {
              Toast.makeText(thisActivity, "You have clicked share", Toast.LENGTH_SHORT).show();
              break;
            }
            case R.id.report: {
              Toast.makeText(thisActivity, "You have clicked report", Toast.LENGTH_SHORT).show();
              break;
            }
            case R.id.run: {
              Toast.makeText(thisActivity, "You have clicked run", Toast.LENGTH_SHORT).show();
              break;
            }
            case R.id.back: {
              finish();
              break;
            }
          }
          return true;
        }
      });
      menu.show();
    }
  };

  private void listenForChangesInDocument() {
    firebaseHelper.setSnapshotListenerOnDocument(errandDB.document(errand.getErrandDocumentID()), new GalInterfaceGuru.SnapshotTakerInterface() {
      @Override
      public void callback(DocumentSnapshot document) {
        if (document.exists()) {
          GenericErrandClass errand = document.toObject(GenericErrandClass.class);
          populateWithInfo(errand);
        }
      }
    });
  }

  private void populateWithInfo(GenericErrandClass errand) {
    errandDescription.setText(errand.getDescription());
    userName.setText(errand.getCreator().getUserName());
    if (errand.getErrandType().equals(Konstants.IMAGE_ERRAND)) {
      Picasso.get().load(errand.getImages().get(0)).into(errandImage);
    } else {
      errandImage.setVisibility(View.GONE);
    }
    cost.setText("GHS " + errand.getCost());
    allowance.setText("GHS " + errand.getAllowance());
    dateText.setText(errand.getCreatedAt());
    String total = String.valueOf(errand.getCost() + errand.getAllowance());
    String summary = "A total of GHS " + total + " will be sent to your wallet on completing this errand";
    moneySummary.setText(summary);
    pageTitle.setText(errand.getTitle());
    String details = MyHelper.mergeTextsFromArrayWithLines(errand.getDetails());
    if (details.equals(Konstants.INIT_STRING)) {
      removeDetailsBox();
    } else {
      detailsText.setText(details);
    }

    if (errand.getTags().size() != 0) {
      for (int i = 0; i < errand.getTags().size(); i++) {
        String item = errand.getTags().get(i);
        Chip chip = MyHelper.createChipNoClose(this, item);
        ChipDrawable chipDrawable = (ChipDrawable) chip.getChipDrawable();
        chip.setBackgroundDrawable(chipDrawable);
        chipDrawable.setChipBackgroundColorResource(R.color.appColorLight_hollow);
        chip.setTextSize((float) 11);
        chip.setPaddingRelative(5, 5, 5, 5);
        chip.setTextColor(getColor(R.color.appColor));
        tagGroup.addView(chip);

      }
    } else {
      tagGroup.setVisibility(View.GONE);
    }


  }

  private void removeDetailsBox() {
    detailsTitleBox.setVisibility(View.INVISIBLE);
    detailsText.setVisibility(View.INVISIBLE);
  }

  private View.OnClickListener runErrand = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      LayoutInflater inflater = getLayoutInflater();
      View v = inflater.inflate(R.layout.start_errand_custom_dialog, null, false);
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
