// Generated by view binder compiler. Do not edit!
package com.myapplication.shopingmarket.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.myapplication.shopingmarket.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class HomeItemRecycleBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final CardView homeItem;

  @NonNull
  public final TextView itemDetails;

  @NonNull
  public final ImageView itemImage;

  @NonNull
  public final TextView itemPrice;

  @NonNull
  public final TextView itemTitle;

  @NonNull
  public final RatingBar ratingProduct;

  private HomeItemRecycleBinding(@NonNull CardView rootView, @NonNull CardView homeItem,
      @NonNull TextView itemDetails, @NonNull ImageView itemImage, @NonNull TextView itemPrice,
      @NonNull TextView itemTitle, @NonNull RatingBar ratingProduct) {
    this.rootView = rootView;
    this.homeItem = homeItem;
    this.itemDetails = itemDetails;
    this.itemImage = itemImage;
    this.itemPrice = itemPrice;
    this.itemTitle = itemTitle;
    this.ratingProduct = ratingProduct;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static HomeItemRecycleBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static HomeItemRecycleBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.home_item_recycle, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static HomeItemRecycleBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      CardView homeItem = (CardView) rootView;

      id = R.id.item_details;
      TextView itemDetails = ViewBindings.findChildViewById(rootView, id);
      if (itemDetails == null) {
        break missingId;
      }

      id = R.id.item_image;
      ImageView itemImage = ViewBindings.findChildViewById(rootView, id);
      if (itemImage == null) {
        break missingId;
      }

      id = R.id.item_price;
      TextView itemPrice = ViewBindings.findChildViewById(rootView, id);
      if (itemPrice == null) {
        break missingId;
      }

      id = R.id.item_title;
      TextView itemTitle = ViewBindings.findChildViewById(rootView, id);
      if (itemTitle == null) {
        break missingId;
      }

      id = R.id.rating_product;
      RatingBar ratingProduct = ViewBindings.findChildViewById(rootView, id);
      if (ratingProduct == null) {
        break missingId;
      }

      return new HomeItemRecycleBinding((CardView) rootView, homeItem, itemDetails, itemImage,
          itemPrice, itemTitle, ratingProduct);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
