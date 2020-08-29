package com.example.bachelorthesisclient.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.EventDetails;
import com.example.bachelorthesisclient.model.Ticket;
import com.example.bachelorthesisclient.repository.RepositoryFactory;
import com.example.bachelorthesisclient.repository.ticket.TicketRepository;
import com.example.bachelorthesisclient.ui.activity.ImagePreviewActivity;
import com.example.bachelorthesisclient.ui.activity.UserTicketsActivity;
import com.example.bachelorthesisclient.util.DateTimeUtil;
import com.example.bachelorthesisclient.util.EventDetailsPersistenceUtil;
import com.example.bachelorthesisclient.util.ImageUtil;
import com.example.bachelorthesisclient.util.QRCodeGeneratorUtil;
import com.example.bachelorthesisclient.wrapper.GsonWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class TicketAdapter extends ArrayAdapter<Ticket> {
    private Context context;
    private List<Ticket> ticketList;
    private TicketRepository ticketRepository;

    public TicketAdapter(@NonNull Context context, List<Ticket> list) {
        super(context, 0, list);
        this.context = context;
        this.ticketList = list;
        this.ticketRepository = (TicketRepository) RepositoryFactory.get(RepositoryFactory.TICKET_REPOSITORY);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        Ticket ticket = ticketList.get(position);

        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_list_ticket_details, parent, false);
        }

        ImageView ivQRCode = listItem.findViewById(R.id.iv_qr_code);
        TextView tvEventName = listItem.findViewById(R.id.tv_event_name);
        TextView tvDateString = listItem.findViewById(R.id.tv_date_string);
        ImageButton imgbtnTicketCancel = listItem.findViewById(R.id.imgbtn_cancel_ticket);

        Bitmap qrCodeBitmap = getQRCodeBitmap(ticket);

        ivQRCode.setImageBitmap(qrCodeBitmap);

        ivQRCode.setOnClickListener(handleOnClickListenerQrCode(qrCodeBitmap));
        imgbtnTicketCancel.setOnClickListener(handleOnClickCancelTicket(ticket));

        EventDetails eventDetails = EventDetailsPersistenceUtil.getEventDetails();

        tvEventName.setText(eventDetails.getName());
        tvDateString.setText(DateTimeUtil.formatDate(eventDetails.getEventDate()));

        return listItem;
    }

    private View.OnClickListener handleOnClickListenerQrCode(final Bitmap qrCodeBitmap) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtil.previewImage(qrCodeBitmap, context);
            }
        };
    }

    private View.OnClickListener handleOnClickCancelTicket(final Ticket ticket) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to cancel ticket reservation")
                        .setPositiveButton("Yes", handleDialogResult(ticket))
                        .setNegativeButton("No", handleDialogResult(ticket))
                        .show();
            }
        };
    }

    private Bitmap getQRCodeBitmap(Ticket ticket) {
        String json = GsonWrapper.toJson(ticket);
        return QRCodeGeneratorUtil.generateQRCode(json);
    }

    private DialogInterface.OnClickListener handleDialogResult(final Ticket ticket) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: {

                        ticketRepository.cancelTicket(ticket.getId())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<Boolean>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Boolean success) {
                                        if (success) {
                                            ticketList.remove(ticket);
                                            notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                        break;

                    }
                    case DialogInterface.BUTTON_NEGATIVE: {
                        break;
                    }
                }
            }
        };
    }
}
