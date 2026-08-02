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

import pl.kalisz.uk.prup.passwordmanager.security.MySecurityManager;

public class MainActivity extends AppCompatActivity {
    EditText et_pass;
    Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        et_pass = findViewById(R.id.et_pass);
        btn_confirm = findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(v -> {
            authorize();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void authorize() {
        String pass = et_pass.getText().toString();

        String savedPassword = MySecurityManager.getMainPassword(MainActivity.this);
        if(pass.equals(savedPassword)) {
            Toast.makeText(MainActivity.this, "Uzyskano dostęp", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, AccountListActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(MainActivity.this, "Niepoprawne hasło", Toast.LENGTH_LONG).show();
        }
    }
}