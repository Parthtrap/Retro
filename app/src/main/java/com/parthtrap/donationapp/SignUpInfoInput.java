package com.parthtrap.donationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.parthtrap.donationapp.HelperClasses.UserInfoHelper;

public class SignUpInfoInput extends AppCompatActivity {

    TextView EmailDisplay;
    EditText NameBox, PhoneBox, AddressBox;
    Switch PhonePublic;
    Button Submit;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference userCollection = db.collection("UserProfiles").document(user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_info_input);

        UserInfoHelper newuser = new UserInfoHelper();

        EmailDisplay = findViewById(R.id.EmailDisplaySignUpInfoInput);
        NameBox = findViewById(R.id.NameInputSignUpInfoInput);
        PhoneBox = findViewById(R.id.PhoneInputSignUpInfoInput);
        AddressBox = findViewById(R.id.AddressInputSignUpInfoInput);
        PhonePublic = findViewById(R.id.PublicPhoneSignUpInfoInput);
        Submit = findViewById(R.id.SubmitSignUpInfoInput);

        PhonePublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    newuser.setPublicPhone(true);
                else
                    newuser.setPublicPhone(false);
            }
        });

        EmailDisplay.setText(user.getEmail());
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newuser.setName(NameBox.getText().toString().trim());
                newuser.setPhoneNumber(PhoneBox.getText().toString().trim());
                newuser.setAddress(AddressBox.getText().toString().trim());
                if (!newuser.getName().isEmpty() && !newuser.getPhoneNumber().isEmpty() && !newuser.getAddress().isEmpty())
                {

                    newuser.setEmailId(user.getEmail());

                    userCollection.set(newuser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent i = new Intent(SignUpInfoInput.this, HomePage.class);
                            startActivity(i);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpInfoInput.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(SignUpInfoInput.this, "Fill All The Fields Above", Toast.LENGTH_SHORT).show();
            }
        });

    }
}