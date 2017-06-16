package orgm.androidtown.app_location.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;
import java.util.Map;

import orgm.androidtown.app_location.R;
import orgm.androidtown.app_location.singleton.CurrentUser;


public class GoogleSignActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sing_in);
        //findViewById(R.id.sign_in_button).setOnClickListener(GoogleSignActivity.this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();


        FirebaseUser user = mAuth.getCurrentUser();
        // [END initialize_auth]
        if(user != null) {
            loginUser(user);
            moveToMapSearchActivity();
            Log.d(TAG, "이미 인증정보가 있음!!");
        } else {
            Log.d(TAG, "현재 인증정보가 없음");
        }
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mAuth = firebaseAuth;

                if (mAuth.getCurrentUser() != null) { // 인증정보가 없는 상태에서 로그인한 경우
                    Log.d(TAG, mAuth.getCurrentUser().getUid());
                    Log.d(TAG, mAuth.getCurrentUser().getEmail());
                    loginUser(mAuth.getCurrentUser());
                    moveToMapSearchActivity();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        initLoginButton();


    }

    private void initLoginButton() {
        findViewById(R.id.googleLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }
    // [END on_stop_remove_listener]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {


                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                //  updateUI(null);// [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(GoogleSignActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void loginUser(final FirebaseUser firebaseUser) {
        //새로 등록할 유저
        final String id = firebaseUser.getUid();
        final String name = firebaseUser.getDisplayName();
        final String email = firebaseUser.getEmail();
        final String photoUrl = firebaseUser.getPhotoUrl().toString();
        Log.d(TAG, id);
        CurrentUser currentUser = CurrentUser.getInstance();
        if(currentUser.getUserID() == null) {
            currentUser.setUserID(id);
            currentUser.setUserName(name);
            currentUser.setUserEmail(email);
            currentUser.setUserPhotoURL(photoUrl);
            Log.d("loginUser", "this");


            // TODO: 2017. 6. 10.  서버로 데이터 요청 보내기

            String url = "http://163.180.117.118:23023/signup";
            StringRequest userLoginRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("onResponse", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getMessage() != null)
                        Log.d("onResponse", error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", id);
                    params.put("userName", name);
                    params.put("userEmail", email);
                    params.put("userPhotoURL", photoUrl);

                    return params;
                }
            };

            Volley.newRequestQueue(this).add(userLoginRequest);
        }

        //Volley.newRequestQueue(mContext).add(FriendLocationRequest) ;
    }


    private void moveToMapSearchActivity() {
        Intent intent = new Intent(GoogleSignActivity.this, MainActivity.class);
        if(getIntent().getExtras() != null)
            intent.putExtras(getIntent().getExtras());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}