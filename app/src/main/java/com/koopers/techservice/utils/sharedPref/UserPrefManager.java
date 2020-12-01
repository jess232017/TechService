package com.koopers.techservice.utils.sharedPref;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.koopers.techservice.data.local.entity.Usuario;


public class UserPrefManager {

    private static final String SHARED_PREF_NAME = "user-session";
    private static final String KEY_ID = "keyid";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_MAIL = "keymail";
    private static final String KEY_PASSWORD = "keypassword";
    private static final String KEY_IMAGE = "keyimage";
    private static final String KEY_ROLE = "keyrole";

    @SuppressLint("StaticFieldLeak")
    private static UserPrefManager mInstance;
    private Context mCtx;

    private UserPrefManager(Context context){
        mCtx = context;
    }

    public static synchronized UserPrefManager getmInstance(Context context){
        if (mInstance == null){
            mInstance = new UserPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(Usuario user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsuario());
        editor.putString(KEY_MAIL, user.getMail());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_IMAGE, user.getImage());
        editor.putString(KEY_ROLE, user.getRole());
        editor.apply();
    }

    //este método verificará si el usuario ya ha iniciado sesión o no
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //este método le dará los datos del usuario conectado
    public Usuario getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Usuario(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_MAIL, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_IMAGE, null),
                sharedPreferences.getString(KEY_ROLE, null)
        );
    }

    //este método desconectará al usuario
    public void logOut(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        //mCtx.startActivity(new Intent(mCtx, Login_Activity.class));
    }
}
