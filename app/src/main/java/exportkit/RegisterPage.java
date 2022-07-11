package exportkit;

import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
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

public class RegisterPage extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1234;
    private FirebaseAuth mAuth;

    @Override
    //au démarage de l'application on vérifie si l'utilisateur est connecté
    //si c'est le cas alors on le renvoie sur la HomePage
    //###################################################################################################
    //####################### Méthode qui pourra potentiellement être enlevé ############################
    //###################################################################################################
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            Intent intent = new Intent(getApplicationContext(),HomePage.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();

        createRequest();

        findViewById(R.id.googleSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override //quand le boutton google est présser on appel la methode d'inscription auprés de google
            public void onClick(View view) {
                signIn();
            }
        });
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
                            Toast.makeText(RegisterPage.this, "Authentification échoué", Toast.LENGTH_SHORT).show();
                        }// ...
                    }
                });
    }
}