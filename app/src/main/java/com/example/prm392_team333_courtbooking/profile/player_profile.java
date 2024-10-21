package com.example.prm392_team333_courtbooking.profile;

import static Constant.SessionConstant.user;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.prm392_team333_courtbooking.Interface.PasswordDialogListener;
import com.example.prm392_team333_courtbooking.R;
import java.util.concurrent.atomic.AtomicBoolean;
import Models.User;
import Repository.UserRepository;
import Session.SessionManager;

public class player_profile extends Fragment implements PasswordDialogListener {

    private EditText etFullName, etEmail, etPhone, etPassword;

    private ImageButton ibHide;

    private ImageView ivAvatar;

    private UserRepository userRepository;

    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player_profile, container, false);

        sessionManager = new SessionManager(requireContext(), user);

        etFullName = view.findViewById(R.id.et_full_name);
        etEmail = view.findViewById(R.id.et_email);
        etPhone = view.findViewById(R.id.et_phone);
        etPassword = view.findViewById(R.id.et_password);
        Button btnSave = view.findViewById(R.id.btn_save);
        TextView tvChangePwd = view.findViewById(R.id.tv_change_pwd);
        ibHide = view.findViewById(R.id.ib_hide);
        ivAvatar = view.findViewById(R.id.iv_avatar);


        userRepository = new UserRepository(requireContext());
        User user = userRepository.getUserById(sessionManager.getUserId());

        loadData();

        btnSave.setOnClickListener(v -> {
            save(user);
        });

        tvChangePwd.setOnClickListener(v -> {
            changePassword();
        });

        AtomicBoolean isPasswordVisible = new AtomicBoolean(false);

        ibHide.setOnClickListener(v -> {
            if (isPasswordVisible.get()) {
                // Hide password
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ibHide.setImageResource(R.drawable.baseline_key_24);
                isPasswordVisible.set(false);
            } else {
                // Show password
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ibHide.setImageResource(R.drawable.baseline_key_off_24);
                isPasswordVisible.set(true);
            }

            etPassword.setSelection(etPassword.getText().length());
        });

        return view;
    }

    private void save(User user){
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

         if(fullName.isEmpty() || fullName.isBlank()){
             etFullName.setError("Full name is required");
             etFullName.requestFocus();
             return;
         }

         if(email.isEmpty() || email.isBlank()){
             etEmail.setError("Email is required");
             etEmail.requestFocus();
             return;
         }

         if(phone.isEmpty() || phone.isBlank()){
             etPhone.setError("Phone is required");
             etPhone.requestFocus();
             return;
         }

         user.setEmail(email);
         user.setFullName(fullName);
         user.setPhone(phone);

         userRepository.updateUser(user);

    }

    private void loadData(){
        User user = userRepository.getUserById(sessionManager.getUserId());
        etFullName.setText(user.getFullName());
        etPhone.setText(user.getPhone());
        etEmail.setText(user.getEmail());
        etPassword.setText(user.getPassword());
    }

    private void changePassword(){
        edit_password edit_password = new edit_password();
        edit_password.setListener(this);
        edit_password.show(getParentFragmentManager(), "edit_password");
    }

    @Override
    public void onPasswordSaved() {
        loadData();
        Toast.makeText(requireContext(), "Update password successfully!", Toast.LENGTH_SHORT).show();
    }
}
