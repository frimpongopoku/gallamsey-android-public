package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class NewErrandCreationPage extends AppCompatActivity implements OnDetailItemsClick, RiderForSelectionRecyclerAdapter.SelectRiderCallback {
  SimpleUser creator;
  ArrayList<String> fakeSelectedRiders = new ArrayList<>(), detailsList = new ArrayList<>(), tagList = new ArrayList<>(), autoCompleteList = new ArrayList<>();
  ArrayAdapter<String> locationDropdownAdapter, autoCompleteAdapter;
  ArrayAdapter<CharSequence> expiryDateAdapter;
  Spinner expiryDateDropDown;
  ArrayList<String> locationList = new ArrayList<>();
  Spinner locationDropdown;
  RecyclerView recyclerView, ridersRecyclerView;
  ImageView taggingTabBtn, quit, helpBtn, userSelectedImageHolder, addDetailsBtn, descriptionTabBtn, estimateTabBtn, allowanceTabBtn, locationTabBtn, detailsTabBtn;
  LinearLayout selectRidersTab, taggingTab, detailsTab, descriptionTab, estimationTab, allowanceTab, locationTab, pictureTab;
  Button saveAsTemplateBtn, postBtn, addPictureTabBtn, removePictureBtn, selectRidersBtn;
  EditText detailsBox, allowanceBox, estimatedCostBox, descriptionBox;
  AutoCompleteTextView autoCompleteBox;
  DetailsListAdapter recyclerAdapter;
  String currentTabKey = Konstants.DESC_TAB, selectedLocation = Konstants.CHOOSE, expiryDurationSelected = Konstants.NOT_SET;
  byte[] userSelectedImage = null; //this is what is going to be uploaded
  Uri selectedImageURI; // image extension will be determined from this
  TextView locationText, durationText;
  int DEFAULT_STATE_VALUE = 40, STATE_CHANGED_VALUE = 60;
  Handler handler = new Handler();
  ImageUploadHelper imageHelper;
  Activity activity;
  ChipGroup chipGroup, ridersChipGroup;
  RiderForSelectionRecyclerAdapter riderSelectionAdapter;
  FirebaseFirestore store = FirebaseFirestore.getInstance();
  CollectionReference errandDB = store.collection(Konstants.ERRAND_COLLECTION);
  MagicBoxes dialogCreator;
  GroundUser authenticatedUser;
  StorageReference storageReference;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_errand_creation_page);
    activity = this;
    dialogCreator = new MagicBoxes(this);
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    if (authenticatedUser != null) {
      makeSimpleUserFrom(authenticatedUser);
    }
    initializeAutoCompleteLists();
    initializeActivity();

  }


  public void goToPaymentPage(GenericErrandClass errand) {
    Intent page = new Intent(this, ErrandPaymentStepPage.class);
    page.putExtra(Konstants.PASS_ERRAND_AROUND, errand);
    startActivity(page);
    finish();
  }

  public void showLoader(String toggler) {
    CardView card = findViewById(R.id.spinner_holder);
    View v = findViewById(R.id.background_transparent);
    if (toggler.equals(Konstants.DO)) {
      v.setVisibility(View.VISIBLE);
      v.invalidate();
      card.setVisibility(View.VISIBLE);
      card.bringToFront();
      selectRidersBtn.setVisibility(View.GONE);
    } else {
      v.setVisibility(View.GONE);
      v.invalidate();
      card.setVisibility(View.GONE);
      selectRidersBtn.setVisibility(View.VISIBLE);
    }


  }

  public void publishErrand() {
    showLoader(Konstants.DO);
    getErrandForShipment(new GalInterfaceGuru.CollectErrandTrainFormShipment() {
      @Override
      public void getErrandObject(final GenericErrandClass errand) {
        String id = errandDB.document().getId(); // get id before its saved, so we can save in the document itself
        errand.setErrandDocumentID(id);
        errandDB.document(id).set(errand).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            goToPaymentPage(errand);
          }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            showLoader(Konstants.UNDO);
            e.printStackTrace();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        });

      }
    });
  }

  private void makeSimpleUserFrom(GroundUser user) {
    creator = new SimpleUser(
      user.getUserDocumentID(),
      user.getPreferredName(),
      user.getPhoneNumber(),
      user.getUserType(),
      user.getProfilePictureURL(),
      Konstants.CREATOR)
    ;
  }

  private void getErrandForShipment(final GalInterfaceGuru.CollectErrandTrainFormShipment errandCallback) {
    String description = MyHelper.grabCleanText(descriptionBox), estimated = MyHelper.grabCleanText(estimatedCostBox), allowance = MyHelper.grabCleanText(allowanceBox);
    final GenericErrandClass errand = new GenericErrandClass(Konstants.NOT_SET, description);
    String date = DateHelper.getDateInMyTimezone();
    errand.setTags(tagList);
    errand.setCost(Float.valueOf(estimated));
    errand.setAllowance(Float.valueOf(allowance));
    errand.setDetails(detailsList);
    errand.setCreator(creator);
    errand.setExpiryDate(
        DateHelper.getMilliSecondsFromDate(
          DateHelper.jumpDateByHours(
            date, DateHelper.getHoursValueFromDurationString(
              expiryDurationSelected
            )
          )
        )
    );
    //    errand.setPickUpLocation(selectedLocation);
//    errand.setNotifiableRiders(fakeSelectedRiders);
    if (userSelectedImage == null) {

      errand.setErrandType(Konstants.TEXT_ERRAND);
      errandCallback.getErrandObject(errand);

    } else {

      errand.setErrandType(Konstants.IMAGE_ERRAND);
      imageHelper.uploadImageToFirebase(
        storageReference,
        selectedImageURI,
        Konstants.IMG_ERRAND_CONSTANT,
        authenticatedUser.getUserDocumentID(),
        userSelectedImage,
        new ImageUploadHelper.CollectUploadedImageURI() {
          @Override
          public void getURI(Uri uri) {
            ArrayList<String> imgs = new ArrayList<>();
            imgs.add(uri.toString());
            errand.setImages(imgs);
            errandCallback.getErrandObject(errand);
          }
        });
    }


  }

  private View.OnClickListener postMyErrand = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      SimpleError result = validateErrand();
      String errorMsg = result.getErrorMessage(), fatal = Konstants.INIT_STRING, semi = Konstants.INIT_STRING;
      if (!result.getStatus().equals(Konstants.ERROR_PASSED)) {
        fatal = errorMsg.split("<==>")[0];
        semi = errorMsg.split("<==>")[1];
      }
      if (result.getStatus().equals(Konstants.ERROR_PASSED)) {
        publishErrand();

      }
      if (result.getStatus().equals(Konstants.ERROR_SEMI_PASSED)) {
        dialogCreator.constructErrandErrorDialog("Confirmation", fatal, semi, "Go back & Edit", "Post Anyway", new MagicBoxCallables() {
          @Override
          public void negativeBtnCallable() {

          }

          @Override
          public void positiveBtnCallable() {
            publishErrand();

          }
        }).show();
      }
      if (result.getStatus().equals(Konstants.ERROR_FAILED)) {
        dialogCreator.constructErrandErrorDialog("Not Ready Yet", fatal, semi, "Quit", "Go back & Edit", new MagicBoxCallables() {
          @Override
          public void negativeBtnCallable() {
            finish();
          }

          @Override
          public void positiveBtnCallable() {

          }
        }).show();
      }
    }
  };

  private SimpleError validateErrand() {
    SimpleError error = new SimpleError();
    int count = 0;
    String errorString = "";
    String errorForOptionalFields = "";
    if (descriptionBox.getText().toString().isEmpty()) {
      count++;
      errorString = MyHelper.concactToWhat(errorString, count + ". You did not provide a description for your errand");
      error.setStatus(Konstants.ERROR_FAILED);
    }

    if (expiryDurationSelected.equals(Konstants.NOT_SET)) {
      count++;
      errorString = MyHelper.concactToWhat(errorString, count + ". When should your errand expire?");
      error.setStatus(Konstants.ERROR_FAILED);
    }
    if (estimatedCostBox.getText().toString().isEmpty()) {
      count++;
      errorString = MyHelper.concactToWhat(errorString, count + ". You need to provide a value for how much your item(s) will cost");
      error.setStatus(Konstants.ERROR_FAILED);
    }
    if (allowanceBox.getText().toString().isEmpty()) {
      count++;
      errorString = MyHelper.concactToWhat(errorString, count + ". You need to provide a value for how much you are willing to give");
      error.setStatus(Konstants.ERROR_FAILED);
    }
    if (selectedLocation.equals(Konstants.CHOOSE)) {
      count++;
      errorForOptionalFields = MyHelper.concactToWhat(errorForOptionalFields, count + ". No destination to receive items was provided. You can change now, or just chat with your rider later");
      if (!error.getStatus().equals(Konstants.ERROR_FAILED))
        error.setStatus(Konstants.ERROR_SEMI_PASSED);
    }
    if (detailsList.size() == 0) {
      count++;
      errorForOptionalFields = MyHelper.concactToWhat(errorForOptionalFields, count + ". No particular details were provided for this errand");
      if (!error.getStatus().equals(Konstants.ERROR_FAILED))
        error.setStatus(Konstants.ERROR_SEMI_PASSED);
    }
    if (tagList.size() == 0) {
      count++;
      errorForOptionalFields = MyHelper.concactToWhat(errorForOptionalFields, count + ". It is always better to add tags to you errands");
      if (!error.getStatus().equals(Konstants.ERROR_FAILED))
        error.setStatus(Konstants.ERROR_SEMI_PASSED);
    }
    if (fakeSelectedRiders.size() == 0) {
      count++;
      errorForOptionalFields = MyHelper.concactToWhat(errorForOptionalFields, count + ". No riders have been selected");
      if (!error.getStatus().equals(Konstants.ERROR_FAILED))
        error.setStatus(Konstants.ERROR_SEMI_PASSED);
    }
    if (!errorForOptionalFields.trim().isEmpty()) {
      errorForOptionalFields = errorForOptionalFields + "\n-Hopefully, you know what you are doing...";
    }

    String bothErrors = errorString + "<==>" + errorForOptionalFields;
    error.setErrorMessage(bothErrors);
//    error.setErrorMessage(MyHelper.concactToWhat(errorString,"Other things (optional) things you missed\n"+errorForOptionalFields));
    return error;
  }

  private void initializeActivity() {
    //  -----------------------------------------------------------
    saveAsTemplateBtn = findViewById(R.id.template_btn);
    storageReference = FirebaseStorage.getInstance().getReference(Konstants.ERRAND_PICTURES_COLLECTION);
    imageHelper = new ImageUploadHelper(this);
    durationText = findViewById(R.id.duration_text_label);
    ridersChipGroup = findViewById(R.id.select_rider_chip_group);
    expiryDateDropDown = findViewById(R.id.errand_expiry_spinner);
    expiryDateAdapter = ArrayAdapter.createFromResource(this, R.array.errand_expiry_values, android.R.layout.simple_list_item_1);
    expiryDateAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
    expiryDateDropDown.setAdapter(expiryDateAdapter);
    postBtn = findViewById(R.id.post_btn);
    autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteList);
    quit = findViewById(R.id.quit);
    selectRidersBtn = findViewById(R.id.select_riders_btn);
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
    selectRidersTab = findViewById(R.id.tab_for_selecting_riders);
    allowanceTab = findViewById(R.id.tab_for_allowance);
    taggingTab = findViewById(R.id.tab_for_tagging);
    pictureTab = findViewById(R.id.tab_for_image);
    removePictureBtn = findViewById(R.id.close_btn_in_description);
    userSelectedImageHolder = findViewById(R.id.user_selected_image);
    allowanceBox = findViewById(R.id.allowance_box);
    estimatedCostBox = findViewById(R.id.estimation_box);
    descriptionBox = findViewById(R.id.description_box);
