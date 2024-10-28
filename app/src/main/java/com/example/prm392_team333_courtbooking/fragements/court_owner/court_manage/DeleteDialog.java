package com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.prm392_team333_courtbooking.R;

public class DeleteDialog extends DialogFragment {

    private RefuseDialogListener listener;

    public interface RefuseDialogListener {
        void onRefuseConfirmed(String reason, int bookingId);
    }

    private final int bookingId;

    public DeleteDialog(int bookingId, RefuseDialogListener listener) {
        this.bookingId = bookingId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.delete_confirm_dialog, null);

        EditText etReason = dialogView.findViewById(R.id.et_reason);

        builder.setView(dialogView)
                .setTitle("Refuse Booking")
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("REFUSE", (dialog, which) -> {
                    String reason = etReason.getText().toString();
                    if (!reason.isEmpty()) {
                        listener.onRefuseConfirmed(reason, bookingId);
                    }
                });

        return builder.create();
    }
}
