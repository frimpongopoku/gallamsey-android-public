package com.pongo.zembe;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeNewsMultiAdapter extends RecyclerView.Adapter {

  int IMAGE_CODE = 6;
  int TEXT_CODE = 2;
  String TAG = "Adapter has started...";

  ArrayList<GenericErrandClass> news = new ArrayList<>();
  Context context;

  public HomeNewsMultiAdapter(Context context, ArrayList<GenericErrandClass> news) {

    this.context = context;
    this.news = news;

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
    GenericErrandClass newsItem = news.get(position);
    if (newsItem.getErrandType().equals(Konstants.IMAGE_ERRAND)) {
      return IMAGE_CODE;
    }
    return TEXT_CODE;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    GenericErrandClass item = news.get(position);
    if (item.getErrandType().equals(Konstants.TEXT_ERRAND)) {
      setTextContent(holder, position);
    } else {
      setImageContent(holder, position);
    }

  }


  public void setTextContent(@NonNull RecyclerView.ViewHolder _holder, int pos) {
    TextViewHolder holder = (TextViewHolder) _holder;
    GenericErrandClass newsItem = news.get(pos);
    holder.container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_recycler));
    holder.description.setText(newsItem.getDescription());
    holder.cost.setText(String.valueOf(newsItem.getCost()));
    holder.profit.setText(String.valueOf(newsItem.getAllowance()));
    holder.date.setText(newsItem.getCreatedAt());
  }

  public void setImageContent(@NonNull RecyclerView.ViewHolder _holder, int pos) {
    ImageViewHolder holder = (ImageViewHolder) _holder;
    GenericErrandClass newsItem = news.get(pos);
    holder.container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_recycler));
    holder.title.setText(newsItem.getDescription());
    holder.cost.setText(String.valueOf(newsItem.getCost()));
    holder.profit.setText(String.valueOf(newsItem.getAllowance()));
    holder.date.setText(newsItem.getCreatedAt());
    Picasso.get().load(newsItem.getImages().get(0)).into(holder.image);
  }

  @Override
  public int getItemCount() {
    return this.news.size();
  }


  //........ TextCard
  class TextViewHolder extends RecyclerView.ViewHolder {
    TextView description, cost, profit, date, has_specifics;
    LinearLayout container;

    public TextViewHolder(@NonNull View itemView) {
      super(itemView);
      this.description = itemView.findViewById(R.id.text_errand_card_desc);
      this.cost = itemView.findViewById(R.id.text_errand_card_cost);
      this.profit = itemView.findViewById(R.id.text_errand_card_allowance);
      this.date = itemView.findViewById(R.id.text_errand_card_date);
      this.container = itemView.findViewById(R.id.container);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent errandViewPage = new Intent(view.getContext(), ErrandViewActivity.class);
          view.getContext().startActivity(errandViewPage);
        }
      });

    }

  }


  //......... ImageCard
  class ImageViewHolder extends RecyclerView.ViewHolder {
    TextView title, cost, profit, date, has_specifics;
    ImageView image;
    LinearLayout container;

    public ImageViewHolder(@NonNull View itemView) {
      super(itemView);
      this.title = itemView.findViewById(R.id.img_errand_card_title);
      this.cost = itemView.findViewById(R.id.img_errand_card_cost);
      this.profit = itemView.findViewById(R.id.img_errand_card_allowance);
      this.date = itemView.findViewById(R.id.img_errand_card_date);
      this.image = itemView.findViewById(R.id.img_errand_card_image);
      this.container = itemView.findViewById(R.id.container);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent imageErrandPage = new Intent(view.getContext(), ImageErrandView.class);
          view.getContext().startActivity(imageErrandPage);
        }
      });
    }
  }
}
