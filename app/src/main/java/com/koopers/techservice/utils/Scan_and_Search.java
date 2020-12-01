package com.koopers.techservice.utils;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.koopers.techservice.R;
import com.koopers.techservice.data.remote.RetrofitApi;
import com.koopers.techservice.data.remote.model.DeviceApiService;
import com.koopers.techservice.data.remote.model.DeviceResponse;
import com.koopers.techservice.ui.activities.Detail_Activity;
import com.koopers.techservice.utils.sharedPref.UserPrefManager;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Scan_and_Search {
    private Animation alphaAnimation;
    private Activity activity;
    private float pixelDensity;

    public Scan_and_Search(Activity activity){
        alphaAnimation = AnimationUtils.loadAnimation(activity, R.anim.alpha_anim);
        pixelDensity = activity.getResources().getDisplayMetrics().density;
        this.activity = activity;
    }


    @SuppressLint("SetTextI18n")
    public void ShowDialog(){
        final View dialogView = View.inflate(activity, R.layout.dialog_search,null);
        final Dialog dialog = new Dialog(activity, R.style.MyAlertDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);


        if (UserPrefManager.getmInstance(activity).isLoggedIn()) {
            ((TextView) dialog.findViewById(R.id.usernameTextView)).setText(activity.getResources().getString(R.string.greeting) + ", " +
                    UserPrefManager.getmInstance(activity).getUser().getUsuario());
        }

        FABProgressCircle fabProgressCircle = dialog.findViewById(R.id.fabProgressCircle);
        FloatingActionButton fab = dialog.findViewById(R.id.fab);
        TextView et_search = dialog.findViewById(R.id.et_search);
        fab.setOnClickListener(v -> {
            if (dialog.findViewById(R.id.fab).isShown()) {
                fabProgressCircle.show();
                Search(et_search.getText() + "");
            } else {
                dialog.findViewById(R.id.animationView).setVisibility(View.GONE);
            }
        });

        ((TextView) dialog.findViewById(R.id.title)).setText(activity.getResources().getString(R.string.search));

        dialog.findViewById(R.id.btn_close).setOnClickListener(v -> launchSearch( dialogView, false, dialog));

        dialog.setOnShowListener(dialogInterface -> launchSearch(dialogView, true, dialog));

        dialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_BACK){
                launchSearch(dialogView, false, dialog);
                return true;
            }
            return false;
        });

        Objects.requireNonNull(dialog.getWindow()).
                setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }


    private void launchSearch(View view, boolean flag, Dialog dialog) {
        int x = view.getRight();
        int y = view.getBottom();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));

        int hypotenuse = (int) Math.hypot(view.getWidth(), view.getHeight());

        if (flag) {
            Animator anim = ViewAnimationUtils.createCircularReveal( view, x, y, 0, hypotenuse);
            anim.setDuration(700);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    view.findViewById(R.id.dialog).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.dialog).startAnimation(alphaAnimation);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            view.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, hypotenuse, 0);
            anim.setDuration(700);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setVisibility(View.GONE);
                    view.findViewById(R.id.dialog).setVisibility(View.GONE);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            anim.start();
        }
    }

    public void Search(String Id){
        try {
            SearchEquipo(Integer.parseInt(Id));
        }
        catch(Exception e) {
            Toasty.info(activity, Id, Toasty.LENGTH_SHORT).show();
        }
    }

    private void SearchEquipo(int SearchId) {
        DeviceApiService deviceApiService =  RetrofitApi.getInstance().getRetrofit().create(DeviceApiService.class);
        Call<DeviceResponse> responseCall = deviceApiService.OneEquipo(SearchId);
        responseCall.enqueue(new Callback<DeviceResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeviceResponse> call, @NonNull Response<DeviceResponse> response) {
                //obtener del servidor
                if (response.body() != null) {
                    if (!response.body().getError()) {
                        Toasty.warning(activity, response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, Detail_Activity.class);
                        intent.putExtra("equipo", response.body().getContenido().get(0));
                        activity.startActivity(intent);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<DeviceResponse> call, @NonNull Throwable t) {
                //obtener localmente

            }
        });
    }

}
