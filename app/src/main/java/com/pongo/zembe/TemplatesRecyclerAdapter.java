package com.pongo.zembe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TemplatesRecyclerAdapter extends RecyclerView.Adapter<TemplatesRecyclerAdapter.TemplatesRecyclerViewHolder> {
  Context context;
  ArrayList<GenericErrandClass> templateList;
  TemplateItemClick onClickListener;

  public TemplatesRecyclerAdapter(Context context, ArrayList<GenericErrandClass> templateList, TemplateItemClick onClickListener) {
    this.context = context;
    this.templateList = templateList;
    this.onClickListener = onClickListener;
  }


  @NonNull
  @Override
  public TemplatesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.template_recyclable_item, parent, false);
    return new TemplatesRecyclerViewHolder(v, onClickListener);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull TemplatesRecyclerViewHolder holder, int position) {
    GenericErrandClass errand = templateList.get(position);
    holder.title.setText(errand.getTitle());
    holder.cost.setText(String.valueOf(errand.getCost()));
    holder.cost.setText(String.valueOf(errand.getAllowance()));
    holder.date.setText("Saved "+DateHelper.getTimeAgo(errand.getCreatedAt()));
  }

  @Override
  public int getItemCount() {
    return templateList.size();
  }

  public class TemplatesRecyclerViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout container;
    TemplateItemClick listener;
    TextView title,cost, allowance,date;

    public TemplatesRecyclerViewHolder(@NonNull View itemView, final TemplateItemClick listener) {
      super(itemView);
      this.listener = listener;
      this.title = itemView.findViewById(R.id.errand_title);
      this.cost = itemView.findViewById(R.id.estimated_cost_text);
      this.allowance = itemView.findViewById(R.id.allowance_text);
      this.date = itemView.findViewById(R.id.date);
      container = itemView.findViewById(R.id.template_container);
      container.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          GenericErrandClass errand = templateList.get(getAdapterPosition());
          listener.templateClick(getAdapterPosition(),errand);
        }
      });
    }
  }

  public interface TemplateItemClick {
    void templateClick(int position, GenericErrandClass errand);
  }
}
