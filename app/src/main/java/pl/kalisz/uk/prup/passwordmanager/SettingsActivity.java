package pl.kalisz.uk.prup.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

import pl.kalisz.uk.prup.passwordmanager.security.MySecurityManager;

public class SettingsActivity extends AppCompatActivity {
    Button btn_generate, btn_save, btn_logout;
    EditText et_pass_current, et_pass_new, et_pass_repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        et_pass_current = findViewById(R.id.et_pass_current);
        et_pass_new = findViewById(R.id.et_pass_new);
        et_pass_repeat = findViewById(R.id.et_pass_repeat);

        btn_generate = findViewById(R.id.btn_generate);
        btn_save = findViewById(R.id.btn_save);
        btn_logout = findViewById(R.id.btn_logout);

        btn_generate.setOnClickListener(v -> {
            generatePass();
        });
        btn_save.setOnClickListener(v -> {
            savePass();
        });
        btn_logout.setOnClickListener(v -> {
            logout();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void generatePass() {
        Random random = new Random();
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*";
        int rand_len = random.nextInt(4) + 12;
        String pass_strong = "";

        for(int i = 0; i < rand_len; i++) {
            int rand_char = random.nextInt(chars.length());
            pass_strong += chars.charAt(rand_char);
        }

        et_pass_new.setText(pass_strong);
    }

    private void savePass() {
        String pass_current = et_pass_current.getText().toString();
        String pass_new = et_pass_new.getText().toString();
        String pass_repeat = et_pass_repeat.getText().toString();

        // czy obecne hasło się zgadza
        String savedPassword = MySecurityManager.getMainPassword(SettingsActivity.this);

        if(!pass_current.equals(savedPassword)) {
            Toast.makeText(SettingsActivity.this, "Obecne hasło jest niepoprawne!", Toast.LENGTH_LONG).show();
            return;
        }

        // czy hasło min. 8znaków
        if(pass_new.length() < 8) {
            Toast.makeText(SettingsActivity.this, "Nowe hasło musi mieć min. 8znaków!", Toast.LENGTH_LONG).show();
            return;
        }

        // czy hasła takie same
        if(!pass_new.equals(pass_repeat)) {
            Toast.makeText(SettingsActivity.this, "Powtórzone hasło się nie zgadza!", Toast.LENGTH_LONG).show();
            return;
        }

        // success
        MySecurityManager.saveMainPassword(SettingsActivity.this, pass_new);
        Toast.makeText(SettingsActivity.this, "Hasło główne zostało zmienione!", Toast.LENGTH_LONG).show();
        logout();
    }

    private void logout() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}