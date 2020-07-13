package com.pongo.zembe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class NewErrandCreationPage extends AppCompatActivity implements OnDetailItemsClick {

  ArrayList<String> detailsList = new ArrayList<>(), tagList = new ArrayList<>(), autoCompleteList = new ArrayList<>();
  ArrayAdapter<String> locationDropdownAdapter, autoCompleteAdapter;
  ArrayList<String> locationList = new ArrayList<>();
  Spinner locationDropdown;
  RecyclerView recyclerView;
  ImageView taggingTabBtn, quit, helpBtn, userSelectedImageHolder, addDetailsBtn, descriptionTabBtn, estimateTabBtn, allowanceTabBtn, locationTabBtn, detailsTabBtn;
  LinearLayout taggingTab, detailsTab, descriptionTab, estimationTab, allowanceTab, locationTab, pictureTab;
  Button addPictureTabBtn, removePictureBtn;
  EditText detailsBox, allowanceBox, estimatedCostBox, descriptionBox;
  AutoCompleteTextView autoCompleteBox;
  DetailsListAdapter recyclerAdapter;
  String currentTabKey = Konstants.DESC_TAB, selectedLocation = Konstants.CHOOSE;
  Bitmap userSelectedImage = null;
  TextView locationText;
  int DEFAULT_STATE_VALUE = 40, STATE_CHANGED_VALUE = 60;
  Handler handler = new Handler();
  ImageUploadHelper imageHelper;
  Activity activity;
  ChipGroup chipGroup;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_errand_creation_page);
    activity = this;
    initializeAutoCompleteLists();
    initializeActivity();

  }


  private void initializeActivity() {
    //  -----------------------------------------------------------
    imageHelper = new ImageUploadHelper(this);
    autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteList);
    quit = findViewById(R.id.quit);
    helpBtn = findViewById(R.id.help_btn);
    chipGroup = findViewById(R.id.chip_group);
    autoCompleteBox = findViewById(R.id.auto_complete);
    autoCompleteBox.setAdapter(autoCompleteAdapter);
    autoCompleteBox.setOnItemClickListener(completeItemSelected);
    locationText = findViewById(R.id.location_description_text);
    locationDropdown = findViewById(R.id.location_dropdown);
    descriptionTabBtn = findViewById(R.id.tab_for_description_btn);
    taggingTabBtn = findViewById(R.id.tab_for_tagging_btn);
    estimateTabBtn = findViewById(R.id.tab_for_estimated_cost_btn);
    allowanceTabBtn = findViewById(R.id.tab_for_allowance_btn);
    locationTabBtn = findViewById(R.id.tab_for_location_btn);
    locationTab = findViewById(R.id.tab_for_location);
    detailsTabBtn = findViewById(R.id.tab_for_details_btn);
    addPictureTabBtn = findViewById(R.id.tab_for_image_btn);
    detailsTab = findViewById(R.id.tab_for_details);
    descriptionTab = findViewById(R.id.tab_for_description);
    estimationTab = findViewById(R.id.tab_for_estimated_cost);
    allowanceTab = findViewById(R.id.tab_for_allowance);
    taggingTab = findViewById(R.id.tab_for_tagging);
    pictureTab = findViewById(R.id.tab_for_image);
    removePictureBtn = findViewById(R.id.close_btn_in_description);
    userSelectedImageHolder = findViewById(R.id.user_selected_image);
    allowanceBox = findViewById(R.id.allowance_box);
    estimatedCostBox = findViewById(R.id.estimation_box);
    descriptionBox = findViewById(R.id.description_box);
//  ..........................................................
    taggingTabBtn.setOnClickListener(onTabClick(Konstants.TAGGING_TAB, taggingTabBtn, taggingTab));
    descriptionTabBtn.setOnClickListener(onTabClick(Konstants.DESC_TAB, descriptionTabBtn, descriptionTab));
    detailsTabBtn.setOnClickListener(onTabClick(Konstants.DETAILS_TAB, detailsTabBtn, detailsTab));
    estimateTabBtn.setOnClickListener(onTabClick(Konstants.ESTIMATION_TAB, estimateTabBtn, estimationTab));
    allowanceTabBtn.setOnClickListener(onTabClick(Konstants.ALLOWANCE_TAB, allowanceTabBtn, allowanceTab));
    addPictureTabBtn.setOnClickListener(showImageDIv);
    removePictureBtn.setOnClickListener(removeSelectedImage);
    locationTabBtn.setOnClickListener(onTabClick(Konstants.LOCATION_TAB, locationTabBtn, locationTab));
    helpBtn.setOnClickListener(showErrandHelp);
    userSelectedImageHolder.setOnClickListener(chooseImageForErrand);
    quit.setOnClickListener(quitCreating);

