package pl.kalisz.uk.prup.passwordmanager.security;


import android.content.Context;
import android.content.SharedPreferences;

import pl.kalisz.uk.prup.passwordmanager.R;

public class MySecurityManager {
    public static void saveMainPassword(Context context, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.prefs_key_file), Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.prefs_key_password), encryptPass(password));
        editor.apply();
    }

    public static String getMainPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.prefs_key_file), Context.MODE_PRIVATE
        );
        String password = sharedPreferences.getString(context.getString(R.string.prefs_key_password), null);
        return decryptPass(password);
    }

    public static String encryptPass(String pass) {
        return CryptoUtils.encrypt(pass);
    }

    public static String decryptPass(String encryptedPass) {
        return CryptoUtils.decrypt(encryptedPass);
    }
}
