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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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

  } public Dialog createErrandErrorDialog(String title, String fatal, String semiError, final MagicBoxCallables magicInterface) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.errand_error_dialog_layout,null,false);
    TextView fatalHeader, fatalErr, semiErrHeader, semiErr;
    fatalHeader = view.findViewById(R.id.fatal_error_header);
    fatalErr = view.findViewById(R.id.fatal_error);
    semiErrHeader = view.findViewById(R.id.semi_error_header);
    semiErr = view.findViewById(R.id.semi_error);
    if(fatal.isEmpty()){
      fatalHeader.setVisibility(View.GONE);
      fatalErr.setVisibility(View.GONE);
    }
    fatalErr.setText(fatal);
    semiErr.setText(semiError);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view)
      .setTitle(title)
      .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          magicInterface.positiveBtnCallable();
        }
      })
      .setNegativeButton("quit", new DialogInterface.OnClickListener() {
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





//  ----------------------- END OF THE LINE FOR THIS CLASS -------------------------
}



interface TagDialogContentCallable {
  void getContent(ArrayList<String> tags);
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
