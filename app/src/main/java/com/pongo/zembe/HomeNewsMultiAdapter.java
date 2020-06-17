package com.pongo.zembe;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeNewsMultiAdapter extends RecyclerView.Adapter {

  int IMAGE_CODE = 6;
  int TEXT_CODE = 2;
  String TAG = "Adapter has started...";
  ArrayList<String> Descriptions = new ArrayList<>();
  ArrayList<String> Profits = new ArrayList<>();
  ArrayList<String> Costs = new ArrayList<>();
  ArrayList<String> Dates = new ArrayList<>();
  Context Context;

  public HomeNewsMultiAdapter(Context context, ArrayList<String> descriptions, ArrayList<String> profits, ArrayList<String> costs, ArrayList<String> dates) {
    Descriptions = descriptions;
    Profits = profits;
    Costs = costs;
    Dates = dates;
    this.Context = context;

  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view;
    if (viewType == TEXT_CODE) {
      view = inflater.inflate(R.layout.text_errand_card, parent, false);
      return new TextViewHolder(view);
    } else if (viewType == IMAGE_CODE) {
      view = inflater.inflate(R.layout.errand_image_card, parent, false);
      return new ImageViewHolder(view);
    }

    return null;
  }

  @Override
  public int getItemViewType(int position) {
    if (position % 2 == 0) {
      return TEXT_CODE;
    }
    return IMAGE_CODE;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    if (position % 2 == 0) {
      setTextContent(holder, position);
    } else {
      setImageContent(holder, position);
    }

  }


  public void setTextContent(@NonNull RecyclerView.ViewHolder _holder, int pos) {
    TextViewHolder holder = (TextViewHolder) _holder;
    holder.description.setText(Descriptions.get(pos));
    holder.cost.setText(Costs.get(pos));
    holder.profit.setText(Profits.get(pos));
    holder.date.setText(Dates.get(pos));
    holder.has_specifics.setText("Has Specifics");

  }

  public void setImageContent(@NonNull RecyclerView.ViewHolder _holder, int pos) {
    ImageViewHolder holder = (ImageViewHolder) _holder;
    holder.title.setText(Descriptions.get(pos));
    holder.cost.setText(Costs.get(pos));
    holder.profit.setText(Profits.get(pos));
    holder.date.setText(Dates.get(pos));
    holder.has_specifics.setText("Has Specifics");
    holder.image.setImageResource(R.drawable.shoe50);

  }

  @Override
  public int getItemCount() {
    return this.Descriptions.size();
  }



  //........ TextCard
  class TextViewHolder extends RecyclerView.ViewHolder  {
    TextView description, cost, profit, date, has_specifics;

    public TextViewHolder(@NonNull View itemView) {
      super(itemView);
      this.description = itemView.findViewById(R.id.text_errand_card_desc);
      this.cost = itemView.findViewById(R.id.text_errand_card_cost);
      this.profit = itemView.findViewById(R.id.text_errand_card_allowance);
      this.date = itemView.findViewById(R.id.text_errand_card_has_specifics);
      this.has_specifics = itemView.findViewById(R.id.text_errand_card_has_specifics);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent errandViewPage = new Intent(view.getContext(),ErrandViewActivity.class);
          view.getContext().startActivity(errandViewPage);
        }
      });

    }

  }


  //......... ImageCard
  class ImageViewHolder extends RecyclerView.ViewHolder {
    TextView title, cost, profit, date, has_specifics;
    ImageView image;

    public ImageViewHolder(@NonNull View itemView) {
      super(itemView);
      this.title = itemView.findViewById(R.id.img_errand_card_title);
      this.cost = itemView.findViewById(R.id.img_errand_card_cost);
      this.profit = itemView.findViewById(R.id.img_errand_card_allowance);
      this.date = itemView.findViewById(R.id.img_errand_card_date);
      this.has_specifics = itemView.findViewById(R.id.img_errand_card_has_specifics);
      this.image = itemView.findViewById(R.id.img_errand_card_image);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent imageErrandPage = new Intent(view.getContext(),ImageErrandView.class);
          view.getContext().startActivity(imageErrandPage);
        }
      });
    }
  }
}
