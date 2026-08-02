package pl.kalisz.uk.prup.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import pl.kalisz.uk.prup.passwordmanager.database.DatabaseHelper;

public class AccountListActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    FloatingActionButton fab_add;
    Button btn_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_list);

        // Set database
        databaseHelper = new DatabaseHelper(this);

        btn_settings = findViewById(R.id.btn_settings);
        fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(v -> {
            Intent intent = new Intent(AccountListActivity.this, AccountAddActivity.class);
            startActivity(intent);
        });

        btn_settings.setOnClickListener(v -> {
            Intent intent = new Intent(AccountListActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // load data
        loadAccounts();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        loadAccounts();
    }

    public void loadAccounts() {
        RecyclerView accounts_recycler_view;
        AccountAdapter accounts_adapter;

        // 1. get data
        List<AccountModel> accounts = databaseHelper.getAccounts();

        // 2. find RecyclerView
        accounts_recycler_view = findViewById(R.id.rv_accounts);

        // 3. Set Layout Manager
        accounts_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        // 4. Set adapter
        accounts_adapter = new AccountAdapter(accounts, AccountListActivity.this);
        accounts_recycler_view.setAdapter(accounts_adapter);
    }
}