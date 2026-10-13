package com.posjetihercegovinu.app.ui;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.posjetihercegovinu.app.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel viewModel;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        viewModel.getLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.buttonRegister.setEnabled(!isLoading);
        });

        viewModel.getError().observe(this, message -> {
            if (message != null){
                binding.textError.setText(message);
                binding.textError.setVisibility(View.VISIBLE);
            }else {
                binding.textError.setVisibility(View.GONE);
            }
        });

        viewModel.getSuccess().observe(this, ok -> {
            if (Boolean.TRUE.equals(ok)){
                finish();
            }
        });
        binding.buttonRegister.setOnClickListener( v -> attemptRegister());

    }

    private void attemptRegister() {

        String name = String.valueOf(binding.editName.getText()).trim();
        String email = String.valueOf(binding.editEmail.getText()).trim();
        String password = String.valueOf(binding.editPassword.getText());
        String confirm = String.valueOf(binding.editConfirm.getText());

        binding.layoutName.setError(null);
        binding.layoutEmail.setError(null);
        binding.layoutPassword.setError(null);
        binding.layoutConfirm.setError(null);


        boolean valid = true;
        if (name.isEmpty()){
            binding.layoutName.setError("Unesite imena");
            valid = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.layoutName.setError("Unstie ispavan email");
            valid = false;
        }

        if (password.length() < 9 || password.length() > 72){
            binding.layoutPassword.setError("Lozinka mora imati 8 do 72 znaka");
            valid = false;
        }

        if (!password.equals(confirm)){
            binding.layoutConfirm.setError("Lozinke se ne podudaraju");
            valid = false;
        }

        if (valid){
            viewModel.register(name, email, password);
        }
    }
}
