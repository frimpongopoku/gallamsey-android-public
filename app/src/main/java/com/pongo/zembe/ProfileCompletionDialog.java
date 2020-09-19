package com.pongo.zembe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ProfileCompletionDialog extends AppCompatDialogFragment {
  GroundUser authenticatedUser = new GroundUser();

  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }

  @NonNull
  @Override

  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("Congratulations")
      .setMessage("Well done! Your profile has been updated successfully.\nWhile you are at it, would you like to set your home location too? ")
      .setNegativeButton("NO, Skip This", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          Toast.makeText(getContext(), "Redirecting...", Toast.LENGTH_LONG).show();
          Intent home = new Intent(getContext(), Home.class);
          home.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
          startActivity(home);
          if (getActivity() != null) getActivity().finish();
        }
      }).setPositiveButton("YES, I Want To", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        Toast.makeText(getContext(), "Redirecting...", Toast.LENGTH_LONG).show();
        Intent locationPage = new Intent(getContext(), AddMoreLocations.class);
        locationPage.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
        locationPage.putExtra(Konstants.COMING_FROM_PROFILE_EDIT,Konstants.COMING_FROM_PROFILE_EDIT);
        startActivity(locationPage);
        if (getActivity() != null) getActivity().finish();
      }
    });
    return builder.create();
  }
}
