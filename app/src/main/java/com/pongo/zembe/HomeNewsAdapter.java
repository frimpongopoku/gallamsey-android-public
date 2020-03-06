package com.pongo.zembe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class HomeNewsAdapter extends RecyclerView.Adapter<HomeNewsAdapter.ViewHolder> {

    String TAG = "Adapter has started...";
    ArrayList<String> Descriptions = new ArrayList<>();
    ArrayList<String> Profits = new ArrayList<>();
    ArrayList<String> Costs = new ArrayList<>();
    ArrayList<String> Dates = new ArrayList<>();
    Context Context ;

  public HomeNewsAdapter(Context context, ArrayList<String> descriptions, ArrayList<String> profits, ArrayList<String> costs, ArrayList<String> dates ) {
    Descriptions = descriptions;
    Profits = profits;
    Costs = costs;
    Dates = dates;
    this.Context = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //connect view to target card text card (...local words  lol )
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_errand_card,parent, false);
    //connect card to the view holder ( ...local words lol )
    ViewHolder holder = new ViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
    Log.d(TAG, "onBindViewHolder: "+pos);
    holder.description.setText( Descriptions.get(pos));
    holder.cost.setText( Costs.get(pos));
    holder.profit.setText( Profits.get(pos));
    holder.date.setText( Dates.get(pos));
    holder.has_specifics.setText("Has Specifics");
  }

  @Override
  public int getItemCount() {
    return Descriptions.size(); //All arrays will be the same size. Could have been anyone
  }

  public class ViewHolder extends RecyclerView.ViewHolder{
    TextView description, cost, profit,date,has_specifics;
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.description = itemView.findViewById(R.id.text_errand_card_desc);
      this.cost = itemView.findViewById(R.id.text_errand_card_cost);
      this.profit = itemView.findViewById(R.id.text_errand_card_allowance);
      this.date = itemView.findViewById(R.id.text_errand_card_date);
      this.date = itemView.findViewById(R.id.text_errand_card_has_specifics);
      this.has_specifics = itemView.findViewById(R.id.text_errand_card_has_specifics);
    }
  }
}