//  ..........................................................
    selectRidersBtn.setOnClickListener(onTabClick(Konstants.SELECT_RIDERS_TAB, null, selectRidersTab));
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
    postBtn.setOnClickListener(postMyErrand);
    expiryDateDropDown.setOnItemSelectedListener(selectExpiryDate);
    saveAsTemplateBtn.setOnClickListener(saveAsTemplate);

//  ----------------------------------------------------------
    locationList.add(Konstants.CHOOSE);
    locationList.add("Home");
    locationList.add("School");
    locationList.add("Club");
    locationDropdownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationList);
    locationDropdownAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
    locationDropdown.setAdapter(locationDropdownAdapter);
    recyclerView = findViewById(R.id.recyclerview_in_details_tab);
    addDetailsBtn = findViewById(R.id.add_btn_in_details_tab);
    detailsBox = findViewById(R.id.edittext_in_details_tab);
    recyclerAdapter = new DetailsListAdapter(this, detailsList, this);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(recyclerAdapter);
    recyclerView.hasFixedSize();
    ridersRecyclerView = findViewById(R.id.select_rider_recycler);
    LinearLayoutManager riderRecyclerManager = new LinearLayoutManager(this);
    ridersRecyclerView.setLayoutManager(riderRecyclerManager);
    riderSelectionAdapter = new RiderForSelectionRecyclerAdapter(this, null, this);
    ridersRecyclerView.setAdapter(riderSelectionAdapter);
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


  private View.OnClickListener saveAsTemplate = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      goToPaymentPage(null);
    }
  };
  private AdapterView.OnItemSelectedListener selectExpiryDate = new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      String duration = adapterView.getItemAtPosition(i).toString();
      expiryDurationSelected = duration;
      if (duration.equals(Konstants.NOT_SET)) {
        durationText.setText("");
      } else {
        durationText.setText("You errand is set to expire in : " + expiryDurationSelected);
      }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
  };
  private AdapterView.OnItemClickListener completeItemSelected = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
      String item = adapterView.getItemAtPosition(i).toString();
      tagList.add(item);
      Chip newTag = MyHelper.createChip(activity, item, new GalInterfaceGuru.TagDialogChipActions() {
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
        selectedImageURI = uri;
        imageHelper.compressImageToBytes(uri, new ImageUploadHelper.CompressedImageToBytesCallback() {
          @Override
          public void getCompressedImage(byte[] compressedImage) {
            userSelectedImage = compressedImage;
            userSelectedImageHolder.setImageBitmap(ImageUploadHelper.changeBytesToBitmap(compressedImage));
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
    if (!descriptionBox.getText().toString().isEmpty() || userSelectedImage != null) {
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


    if (detailsList.size() != 0 && !expiryDurationSelected.equals(Konstants.NOT_SET)) {
      detailsTabBtn.setImageResource(R.drawable.ic_list_green);
    } else {
      detailsTabBtn.setImageResource(R.drawable.ic_list);
    }
    if (tagList.size() != 0) {
      taggingTabBtn.setImageResource(R.drawable.label_active);
    } else {
      taggingTabBtn.setImageResource(R.drawable.label_normal);
    }
    if (fakeSelectedRiders.size() != 0) {
      selectRidersBtn.setBackgroundResource(R.drawable.just_a_white_circle);
      selectRidersBtn.setText(String.valueOf(fakeSelectedRiders.size()));
    } else {
      selectRidersBtn.setBackgroundResource(R.drawable.select_rider_button);
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
      case Konstants.SELECT_RIDERS_TAB: {
        return selectRidersTab;
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


  @Override
  public void onRiderClick(final int position) {
    fakeSelectedRiders.add("Somen random");
    riderSelectionAdapter.notifyItemRemoved(position);
    Chip chip = MyHelper.createChip(this, "Somen random", new GalInterfaceGuru.TagDialogChipActions() {
      @Override
      public void removeTag(View v) {
        fakeSelectedRiders.remove(position);
        ridersChipGroup.removeView(v);
        Toast.makeText(activity, "I have been removed", Toast.LENGTH_SHORT).show();
      }
    });
    ridersChipGroup.addView(chip);

  }
}
