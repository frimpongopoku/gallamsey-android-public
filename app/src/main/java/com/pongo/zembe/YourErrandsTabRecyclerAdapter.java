package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pongo.zembe.GigsTabRecyclerAdapter;

import java.util.ArrayList;

public class YourErrandsTabRecyclerAdapter extends RecyclerView.Adapter<YourErrandsTabRecyclerAdapter.YourErrandsTabRecyclerViewHolder> {
  Context context;
  ArrayList<GenericErrandClass> listOfErrands = new ArrayList<>();
  YourErrandItemClick listener;

  public YourErrandsTabRecyclerAdapter(Context context, ArrayList<GenericErrandClass> listOfErrands, YourErrandItemClick listener) {
    this.context = context;
    this.listOfErrands = listOfErrands;
    this.listener = listener;
  }


  @NonNull
  @Override
  public YourErrandsTabRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.created_errands_recyclable, parent, false);
    return new YourErrandsTabRecyclerViewHolder(v, this.listener);
  }


  @Override
  public void onBindViewHolder(@NonNull YourErrandsTabRecyclerViewHolder holder, int position) {
    GenericErrandClass errand = listOfErrands.get(position);
    String desc = errand.getDescription() != null ? errand.getDescription() : "Was created without a description...";
    desc = desc.length() > 160 ? desc.substring(160) + "..." : desc;
    holder.desc.setText(desc);
    holder.status.setText(Konstants.ERRAND_STATUS_MAP.get(errand.getStatus()));
    holder.cost.setText(String.valueOf(errand.getCost()));
    holder.allowance.setText(String.valueOf(errand.getAllowance()));
    holder.dateText.setText(DateHelper.getTimeAgo(errand.getCreatedAt()));
  }

  @Override
  public int getItemCount() {
    return listOfErrands.size();
  }

  public interface YourErrandItemClick {
    void onYourErrandItemClick(int position);
  }

  public class YourErrandsTabRecyclerViewHolder extends RecyclerView.ViewHolder {
    YourErrandItemClick listener;
    RelativeLayout container;
    TextView desc, dateText, status, cost, allowance;

    public YourErrandsTabRecyclerViewHolder(@NonNull View itemView, final YourErrandItemClick listener) {
      super(itemView);
      this.listener = listener;
      this.container = itemView.findViewById(R.id.main_container);
      this.desc = itemView.findViewById(R.id.desc);
      this.dateText = itemView.findViewById(R.id.date);
      this.status = itemView.findViewById(R.id.errand_status);
      this.cost = itemView.findViewById(R.id.estimated_cost_text);
      this.allowance = itemView.findViewById(R.id.allowance_text);
      container.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          listener.onYourErrandItemClick(getAdapterPosition());
        }
      });
    }
  }
}
