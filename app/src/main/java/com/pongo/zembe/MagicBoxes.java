package com.pongo.zembe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

public class MagicBoxes extends AppCompatDialogFragment {

  private final Context context;

  public MagicBoxes(Context context) {
    this.context = context;
  }

  public Dialog constructASimpleDialog(String title, String msg, final MagicBoxCallables magicInterface){
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


  public Dialog constructCustomDialog(String title, View v, final MagicBoxCallables magicInterface){
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

  }public Dialog constructCustomDialogOneAction(String title, View v,String actionTitle, final MagicBoxCallables magicInterface){
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
}

interface  MagicBoxCallables {
  void negativeBtnCallable();
  void positiveBtnCallable();
}
