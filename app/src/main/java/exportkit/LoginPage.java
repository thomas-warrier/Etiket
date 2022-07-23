package exportkit;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import exportkit.figma.R;


public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1234;
    private FirebaseAuth mAuth;

    private Button registerButton;
    private EditText editTextEmail,editTextPassword;
    private Button signIn; //button to sign in with email and password
    private ProgressBar progressBar;


    @Override
    //au démarage de l'application on vérifie si l'utilisateur est connecté
    //si c'est le cas alors on le renvoie sur la HomePage
    protected void onStart(){
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            Intent intent = new Intent(getApplicationContext(),HomePage.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        createRequest();

        findViewById(R.id.googleLogInButton).setOnClickListener(new View.OnClickListener() {
            @Override //quand le boutton google est présser on appel la methode d'inscription auprés de google
            public void onClick(View view) {
                signIn();
            }
        });

        registerButton = findViewById(R.id.button_switch_to_register_activity);
        registerButton.setOnClickListener(this);

        signIn = findViewById(R.id.se_connecter_button);
        signIn.setOnClickListener(this);

        editTextEmail = findViewById(R.id.email_input_login);
        editTextPassword = findViewById(R.id.password_login_input);

        progressBar = findViewById(R.id.progressBar_login);
    }



    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by google sign in option.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn(){
        Intent signInIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    //cette méthode permet de vérifier si l'opération d'inscription c'est bien passé

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful,authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed,update UI appropriately
                Toast.makeText(this,"Authentification échoué",Toast.LENGTH_SHORT);
            }
        }
    }
    //cette méthode est appelée lorsque la vérification d'inscription c'est bien déroulé
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(),HomePage.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginPage.this, "Authentification échoué", Toast.LENGTH_SHORT).show();
                        }// ...
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_switch_to_register_activity:
                startActivity(new Intent(this,RegisterPage.class));
                break;
            case R.id.se_connecter_button:
                UserLogin();
                break;

        }
    }

    private void UserLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Une adresse e-mail est obligatoire");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Adresse e-mail invalide");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Mot de passe obligatoire");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Le mot de passe doit contenir au moins 6 charactères");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginPage.this,HomePage.class));
                }else{
                    Toast.makeText(LoginPage.this, "Authentification échouée", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}