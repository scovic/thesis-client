package com.example.bachelorthesisclient.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.Ticket;
import com.example.bachelorthesisclient.ui.adapter.TicketAdapter;
import com.example.bachelorthesisclient.ui.viewmodel.UserTicketsViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class UserTicketsActivity extends AppCompatActivity {
    private TicketAdapter ticketAdapter;
    private UserTicketsViewModel mViewModel;

    private TextInputLayout etTicketQuantity;
    private Button btnReserve;
    private ListView ticketList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvErrorMessage;
    private TextView tvNoTicketsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tickets);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setViews();
        setViewModel();
        setListeners();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViewModel() {
        this.mViewModel = new ViewModelProvider(this).get(UserTicketsViewModel.class);
        observeViewModel();
    }

    private void observeViewModel() {
        mViewModel.getTickets().observe(this, new Observer<List<Ticket>>() {
            @Override
            public void onChanged(List<Ticket> tickets) {
                ticketAdapter = new TicketAdapter(UserTicketsActivity.this, tickets);
                ticketList.setAdapter(ticketAdapter);
            }
        });

        mViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                swipeRefreshLayout.setRefreshing(loading);

            }
        });

        mViewModel.getReserveBtnDisabled().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDisabled) {
                btnReserve.setEnabled(!isDisabled);
            }
        });

        mViewModel.getShowErrorMessage().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean showErrorMessage) {
                if (showErrorMessage) {
                    tvErrorMessage.setVisibility(View.VISIBLE);
                } else {
                    tvErrorMessage.setVisibility(View.GONE);
                }
            }
        });

        mViewModel.getNoReservedTickets().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean noTickets) {
                if (noTickets) {
                    tvNoTicketsMessage.setVisibility(View.VISIBLE);
                } else {
                    tvNoTicketsMessage.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setViews() {
        etTicketQuantity = findViewById(R.id.et_num_of_tickets);
        btnReserve = findViewById(R.id.btn_reserve_tickets);
        ticketList = findViewById(R.id.ticket_list);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_tickets);
        tvErrorMessage = findViewById(R.id.tv_user_tickets_error_msg);
        tvNoTicketsMessage = findViewById(R.id.tv_no_tickets);

    }

    private void setListeners() {
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.purchaseTickets();
                etTicketQuantity.getEditText().setText("");
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.reloadData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        etTicketQuantity.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    int quantity = Integer.parseInt(s.toString());
                    mViewModel.setTicketsQuantity(quantity);
                } else {
                    mViewModel.setTicketsQuantity(0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
