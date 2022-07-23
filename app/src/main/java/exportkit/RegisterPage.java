package exportkit;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.*;
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
import com.google.firebase.database.FirebaseDatabase;
import exportkit.figma.R;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener {


    //################################### google Sign in ########################################
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1234;
    //################################## end of google Sign in #################################

    //################################# register with email and passaword ######################
    private TextView registerUserButton;
    private EditText editTextEmail, editTextPasswordConfirmation,editTextPassword;
    private ProgressBar progressBar;
    //################################ end of register with email and password #################
    private FirebaseAuth mAuth;





    @Override
    //au démarage de l'application on vérifie si l'utilisateur est connecté
    //si c'est le cas alors on le renvoie sur la HomePage
    //###################################################################################################
    //####################### Méthode qui pourra potentiellement être enlevé ############################
    //###################################################################################################
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
        setContentView(R.layout.activity_register_page);

        //###############################   Google sign in   #################################
        mAuth = FirebaseAuth.getInstance();

        createRequest();

        findViewById(R.id.googleSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override //quand le boutton google est présser on appel la methode d'inscription auprés de google
            public void onClick(View view) {
                signIn();
            }
        });
        //############################# End of Google Sign In  ###############################

        //############################ Register with email and password ######################
        registerUserButton=findViewById(R.id.register_button);
        registerUserButton.setOnClickListener(this);
        editTextEmail=findViewById(R.id.email_input_register);
        editTextPasswordConfirmation =findViewById(R.id.password_confirmation_input);
        editTextPassword=findViewById(R.id.password_register_input);
        progressBar = findViewById(R.id.progressBar_register);
        //############################ end of register with email and password ###############
    }











    //######################################## Google Sign In ###################################################

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
                Toast.makeText(this,"Authentification échoué",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(RegisterPage.this, "Authentification échoué", Toast.LENGTH_SHORT).show();
                        }// ...
                    }
                });
    }

    //############################################# END of Google sign In #################################################################









    @Override
    public void onClick(View view) {
            if (view.getId()==R.id.register_button){
                registerUserEmailPassword();
            }
    }

    private void registerUserEmailPassword() {
        String email = editTextEmail.getText().toString().trim();
        String passwordConfirmation = editTextPasswordConfirmation.getText().toString().trim();
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

        if (passwordConfirmation.isEmpty()) {
            editTextPasswordConfirmation.setError("un mot de passe est obligatoire");
            editTextPasswordConfirmation.requestFocus();
            return;
        }
        //firebase n'accepte pas les mdp en dessous de 6 charactères
        if (passwordConfirmation.length() < 6) {
            editTextPasswordConfirmation.setError("Le mot de passe doit contenir au moins 6 charactères");
            editTextPasswordConfirmation.requestFocus();
            return;
        }


        if (password.isEmpty()) {
            editTextPassword.setError("Mot de passe obligatoire");
            editTextPassword.requestFocus();
            return;
        }

        if(!password.equals(passwordConfirmation)){
            editTextPassword.setError("Le mot de passe doit être identique");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(email);//if the user have been registered sucessfully

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterPage.this, "Authentification réussie", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(RegisterPage.this, "Authentification échouée,Réessayer plus tard", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterPage.this, "Authentification échouée,Réessayer plus tard", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}