package com.webmintinfotech.ecom.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseFragment;
import com.webmintinfotech.ecom.databinding.FragAccountBinding;
import com.webmintinfotech.ecom.model.ContactInfo;
import com.webmintinfotech.ecom.model.GetProfileResponse;
import com.webmintinfotech.ecom.model.UserData;
import com.webmintinfotech.ecom.ui.activity.ActEditProfile;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountFragment extends BaseFragment<FragAccountBinding> {

    private FragAccountBinding accountBinding;
    private UserData dataResponse;
    private ContactInfo contactData = new ContactInfo();
    private String notificationStatus = "";
    private String referralCode = "";
    private String userid = "";

    @SuppressLint("SetTextI18n")
    @Override
    public void initView(View view) {
        accountBinding = FragAccountBinding.bind(view);
        userid = SharePreference.getStringPref(requireActivity(), SharePreference.userId);
        Log.d("UserID", userid);

        accountBinding.ivEditprofile.setOnClickListener(v -> {
            if (SharePreference.getBooleanPref(requireActivity(), SharePreference.isLogin)) {
                onActivityResult.launch(new Intent(requireActivity(), ActEditProfile.class));
            } else {
                openActivity(ActLogin.class);
                requireActivity().finish();
                requireActivity().finishAffinity();
            }
        });

        // ... (Other UI initialization)

        accountBinding.ivInstagram.setOnClickListener(v -> openWebPage(contactData.getInstagram()));
        accountBinding.ivLinkedIn.setOnClickListener(v -> openWebPage(contactData.getLinkedin()));
        accountBinding.ivFaceBook.setOnClickListener(v -> openWebPage(contactData.getFacebook()));
        accountBinding.ivTwitter.setOnClickListener(v -> openWebPage(contactData.getTwitter()));
    }

    @Override
    public FragAccountBinding getBinding() {
        accountBinding = FragAccountBinding.inflate(getLayoutInflater());
        return accountBinding;
    }

    // ... (Other methods)

    private void alertLogOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.log_out);
        builder.setMessage(R.string.logout_text);
        builder.setPositiveButton("Yes", (dialogInterface,var) -> {
            dialogInterface.dismiss();
            Common.setLogout(requireActivity());
        });
        builder.setNegativeButton("No", (dialogInterface, var) -> dialogInterface.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void callApiProfile(HashMap<String, String> hasmap) {
        Common.showLoadingProgress(requireActivity());
        Call<GetProfileResponse> call = ApiClient.getClient().getProfile(hasmap);
        call.enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {

            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {

            }
            // ... (Other methods)
        });
    }

    private void setProfileData() {
        if (SharePreference.getBooleanPref(requireActivity(), SharePreference.isLogin)) {
            accountBinding.tvUsername.setText(SharePreference.getStringPref(requireActivity(), SharePreference.userName));
            accountBinding.tvEmail.setText(SharePreference.getStringPref(requireActivity(), SharePreference.userEmail));
            Glide.with(requireActivity())
                    .load(SharePreference.getStringPref(requireActivity(), SharePreference.userProfile))
                    .placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.profile, null))
                    .into(accountBinding.ivProfile);
        } else {
            accountBinding.ivProfile.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_elogo, null));
        }
    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    // ... (Other methods)

    private final ActivityResultLauncher<Intent> onActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                    if (Common.isCheckNetwork(requireActivity())) {
                        HashMap<String, String> hasmap = new HashMap<>();
                        hasmap.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));
                        callApiProfile(hasmap);
                    } else {
                        Common.alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
                    }
                }
            }
    );

    @Override
    public void onResume() {
        super.onResume();
        Common.getCurrentLanguage(requireActivity(), false);
        if (Common.isCheckNetwork(requireActivity())) {
            if (!userid.isEmpty()) {
                HashMap<String, String> hasmap = new HashMap<>();
                hasmap.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));
                callApiProfile(hasmap);
            }
        } else {
            Common.alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
        }
    }
}