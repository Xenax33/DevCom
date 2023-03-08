package com.example.devcom.Logging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.devcom.BL.Person;
import com.example.devcom.MainActivity;
import com.example.devcom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class SignUp extends AppCompatActivity {

    Button signin;
    Button loadImg;
    ImageView img;
    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signin = findViewById(R.id.sign_UP3);
        loadImg = findViewById(R.id.loadImg);
        img = findViewById(R.id.gone);
        img.setTag(R.drawable.add);
        auth = FirebaseAuth.getInstance();
         currentUser = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
//        if(FirebaseAuth.getInstance().getCurrentUser() != null)
//        {
//            Intent intent = new Intent(SignIn.this , MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
        //signin.setVisibility(View.GONE);
//        loadImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 3);
//                //new ActivityResultContracts.StartActivityForResult(intent , 3);
//            }
//
//        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name;
                EditText pass;
                EditText id;
                name = findViewById(R.id.Name);
                pass = findViewById(R.id.password4);
                id = findViewById(R.id.ID);

               // if (!(name.getText().equals("") || pass.getText().equals("") || id.getText().equals(""))) {
                 if(id.getText().toString().trim().length() != 0 && name.getText().toString().trim().length() != 0 &&
                         pass.getText().toString().trim().length() != 0 )
                 {
                    auth.createUserWithEmailAndPassword(id.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Person p = new Person(id.getText().toString(), pass.getText().toString(), name.getText().toString());
                                String userID = task.getResult().getUser().getUid();

                                p.setDataBaseID(userID);
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                String email = p.getID();
                                String emailParts[] = email.split("@");
                                ref.child("KeysHash").child(emailParts[0]).setValue(userID);
                                //db.getReference().child(p.getID());
                                db.getReference().child("Users").child(userID).setValue(p);


                                //Intent intentImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                //startActivityForResult(intentImg, 3);
                                Toast.makeText(SignUp.this, "Data has Been Saved", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this , MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else
                            {
                                Toast.makeText(SignUp.this, "Error in Saving", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "Please Provide All Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && data != null) {
//            Uri selectedImg = data.getData();
//            img.setImageURI(selectedImg);
//            final StorageReference reference = storage.getReference().child("profile_pic")
//                    .child(FirebaseAuth.getInstance().getUid());
//            reference.putFile(selectedImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            db.getReference().child("Users").child(auth.getUid()).child("profile_pic").setValue(uri.toString());
//                            Toast.makeText(SignIn.this, "Picture Has Been Uploaded", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(SignIn.this , MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                            //signin.setVisibility(View.VISIBLE);
//                        }
//                    });
//                }
//            });
//        }
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(currentUser != null)
//        {
//            Intent intent = new Intent(SignIn.this , MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
}
