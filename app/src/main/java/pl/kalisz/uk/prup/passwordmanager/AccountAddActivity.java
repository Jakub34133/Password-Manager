package pl.kalisz.uk.prup.passwordmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pl.kalisz.uk.prup.passwordmanager.database.DatabaseHelper;

public class AccountAddActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    EditText et_platform, et_login, et_password;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_add);

        // Set database
        databaseHelper = new DatabaseHelper(this);
        et_platform = findViewById(R.id.et_platform);
        et_login = findViewById(R.id.et_login);
        et_password = findViewById(R.id.et_password);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(v -> {
            addAccount();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addAccount() {
        try {
            String platform = et_platform.getText().toString();
            String login = et_login.getText().toString();
            String password = et_password.getText().toString();

            AccountModel accountModel = new AccountModel(platform, login, password);
            boolean result = databaseHelper.addAccount(accountModel);

            if (result) {
                Toast.makeText(AccountAddActivity.this, "Dodano12 konto: " + login, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(AccountAddActivity.this, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            Toast.makeText(AccountAddActivity.this, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
        }

    }
}