package com.example.prm392_team333_courtbooking.profile;

import static Constant.SessionConstant.user;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.prm392_team333_courtbooking.Interface.BookingDialogListener;
import com.example.prm392_team333_courtbooking.Interface.PasswordDialogListener;
import com.example.prm392_team333_courtbooking.R;

import Models.User;
import Repository.UserRepository;
import Session.SessionManager;

public class edit_password extends DialogFragment {

    private EditText etOldPwd, etNewPwd, etConfirmedPwd;

    private UserRepository userRepository;

    private SessionManager sessionManager;

    private PasswordDialogListener listener;

    public void setListener(PasswordDialogListener listener) {
        this.listener = listener;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_password, container, false);

        etOldPwd = view.findViewById(R.id.et_old_password);
        etNewPwd = view.findViewById(R.id.et_new_password);
        etConfirmedPwd = view.findViewById(R.id.et_confirmed_password);

        userRepository = new UserRepository(requireContext());
        sessionManager = new SessionManager(requireContext(), user);

        Button btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> {
            save();
        });

        return view;
    }

    private void save(){

        String oldPwd = etOldPwd.getText().toString().trim();
        String newPwd = etNewPwd.getText().toString().trim();
        String confirmedPwd = etConfirmedPwd.getText().toString().trim();

        if(oldPwd.isEmpty() || oldPwd.isBlank()){
            etOldPwd.setError("Old password is required");
            etOldPwd.requestFocus();
            return;
        }

        User user = userRepository.getUserById(sessionManager.getUserId());

        if(!user.getPassword().equals(oldPwd)){
            etOldPwd.setError("Wrong password");
            return;
        }

        if(newPwd.isEmpty() || oldPwd.isBlank()){
            etNewPwd.setError("New password is required");
            etNewPwd.requestFocus();
            return;
        }

        if(confirmedPwd.isEmpty() || oldPwd.isBlank()){
            etConfirmedPwd.setError("Confirmed password is required");
            etNewPwd.requestFocus();
            return;
        }

        if(!newPwd.equals(confirmedPwd)){
            etConfirmedPwd.setError("Passwords are not match ");
            return;
        }

        user.setPassword(newPwd);

        int id = userRepository.updateUser(user);

        if(id <= 0){
            Toast.makeText(requireContext(), "Fail to update password", Toast.LENGTH_SHORT).show();
            return;
        }else{
            if (listener != null){
                listener.onPasswordSaved();
            }
            dismiss();
        }
    }

}
