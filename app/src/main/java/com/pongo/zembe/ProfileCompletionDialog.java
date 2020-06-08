package com.pongo.zembe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ProfileCompletionDialog extends AppCompatDialogFragment {

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("Congratulations")
      .setMessage("Well done! Your profile has been updated successfully.\nWhile you are at it, would you like to set your home location too? ")
      .setNegativeButton("NO, Skip This", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          Intent home = new Intent(getContext(),Home.class);
          startActivity(home);
        }
      }).setPositiveButton("YES, I Want To", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        Intent locationPage = new Intent(getContext(),SetLocationsPage.class);
        startActivity(locationPage);
      }
    });
    return builder.create();
  }
}
