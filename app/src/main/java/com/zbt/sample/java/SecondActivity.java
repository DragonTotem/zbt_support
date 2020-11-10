package com.zbt.sample.java;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.zbt.sample.R;

import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Author       :zbt
 * Date         :2020/10/27 下午3:04
 * Version      :1.0.0
 * Description  :测试Activity
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @TestOnly
    public void testEncryptedSharedPreferences() {

        try {

            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // use the shared preferences and editor as you normally would
            SharedPreferences.Editor editor = sharedPreferences.edit();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
