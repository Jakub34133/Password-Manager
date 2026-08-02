package pl.kalisz.uk.prup.passwordmanager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pl.kalisz.uk.prup.passwordmanager.database.DatabaseHelper;

public class AccountManageActivity extends AppCompatActivity {
    public static final String EXTRA_ACCOUNT_ID = "extra_account_id";
    private int account_id;
    private AccountModel accountModel;
    DatabaseHelper databaseHelper;
    EditText et_platform, et_login, et_password;
    Button btn_edit, btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_manage);

        // get city
        account_id = getIntent().getIntExtra(EXTRA_ACCOUNT_ID, -1);

        // Set database
        databaseHelper = new DatabaseHelper(this);
        et_platform = findViewById(R.id.et_platform);
        et_login = findViewById(R.id.et_login);
        et_password = findViewById(R.id.et_password);

        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);

        btn_edit.setOnClickListener(v -> {
            editCity();
        });
        btn_delete.setOnClickListener(v -> {
            deleteCity();
        });

        //load city
        accountModel = loadAccount();
        if(accountModel != null) {
            et_platform.setText(accountModel.getPlatform());
            et_login.setText(accountModel.getLogin());
            et_password.setText(accountModel.getPassword());
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private AccountModel loadAccount() {
        try {
            return databaseHelper.getAccountById(account_id);
        } catch(Exception e) {
            Toast.makeText(AccountManageActivity.this, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void switchPassVisibility() {
        Log.d("a", String.valueOf(et_password.getInputType()));
        Log.d("a", String.valueOf(et_login.getInputType()));

    }

    private void editCity() {
        try {
            String platform = et_platform.getText().toString();
            String login = et_login.getText().toString();
            String password = et_password.getText().toString();

            AccountModel accountModel = new AccountModel(account_id, platform, login, password);
            boolean result = databaseHelper.updateAccount(accountModel);

            if (result) {
                Toast.makeText(AccountManageActivity.this, "Edytowano konto: " + login, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(AccountManageActivity.this, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            Toast.makeText(AccountManageActivity.this, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteCity() {
        try {
            AccountModel accountModel = databaseHelper.getAccountById(account_id);
            boolean result = databaseHelper.deleteAccount(accountModel);

            if(result) {
                Toast.makeText(AccountManageActivity.this, "Usunięto konto: " + accountModel.getLogin(), Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(AccountManageActivity.this, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            Toast.makeText(AccountManageActivity.this, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
        }
    }
}