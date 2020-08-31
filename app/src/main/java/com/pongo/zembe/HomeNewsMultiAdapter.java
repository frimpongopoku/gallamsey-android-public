package com.pongo.zembe;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeNewsMultiAdapter extends RecyclerView.Adapter {

  int IMAGE_CODE = 6;
  int TEXT_CODE = 2;
  String TAG = "Adapter has started...";
  ArrayList<GenericErrandClass> news = new ArrayList<>();
  Context context;
  OnNewsItemClick listener;
  GroundUser authenticatedUser;

  public HomeNewsMultiAdapter(Context context, ArrayList<GenericErrandClass> news, OnNewsItemClick listener) {
    this.listener = listener;
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
      return new TextViewHolder(view, listener);
    } else if (viewType == IMAGE_CODE) {
      view = inflater.inflate(R.layout.errand_image_card, parent, false);
      return new ImageViewHolder(view, listener);
    }

    return null;
  }

  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }

  public void setNews(ArrayList<GenericErrandClass> news) {
    this.news = news;
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
    } else if(item.getErrandType().equals(Konstants.IMAGE_ERRAND)) {
      setImageContent(holder, position);
    }

  }

  public void setTextContent(@NonNull RecyclerView.ViewHolder _holder, int pos) {
    TextViewHolder holder = (TextViewHolder) _holder;
    GenericErrandClass newsItem = news.get(pos);
    SimpleUser user = newsItem.getCreator();
    holder.container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_recycler));
    holder.description.setText(newsItem.getDescription());
    holder.cost.setText(String.valueOf(newsItem.getCost()));
    holder.profit.setText(String.valueOf(newsItem.getAllowance()));
    holder.date.setText(DateHelper.getTimeAgo(newsItem.getCreatedAt()));
    if (user != null && !user.getProfilePicture().equals(Konstants.INIT_STRING)) {
      Picasso.get().load(user.getProfilePicture()).into(holder.creatorPic);
    } else {
      //user does not have a custom profile, so use their gender to give a default one
      if (user != null && !user.getGender().equals(Konstants.INIT_STRING) && user.getGender().equals(Konstants.MALE)) {
        holder.creatorPic.setImageResource(R.drawable.african_avatar_male);
      } else if (user != null && !user.getGender().equals(Konstants.INIT_STRING) && user.getGender().equals(Konstants.FEMALE)) {
        holder.creatorPic.setImageResource(R.drawable.african_avatar_female);
      } else {
        holder.creatorPic.setImageResource(R.drawable.profile_dummy_box_other);
      }

    }
  }

  public void setImageContent(@NonNull RecyclerView.ViewHolder _holder, int pos) {
    ImageViewHolder holder = (ImageViewHolder) _holder;
    GenericErrandClass newsItem = news.get(pos);
    SimpleUser user = newsItem.getCreator();
    holder.container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_recycler));
    holder.title.setText(newsItem.getDescription());
    holder.cost.setText(String.valueOf(newsItem.getCost()));
    holder.profit.setText(String.valueOf(newsItem.getAllowance()));
    holder.date.setText(DateHelper.getTimeAgo(newsItem.getCreatedAt()));
    if (newsItem.getImages() != null && newsItem.getImages().size() != 0) {
      Picasso.get().load(newsItem.getImages().get(0)).into(holder.image);
    }
    if (!user.getProfilePicture().equals(Konstants.INIT_STRING)) {
      Picasso.get().load(user.getProfilePicture()).into(holder.creatorPic);
    } else {
      //user does not have a custom profile, so use their gender to give a default one
      if (!user.getGender().equals(Konstants.INIT_STRING) && user.getGender().equals(Konstants.MALE)) {
        holder.creatorPic.setImageResource(R.drawable.african_avatar_male);
      } else if (!user.getGender().equals(Konstants.INIT_STRING) && user.getGender().equals(Konstants.FEMALE)) {
        holder.creatorPic.setImageResource(R.drawable.african_avatar_female);
      } else {
        holder.creatorPic.setImageResource(R.drawable.profile_dummy_box_other);
      }

    }

  }

  @Override
  public int getItemCount() {
    return this.news.size();
  }


  //........ TextCard
  class TextViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    TextView description, cost, profit, date, has_specifics;
    LinearLayout container;
    OnNewsItemClick listener;
    GalInterfaceGuru.EditContextMenuItemListener editMenuItemListener;
    CircleImageView creatorPic;
    GalInterfaceGuru.MessageCreatorContextMenuItemListener messengerListener;


    public TextViewHolder(@NonNull final View itemView, final OnNewsItemClick listener) {
      super(itemView);
      this.description = itemView.findViewById(R.id.text_errand_card_desc);
      this.cost = itemView.findViewById(R.id.text_errand_card_cost);
      this.profit = itemView.findViewById(R.id.text_errand_card_allowance);
      this.date = itemView.findViewById(R.id.text_errand_card_date);
      this.container = itemView.findViewById(R.id.container);
      this.listener = listener;
      this.creatorPic = itemView.findViewById(R.id.creator_pic);
      this.editMenuItemListener = (GalInterfaceGuru.EditContextMenuItemListener) listener;
      this.messengerListener = (GalInterfaceGuru.MessageCreatorContextMenuItemListener) listener;
      itemView.setOnCreateContextMenuListener(this);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          GenericErrandClass errand = news.get(getAdapterPosition()); //news arr from main class
          listener.newsItemCallback(getAdapterPosition(), errand);
        }
      });
    }


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
      GenericErrandClass errand = news.get(getAdapterPosition());
      SimpleUser creator = errand != null ? errand.getCreator() : null;
      //------------------MORE MENU ITEM ------------------------------------
      MenuItem more = contextMenu.add("More");
      more.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
          GenericErrandClass errand = news.get(getAdapterPosition()); //news arr from main class
          listener.newsItemCallback(getAdapterPosition(), errand);
          return true;
        }
      });
      if (authenticatedUser != null && authenticatedUser.getUserDocumentID().equals(errand.getCreator().getUserPlatformID())) {
       // ------------------------ EDIT MENU ITEM -----------------------------
        MenuItem edit = contextMenu.add("Edit");
        edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            GenericErrandClass errand = news.get(getAdapterPosition()); //news arr from main class
            editMenuItemListener.getErrandToBeEdited(getAdapterPosition(), errand);
            return true;
          }
        });
        //--------------------------DELETE MENU ITEM -------------------------
        MenuItem delete = contextMenu.add("Delete & Refund");
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            return false;
          }
        });
      } else {
        //---------------- MESSAGE MENU ITEM -----------------------------
        String userName = creator != null ? creator.getUserName() : "";
        MenuItem message = contextMenu.add("Message " + userName);
        message.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            GenericErrandClass errand = news.get(getAdapterPosition());
            messengerListener.talkToCreatorAboutErrand(getAdapterPosition(),errand);
            return true;
          }
        });
        //--------------------------- REPORT MENU ITEM -----------------------
        MenuItem report = contextMenu.add("Report");
        report.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            return false;
          }
        });
      }

    }


  }


  //......... ImageCard
  class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    TextView title, cost, profit, date, has_specifics;
    ImageView image;
    LinearLayout container;
    OnNewsItemClick listener;
    GalInterfaceGuru.EditContextMenuItemListener editMenuItemListener;
    CircleImageView creatorPic;
    GalInterfaceGuru.MessageCreatorContextMenuItemListener messengerListener;

    public ImageViewHolder(@NonNull View itemView, final OnNewsItemClick listener) {
      super(itemView);
      this.title = itemView.findViewById(R.id.img_errand_card_title);
      this.cost = itemView.findViewById(R.id.img_errand_card_cost);
      this.profit = itemView.findViewById(R.id.img_errand_card_allowance);
      this.date = itemView.findViewById(R.id.img_errand_card_date);
      this.image = itemView.findViewById(R.id.img_errand_card_image);
      this.container = itemView.findViewById(R.id.container);
      this.listener = listener;
      this.creatorPic = itemView.findViewById(R.id.creator_pic);
      this.editMenuItemListener = (GalInterfaceGuru.EditContextMenuItemListener) listener; // THe activity listener can be cast into this interface because it is implementing it as well
      this.messengerListener = (GalInterfaceGuru.MessageCreatorContextMenuItemListener) listener;
      itemView.setOnCreateContextMenuListener(this);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          GenericErrandClass errand = news.get(getAdapterPosition());
          listener.newsItemCallback(getAdapterPosition(), errand);
        }
      });
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
      GenericErrandClass errand = news.get(getAdapterPosition());
      SimpleUser creator = errand != null ? errand.getCreator() : null;
      //--------------------MORE MENU ITEM -----------------------------------
      MenuItem more = contextMenu.add("More");
      more.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
          GenericErrandClass errand = news.get(getAdapterPosition()); //news arr from main class
          listener.newsItemCallback(getAdapterPosition(), errand);
          return true;
        }
      });

      if (authenticatedUser != null && authenticatedUser.getUserDocumentID().equals(errand.getCreator().getUserPlatformID())) {
        //--------------------- EDIT MENU ITEM ------------------------------
        MenuItem edit = contextMenu.add("Edit");
        edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            GenericErrandClass errand = news.get(getAdapterPosition()); //news arr from main class
            editMenuItemListener.getErrandToBeEdited(getAdapterPosition(), errand);
            return true;
          }
        });
        // -------------------- DELETE MENU ITEM ----------------------------
        MenuItem delete = contextMenu.add("Delete & Refund");
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            GenericErrandClass errand = news.get(getAdapterPosition());
            messengerListener.talkToCreatorAboutErrand(getAdapterPosition(),errand);
            return true;
          }
        });

      } else {
        //---------------- MESSAGE MENU ITEM -----------------------------
        String userName = creator != null ? creator.getUserName() : "";
        MenuItem message = contextMenu.add("Message " + userName);
        message.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            GenericErrandClass errand = news.get(getAdapterPosition());
            messengerListener.talkToCreatorAboutErrand(getAdapterPosition(),errand);
            return true;
          }
        });
        //-------------------- REPORT MENU ITEM ----------------------------
        MenuItem report = contextMenu.add("Report");
        report.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            return false;
          }
        });
      }
    }
  }

  public interface OnNewsItemClick {
    void newsItemCallback(int pos, GenericErrandClass selectedErrand);
  }


}
