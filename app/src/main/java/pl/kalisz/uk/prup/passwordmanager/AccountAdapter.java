package pl.kalisz.uk.prup.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private final List<AccountModel> accountList;
    private final Context context;

    public AccountAdapter(List<AccountModel> accountList, Context context) {
        this.accountList = accountList;
        this.context = context;
    }

    // 1. new view (cardview_city.xml)
    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_account, parent, false);
        return new AccountViewHolder(view);
    }

    // 2. set city data for chosen element on the list
    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        AccountModel currentAccount = accountList.get(position);
        holder.tv_platform.setText(currentAccount.getPlatform());
        holder.tv_login.setText(String.format("%s | ********", currentAccount.getLogin()));

        holder.cl_cardview_account.setOnClickListener(v -> {
            Intent intent = new Intent(context, AccountManageActivity.class);
            intent.putExtra(AccountManageActivity.EXTRA_ACCOUNT_ID, currentAccount.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    // Reference to view from cardview_account.xml
    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView tv_platform, tv_login;
        ConstraintLayout cl_cardview_account;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_platform = itemView.findViewById(R.id.tv_platform);
            tv_login = itemView.findViewById(R.id.tv_login);
            cl_cardview_account = itemView.findViewById(R.id.cl_cardview_account);
        }
    }
}
