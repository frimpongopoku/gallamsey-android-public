package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TempatesRecyclerAdapter extends RecyclerView.Adapter<TempatesRecyclerAdapter.TemplatesRecyclerViewHolder> {
  Context context;
  ArrayList<Errand> templateList;
  TemplateItemClick onClickListener;

  public TempatesRecyclerAdapter(Context context, ArrayList<Errand> templateList, TemplateItemClick onClickListener) {
    this.context = context;
    this.templateList = templateList;
    this.onClickListener = onClickListener;
  }


  @NonNull
  @Override
  public TemplatesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.template_recyclable_item, parent,false);
    return new TemplatesRecyclerViewHolder(v,onClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull TemplatesRecyclerViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 8;
  }

  public class TemplatesRecyclerViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout container;
    TemplateItemClick listener;
    public TemplatesRecyclerViewHolder(@NonNull View itemView, final TemplateItemClick listener) {
      super(itemView);
      this.listener = listener;
      container = itemView.findViewById(R.id.template_container);
      container.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          listener.onClick(getAdapterPosition());
        }
      });
    }
  }

  public interface TemplateItemClick {
    void onClick(int position);
  }
}
