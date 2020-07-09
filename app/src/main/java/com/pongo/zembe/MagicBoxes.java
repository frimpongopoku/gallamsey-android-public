package com.pongo.zembe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;

import java.util.ArrayList;

public class MagicBoxes extends AppCompatDialogFragment {

  private final Context context;

  public MagicBoxes(Context context) {
    this.context = context;
  }

  public Dialog constructASimpleDialog(String title, String msg, final MagicBoxCallables magicInterface) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle(title)
      .setMessage(msg)
      .setNegativeButton("NO", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          magicInterface.negativeBtnCallable();
        }
      }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        magicInterface.positiveBtnCallable();
      }
    });
    return builder.create();
  }


  public Dialog constructCustomDialog(String title, View v, final MagicBoxCallables magicInterface) {
//    LayoutInflater inflater = getActivity().getLayoutInflater();
//    View view = inflater.inflate(R.layout.start_errand_custom_dialog,null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(v)
      .setTitle(title)
      .setPositiveButton("Start Now", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          magicInterface.positiveBtnCallable();
        }
      })
      .setNegativeButton("Add to list", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          magicInterface.negativeBtnCallable();
        }
      });

    return builder.create();

  }

  public Dialog constructCustomDialogOneAction(String title, View v, String actionTitle, final MagicBoxCallables magicInterface) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(v)
      .setTitle(title)
      .setPositiveButton(actionTitle, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          magicInterface.positiveBtnCallable();
        }
      });

    return builder.create();

  }

  public Dialog constructRatingDialog(String contentHeadline, Bitmap bitmap, final RatingButtonActions buttonActions) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    View v = LayoutInflater.from(context).inflate(R.layout.smiley_rating_dialog, null, false);
    TextView headline = v.findViewById(R.id.headline_text);
    headline.setText(contentHeadline);
    final SmileyRating smileyRator = v.findViewById(R.id.smile_rating);
    ImageView img = v.findViewById(R.id.rider_profile_img);
    builder.setView(v).setPositiveButton("Done", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        buttonActions.onFinish(smileyRator.getSelectedSmiley().getRating());
      }
    });
    return builder.create();
  }


//  public Dialog constructDialogForMobileNumberAddition(final ArrayList<PaymentContact> initialArray, final PhoneNumberDialogContentCallable callable) {
//    final EditText addingBox, nameBox;
//    Button addButton;
//    RecyclerView recyclerView ;
//    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//    View v = LayoutInflater.from(context).inflate(R.layout.add_mobile_number_custom_view_for_dialog, null, false);
//    addingBox = v.findViewById(R.id.adding_textbox);
//    nameBox = v.findViewById(R.id.name_textbox);
//    addButton = v.findViewById(R.id.add_btn);
//    recyclerView = v.findViewById(R.id.phone_numbers_recycler);
//    final PhoneNumberRecyclerAdapter recyclerAdapter = new PhoneNumberRecyclerAdapter(context,initialArray);
//    LinearLayoutManager manager = new LinearLayoutManager(context);
//    recyclerView.setLayoutManager(manager);
//    recyclerView.setAdapter(recyclerAdapter);
//    addButton.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        String name, phone;
//        name = nameBox.getText().toString().trim();
//        phone = addingBox.getText().toString().trim();
//        if(!name.isEmpty() && !phone.isEmpty()){
//          initialArray.add(new PaymentContact(name,phone));
//          recyclerAdapter.notifyDataSetChanged();
//          addingBox.setText("");
//          nameBox.setText("");
//          nameBox.requestFocus();
//        }
//        else{
//          Toast.makeText(context, "Make sure you have put in name and a phone number", Toast.LENGTH_SHORT).show();
//        }
//      }
//    });
//    builder.setView(v)
//      .setTitle("Add Payment Mobile Number")
//      .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialogInterface, int i) {
//          callable.getContent(initialArray);
//
//        }
//      });
//    return builder.create();
//  }


}

interface PhoneNumberDialogContentCallable {
  void getContent(ArrayList<PaymentContact> phoneNumbers);
}

interface RatingButtonActions {
  void onFinish(int rating);
}

interface MagicBoxCallables {
  void negativeBtnCallable();

  void positiveBtnCallable();
}
