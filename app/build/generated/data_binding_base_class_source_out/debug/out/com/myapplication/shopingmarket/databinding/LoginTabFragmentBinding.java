// Generated by view binder compiler. Do not edit!
package com.myapplication.shopingmarket.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.myapplication.shopingmarket.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LoginTabFragmentBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView forgetPass;

  @NonNull
  public final Button loginBtn;

  @NonNull
  public final EditText mailField;

  @NonNull
  public final TextInputEditText pass;

  @NonNull
  public final TextInputLayout passBtn;

  private LoginTabFragmentBinding(@NonNull ConstraintLayout rootView, @NonNull TextView forgetPass,
      @NonNull Button loginBtn, @NonNull EditText mailField, @NonNull TextInputEditText pass,
      @NonNull TextInputLayout passBtn) {
    this.rootView = rootView;
    this.forgetPass = forgetPass;
    this.loginBtn = loginBtn;
    this.mailField = mailField;
    this.pass = pass;
    this.passBtn = passBtn;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LoginTabFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LoginTabFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.login_tab_fragment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LoginTabFragmentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.forget_Pass;
      TextView forgetPass = ViewBindings.findChildViewById(rootView, id);
      if (forgetPass == null) {
        break missingId;
      }

      id = R.id.login_btn;
      Button loginBtn = ViewBindings.findChildViewById(rootView, id);
      if (loginBtn == null) {
        break missingId;
      }

      id = R.id.mail_field;
      EditText mailField = ViewBindings.findChildViewById(rootView, id);
      if (mailField == null) {
        break missingId;
      }

      id = R.id.pass;
      TextInputEditText pass = ViewBindings.findChildViewById(rootView, id);
      if (pass == null) {
        break missingId;
      }

      id = R.id.pass_btn;
      TextInputLayout passBtn = ViewBindings.findChildViewById(rootView, id);
      if (passBtn == null) {
        break missingId;
      }

      return new LoginTabFragmentBinding((ConstraintLayout) rootView, forgetPass, loginBtn,
          mailField, pass, passBtn);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}