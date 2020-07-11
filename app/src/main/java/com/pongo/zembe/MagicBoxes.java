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

//  public Dialog constructTagDialog(final ArrayList<String> tags, final TagDialogContentCallable tagDialogContent) {
//    final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, tags);
//    final ChipGroup group;
//    AutoCompleteTextView textbox;
//    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//    View v = LayoutInflater.from(context).inflate(R.layout.tagging_dialog_layout, null, false);
//    group = v.findViewById(R.id.tags_chip_group);
//    textbox = v.findViewById(R.id.tag_textbox);
//    textbox.setAdapter(adapter);
//    textbox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//      @Override
//      public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
//        final String item = adapterView.getItemAtPosition(i).toString();
//        tags.add(item);
//        group.addView(createChip(item, group, new TagDialogChipActions() {
//          @Override
//          public void removeTag(View v) {
//            group.removeView(v);
//            tags.remove(i);
//            Toast.makeText(context, "Removed : " + item, Toast.LENGTH_SHORT).show();
//          }
//        }));
//      }
//
//      @Override
//      public void onNothingSelected(AdapterView<?> adapterView) {
//
//      }
//    });
//    builder.setView(v)
//      .setTitle("Tagging (Optional)")
//      .setPositiveButton("Save & Continue", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialogInterface, int i) {
//          tagDialogContent.getContent(tags);
//        }
//      })
//      .setNegativeButton("Skip This", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialogInterface, int i) {
//          Toast.makeText(context, "Nothing will happen here bro!", Toast.LENGTH_SHORT).show();
//        }
//      });
//    return builder.create();
//  }

  private Chip createChip(final String name, final ChipGroup group, final TagDialogChipActions tagDialogChipActions) {
    Chip chip = new Chip(context);
    chip.setText(name);
    chip.setCloseIconEnabled(true);
    chip.setOnCloseIconClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        tagDialogChipActions.removeTag(view);
      }
    });
    return chip;
  }


//  ----------------------- END OF THE LINE FOR THIS CLASS -------------------------
}

interface TagDialogChipActions {
  void removeTag(View v);
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
