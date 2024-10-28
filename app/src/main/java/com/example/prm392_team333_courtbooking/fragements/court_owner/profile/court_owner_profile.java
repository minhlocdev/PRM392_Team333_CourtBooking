package com.example.prm392_team333_courtbooking.fragements.court_owner.profile;

import static Constant.SessionConstant.courtOwner;
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

import com.example.prm392_team333_courtbooking.Interface.OnLogoutListener;
import com.example.prm392_team333_courtbooking.Interface.PasswordDialogListener;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.activities.owner.court_owner_layout;

import java.util.concurrent.atomic.AtomicBoolean;
import Models.CourtOwner;
import Repository.CourtOwnerRepository;
import Session.SessionManager;

public class court_owner_profile extends Fragment implements PasswordDialogListener {

    private EditText etFullName, etEmail, etPhone, etPassword;

    private ImageButton ibHide;

    private ImageView ivAvatar;

    private CourtOwnerRepository courtOwnerRepository;

    private SessionManager sessionManager;

    private OnLogoutListener logoutListener;
    @Override
    public void onResume() {
        super.onResume();
        ((court_owner_layout) requireActivity()).updateToolbarTitle("Profile"); // Set the title for this fragment
        ((court_owner_layout) requireActivity()).setNavigationButtonEnabled(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.court_owner_profile, container, false);

        sessionManager = new SessionManager(requireContext(), courtOwner);

        etFullName = view.findViewById(R.id.et_full_name);
        etEmail = view.findViewById(R.id.et_email);
        etPhone = view.findViewById(R.id.et_phone);
        etPassword = view.findViewById(R.id.et_password);
        Button btnSave = view.findViewById(R.id.btn_save);
        TextView tvChangePwd = view.findViewById(R.id.tv_change_pwd);
        ibHide = view.findViewById(R.id.ib_hide);
        ivAvatar = view.findViewById(R.id.iv_avatar);


        courtOwnerRepository = new CourtOwnerRepository(requireContext());
        CourtOwner courtOwner = courtOwnerRepository.getCourtOwnerById(sessionManager.getCourtOwnerId());

        loadData();
        Button btnLogout = view.findViewById(R.id.btn_logout);

        // Set click listener for the logout button
        btnLogout.setOnClickListener(v -> {
            if (logoutListener != null) {
                logoutListener.onLogout(); // Call the logout method in the container fragment
            }
        });

        btnSave.setOnClickListener(v -> {
            save(courtOwner);
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

    private void save(CourtOwner courtOwner){
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

        courtOwner.setEmail(email);
        courtOwner.setFullName(fullName);
        courtOwner.setPhone(phone);

        courtOwnerRepository.updateCourtOwner(courtOwner);

        Toast.makeText(requireContext(), "Update successfully", Toast.LENGTH_SHORT).show();

    }

    private void loadData(){
        CourtOwner courtOwner = courtOwnerRepository.getCourtOwnerById(sessionManager.getCourtOwnerId());
        etFullName.setText(courtOwner.getFullName());
        etPhone.setText(courtOwner.getPhone());
        etEmail.setText(courtOwner.getEmail());
        etPassword.setText(courtOwner.getPassword());
    }

    private void changePassword(){
        edit_password_court_owner edit_password = new edit_password_court_owner();
        edit_password.setListener(this);
        edit_password.show(getParentFragmentManager(), "edit_password");
    }

    @Override
    public void onPasswordSaved() {
        loadData();
        Toast.makeText(requireContext(), "Update password successfully!", Toast.LENGTH_SHORT).show();
    }

    public void setLogoutListener(OnLogoutListener listener) {
        this.logoutListener = listener;
    }

}
