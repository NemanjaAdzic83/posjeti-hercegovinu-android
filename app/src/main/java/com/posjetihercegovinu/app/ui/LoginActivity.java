package com.posjetihercegovinu.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.posjetihercegovinu.app.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Kruzic je vidljiv dok traje zahjev, a dugme je ada onemoguceno
        viewModel.getLoading().observe(this, isLoading ->{
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.buttonLogin.setEnabled(!isLoading);
        });

        // Prikazuje poruku greske servera ili je skriva kad je nema
        viewModel.getError().observe(this, message ->{
            if (message != null) {
                binding.textError.setText(message);
                binding.textError.setVisibility(View.VISIBLE);
            }else {
                binding.textError.setVisibility(View.GONE);
            }
        });

        // Kad prijava uspije , zatvaramo ekran i vracamo se na prethodni
        viewModel.getSuccess().observe(this, ok ->{
            if (Boolean.TRUE.equals(ok)){
                finish();
            }
        });

        binding.buttonLogin.setOnClickListener( v -> attemptLogin());

        binding.buttonGoRegister.setOnClickListener( v -> startActivity(new Intent(this,RegisterActivity.class)));
    }

    // Provjerava polja na uredjaju prije slanja, email mora imati format,
    // a lozinka ne smije biti prazna. Tek ako je sve ispravno salje zahtjev
    private void attemptLogin() {
        String email = String.valueOf(binding.editEmail.getText()).trim();
        String password = String.valueOf(binding.editPassword.getText());

        binding.layoutEmail.setError(null);
        binding.layoutPassword.setError(null);

        boolean valid = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.layoutEmail.setError("Unesite ispravan email");
            valid = false;
        }

        if (password.isEmpty()){
            binding.layoutPassword.setError("Unesite lozinku");
            valid = false;
        }

        if (valid){
            viewModel.login(email, password);
        }

    }

    // Poziva se kad se vratimo na Login , ako je korisnik u medjuvremenu prijavljen (registracij izdaje token)
    // Login vise nije potreban
    @Override
    protected void onResume(){
        super.onResume();
        if (new com.posjetihercegovinu.app.data.local.TokenStorage(this).isLoggedIn()){
            finish();
        }
    }

}
