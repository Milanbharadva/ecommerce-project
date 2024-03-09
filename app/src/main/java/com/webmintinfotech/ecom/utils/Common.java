package com.webmintinfotech.ecom.utils;

import static com.webmintinfotech.ecom.utils.SharePreference.getStringPref;
import static com.webmintinfotech.ecom.utils.SharePreference.setStringPref;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.ui.activity.ActMain;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Common {
    public static boolean isProfileEdit = false;
    public static boolean isProfileMainEdit = false;
    public static boolean isAddOrUpdated = false;

    // Toast messages
    public static void getToast(Activity activity, String strTxtToast) {
        Toast.makeText(activity, strTxtToast, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidAmount(String strPattern) {
        return Pattern.compile("^[0-9]+([.][0-9]{2})?$").matcher(strPattern).matches();
    }

    public static void getLog(String strKey, String strValue) {
        Log.e(">>>---  " + strKey + "  ---<<<", strValue);
    }

    public static boolean isValidEmail(String strPattern) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(strPattern).matches();
    }

    public static boolean isCheckNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void openActivity(Activity activity, Class<?> destinationClass) {
        activity.startActivity(new Intent(activity, destinationClass));
        activity.overridePendingTransition(R.anim.fad_in, R.anim.fad_out);
    }

    private static Dialog dialog = null;

    public static void dismissLoadingProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static void showLoadingProgress(Activity context) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dlg_progress);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void alertErrorOrValidationDialog(Activity act, String msg) {
         Dialog dialog = null;
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            dialog = new Dialog(act, R.style.AppCompatAlertDialogStyleBig);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            LayoutInflater mInflater = LayoutInflater.from(act);
            View mView = mInflater.inflate(R.layout.dlg_validation, null, false);
            TextView textDesc = mView.findViewById(R.id.tvMessage);
            textDesc.setText(msg);
            TextView tvOk = mView.findViewById(R.id.tvOk);
            Dialog finalDialog = dialog;
            tvOk.setOnClickListener(v -> finalDialog.dismiss());
            dialog.setContentView(mView);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showErrorFullMsg(Activity activity, String message) {
        TSnackbar snackbar = TSnackbar.make(activity.findViewById(android.R.id.content), message, TSnackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.RED);
        TextView textView = snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void showSuccessFullMsg(Activity activity, String message) {
        TSnackbar snackbar = TSnackbar.make(activity.findViewById(android.R.id.content), message, TSnackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(activity.getResources().getColor(R.color.light_green));
        TextView textView = snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void setLogout(Activity activity) {
        String getUserID = getStringPref(activity, SharePreference.userId);
        boolean isTutorialsActivity = SharePreference.getBooleanPref(activity, SharePreference.isTutorial);
        SharePreference preference = new SharePreference(activity);
        preference.mLogout();
        SharePreference.setBooleanPref(activity, SharePreference.isTutorial, isTutorialsActivity);
        if (getUserID != null) {
            setStringPref(activity, SharePreference.userId, "");
        }
        Intent intent = new Intent(activity, ActLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public static MultipartBody.Part setImageUpload(String strParameter, File mSelectedFileImg) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), mSelectedFileImg);
        return MultipartBody.Part.createFormData(strParameter, mSelectedFileImg.getName(), requestBody);
    }

    public static RequestBody setRequestBody(String bodyData) {
        return RequestBody.create(bodyData, MediaType.parse("text/plain"));
    }

    public static void closeKeyBoard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            try {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void getCurrentLanguage(Activity context, boolean isChangeLanguage) {
        if (getStringPref(context, SharePreference.SELECTED_LANGUAGE) == null || getStringPref(context, SharePreference.SELECTED_LANGUAGE).equalsIgnoreCase("")) {
            setStringPref(context, SharePreference.SELECTED_LANGUAGE, context.getResources().getString(R.string.language_english));
        }
        Locale locale = getStringPref(context, SharePreference.SELECTED_LANGUAGE).equalsIgnoreCase(context.getResources().getString(R.string.language_english)) ? new Locale("en-us") : new Locale("ar");

        //start
        android.content.res.Resources activityRes = context.getResources();
        android.content.res.Configuration activityConf = activityRes.getConfiguration();
        if (getStringPref(context, SharePreference.SELECTED_LANGUAGE).equalsIgnoreCase(context.getResources().getString(R.string.language_english))) {
            activityConf.setLocale(new Locale("en-us")); // API 17+ only.
        } else {
            activityConf.setLocale(new Locale("ar"));
        }
        activityRes.updateConfiguration(activityConf, activityRes.getDisplayMetrics());
        android.content.res.Resources applicationRes = context.getApplicationContext().getResources();
        android.content.res.Configuration applicationConf = applicationRes.getConfiguration();
        applicationConf.setLocale(locale);
        applicationRes.updateConfiguration(applicationConf, applicationRes.getDisplayMetrics());

        if (isChangeLanguage) {
            Intent intent = new Intent(context, ActMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            context.finish();
            context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public static String getDate(String strDate) {
        try {
            SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            Date dateObj = curFormater.parse(strDate);
            SimpleDateFormat postFormater = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            return postFormater.format(dateObj);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDateTime(String strDate) {
        try {
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Date dateObj = curFormater.parse(strDate);
            SimpleDateFormat postFormater = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.US);
            return postFormater.format(dateObj);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getPrice(String currencyPos, String currency, String price) {
        return currencyPos.equals("left") ? currency + String.format(Locale.US, "%,.02f", Double.parseDouble(price)) :
                String.format(Locale.US, "%,.02f", Double.parseDouble(price)) + currency;
    }
}