//  ----------------------------------------------------------
    locationList.add(Konstants.CHOOSE);
    locationList.add("Home");
    locationList.add("School");
    locationList.add("Club");
    locationDropdownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, locationList);
    locationDropdown.setAdapter(locationDropdownAdapter);
    recyclerView = findViewById(R.id.recyclerview_in_details_tab);
    addDetailsBtn = findViewById(R.id.add_btn_in_details_tab);
    detailsBox = findViewById(R.id.edittext_in_details_tab);
    recyclerAdapter = new DetailsListAdapter(this, detailsList, this);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(recyclerAdapter);
    recyclerView.hasFixedSize();
    addDetailsBtn.setOnClickListener(addToDetailsList);
    locationDropdown.setOnItemSelectedListener(chooseLocation);
    startInvigilatingInfinitely();
  }

  private void initializeAutoCompleteLists() {
    autoCompleteList.add("January");
    autoCompleteList.add("February");
    autoCompleteList.add("March");
    autoCompleteList.add("April");
    autoCompleteList.add("May");
    autoCompleteList.add("June");
    autoCompleteList.add("July");
  }

  private AdapterView.OnItemClickListener completeItemSelected = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
      String item = adapterView.getItemAtPosition(i).toString();
      tagList.add(item);
      Chip newTag = RandomHelpersClass.createChip(activity, item, new GalInterfaceGuru.TagDialogChipActions() {
        @Override
        public void removeTag(View v) {
          chipGroup.removeView(v);
          tagList.remove(i);
        }
      });
      chipGroup.addView(newTag);
      autoCompleteBox.setText("");
    }
  };

  private View.OnClickListener quitCreating = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      finish();
    }
  };
  private View.OnClickListener chooseImageForErrand = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      imageHelper.openFileChooserWithCropper(activity, 6, 6);
    }
  };

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    imageHelper.collectCroppedImage(requestCode, resultCode, data, new ImageUploadHelper.CroppingImageCallback() {
      @Override
      public void getCroppedImage(Uri uri) {
        imageHelper.compressImageToBytes(uri, new ImageUploadHelper.CompressedImageToBytesCallback() {
          @Override
          public void getCompressedImage(byte[] compressedImage) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(compressedImage, 0, compressedImage.length);
            userSelectedImage = bitmap;
            userSelectedImageHolder.setImageBitmap(bitmap);
          }
        });

      }

      @Override
      public void getCroppingError(Exception e) {
        e.printStackTrace();
        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private View.OnClickListener showErrandHelp = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent help = new Intent(NewErrandCreationPage.this, TeachMeHowToCreateAnErrand.class);
      startActivity(help);
    }
  };

  private void startInvigilatingInfinitely() {
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        errandStepsInvigilator();
        // restart the whole process again
        startInvigilatingInfinitely();
      }
    }, 2000);

  }

  @Override
  protected void onResume() {
    super.onResume();
    startInvigilatingInfinitely();
  }

  @Override
  protected void onPause() {
    handler.removeCallbacksAndMessages(null);
    super.onPause();
  }

  private void errandStepsInvigilator() {
    //if description has been provided
    //if estimated cost has been provided
    //if allowance cost has been provided
    //if location has been provided
    //if extra details have been provided
    if (!descriptionBox.getText().toString().isEmpty()) {
      descriptionTabBtn.setImageResource(R.drawable.ic_description_green);
    } else {
      descriptionTabBtn.setImageResource(R.drawable.ic_description_black_24dp);
    }

    if (!estimatedCostBox.getText().toString().isEmpty()) {
      estimateTabBtn.setImageResource(R.drawable.ic_payment_green);
    } else {
      estimateTabBtn.setImageResource(R.drawable.errand_payment_icon);
    }
    if (!allowanceBox.getText().toString().isEmpty()) {
      allowanceTabBtn.setImageResource(R.drawable.ic_card_giftcard_green);
    } else {
      allowanceTabBtn.setImageResource(R.drawable.ic_card_giftcard_black_24dp);
    }

    if (!selectedLocation.equals(Konstants.CHOOSE)) {
      locationTabBtn.setImageResource(R.drawable.ic_location_green);
    } else {
      locationTabBtn.setImageResource(R.drawable.location_vector_icon);
    }


    if (detailsList.size() != 0) {
      detailsTabBtn.setImageResource(R.drawable.ic_list_green);
    } else {
      detailsTabBtn.setImageResource(R.drawable.ic_list);
    }
    if (tagList.size() != 0) {
      taggingTabBtn.setImageResource(R.drawable.label_active);
    } else {
      taggingTabBtn.setImageResource(R.drawable.label_normal);
    }
  }

  private AdapterView.OnItemSelectedListener chooseLocation = new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      selectedLocation = adapterView.getItemAtPosition(i).toString();
      String text = "Your items(s) will be delivered at '" + selectedLocation + "'";
      locationText.setText(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
  };
  private View.OnClickListener removeSelectedImage = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      pictureTab.setVisibility(View.GONE);
      removePictureBtn.setVisibility(View.GONE);
      userSelectedImage = null;
      userSelectedImageHolder.setImageResource(R.drawable.galam_wakye);
    }
  };
  private View.OnClickListener showImageDIv = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      addPictureTabBtn.setAlpha(1);
      pictureTab.setVisibility(View.VISIBLE);
      removePictureBtn.setVisibility(View.VISIBLE);
    }
  };

  private View.OnClickListener onTabClick(final String whichPage, final ImageView newPageBtn, final LinearLayout newPage) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        takeMeToThisTab(whichPage, newPageBtn, newPage);
      }
    };
  }

  private void takeMeToThisTab(String whichPage, ImageView newPageBtn, LinearLayout newPage) {
    if (currentTabKey.equals(whichPage)) {
      return;
    }
    ImageView oldPageBtn = bringMeTabButton(currentTabKey);
    LinearLayout oldPage = bringMeTabPage(currentTabKey);
    changeTabBtnState(oldPageBtn, Konstants.INACTIVE);
    changeTabBtnState(newPageBtn, Konstants.ACTIVE);
    oldPage.setVisibility(View.GONE);
    newPage.setVisibility(View.VISIBLE);
    currentTabKey = whichPage;

  }

  private void changeTabBtnState(ImageView btn, String how) {
    if (btn == null) return;
    switch (how) {
      case Konstants.INACTIVE: {
        btn.getLayoutParams().width = DEFAULT_STATE_VALUE;
        btn.getLayoutParams().height = DEFAULT_STATE_VALUE;
        break;
      }
      case Konstants.ACTIVE: {
        btn.getLayoutParams().width = STATE_CHANGED_VALUE;
        btn.getLayoutParams().height = STATE_CHANGED_VALUE;
        break;
      }
    }
    btn.requestLayout();
  }

  private ImageView bringMeTabButton(String whichTab) {
    switch (whichTab) {
      case Konstants.DESC_TAB: {
        return descriptionTabBtn;
      }
      case Konstants.LOCATION_TAB: {
        return locationTabBtn;
      }
      case Konstants.DETAILS_TAB: {
        return detailsTabBtn;
      }
      case Konstants.ESTIMATION_TAB: {
        return estimateTabBtn;
      }
      case Konstants.ALLOWANCE_TAB: {
        return allowanceTabBtn;
      }
      case Konstants.TAGGING_TAB: {
        return taggingTabBtn;
      }
    }
    return null;
  }

  private LinearLayout bringMeTabPage(String whichTab) {

    switch (whichTab) {
      case Konstants.IMAGE_TAB: {
        return pictureTab;
      }
      case Konstants.DESC_TAB: {
        return descriptionTab;
      }
      case Konstants.LOCATION_TAB: {
        return locationTab;
      }
      case Konstants.DETAILS_TAB: {
        return detailsTab;
      }
      case Konstants.ALLOWANCE_TAB: {
        return allowanceTab;
      }
      case Konstants.ESTIMATION_TAB: {
        return estimationTab;
      }
      case Konstants.TAGGING_TAB: {
        return taggingTab;
      }
    }
    return null;
  }

  private View.OnClickListener addToDetailsList = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      if (validateDetails()) {
        detailsList.add(detailsBox.getText().toString().trim());
        recyclerAdapter.notifyDataSetChanged();
        detailsBox.setText("");
      }
    }
  };

  public Boolean validateDetails() {
    Boolean good = true;
    String det = detailsBox.getText().toString().trim();
    if (det.isEmpty()) {
      detailsBox.setError("Add a specific instruction before you add");
      detailsBox.requestFocus();
      good = false;
    }
    return good;
  }

  @Override
  public void onDetailItemClick(int pos) {
    detailsList.remove(pos);
    recyclerAdapter.notifyItemRemoved(pos);
  }


}
