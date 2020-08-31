package com.pongo.zembe;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class SpeedyRecycler extends RecyclerView {
  Context context;
  public SpeedyRecycler(@NonNull Context context) {
    super(context);
  }

  public SpeedyRecycler(@NonNull Context context, @Nullable AttributeSet attrs, Context context1) {
    super(context, attrs);
    this.context = context1;
  }

  public SpeedyRecycler(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle, Context context1) {
    super(context, attrs, defStyle);
    this.context = context1;
  }
  @Override
  public boolean fling(int velocityX, int velocityY) {

    velocityY *= 0.7;
    // velocityX *= 0.7; for Horizontal recycler view. comment velocityY line not require for Horizontal Mode.

    return super.fling(velocityX, velocityY);
  }
}
