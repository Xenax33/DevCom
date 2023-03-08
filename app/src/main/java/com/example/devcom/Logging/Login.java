package com.example.devcom.Logging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.devcom.MainActivity;
import com.example.devcom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    Button login , signin;
    EditText ID;
    EditText password;
    FirebaseDatabase db;
    FirebaseAuth auth;
    FirebaseUser currUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing);
//        Person p = new Person( "123" , "123","azan" , R.drawable.img3 );
//        Person q = new Person("321" , "321" ,  "usman", R.drawable.logo);
//        UserDL.addUser("123" , p);
//        UserDL.addUser("321" , q);
//        PersonDL.AddVertex(p , new ArrayList<Person>());
//        PersonDL.AddVertex(q , new ArrayList<Person>());
//        PersonDL.addEdge(p , q);

//        q.addPost(new Post("12/13/2022", "how to solve this problem" , R.drawable.img1));
//        q.addPost(new Post("12/13/2022", "how to solve that problem" , R.drawable.img2));
//        q.addPost(new Post("12/13/2022", "Nazeef LunD" , R.drawable.logo));
//        p.addPost(new Post("69/69/6969", "HELLO MERE BHAIYYO YE GAANA TO SUNO ZARA BOHT HEAVY H TO SUNO NAZEEF LORA" , R.drawable.logo));


        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        currUser = auth.getCurrentUser();
        login = (Button) findViewById(R.id.Log_IN);
        signin = (Button) findViewById(R.id.Sign_UP);
        ID = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ID.getText().toString();
                String pass = password.getText().toString();
                if(id.trim().length() != 0  && pass.trim().length() != 0 )
                auth.signInWithEmailAndPassword(id , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("ID" , id);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(Login.this, "Invalid ID or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //boolean p = UserDL.isValidUser(id , pass );
                //if(p != false)
                //{
//                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                    intent.putExtra("ID" , id);
//                    startActivity(intent);
//                    finish();
               // }
//                else {
//                    Toast.makeText(signing.this, "Invalid ID or Password", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this, "sign in", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getBaseContext(), SignUp.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currUser != null)
        {
            Intent intent = new Intent(Login.this, MainActivity.class);
           // intent.putExtra("ID" , id);
            startActivity(intent);
            finish();
        }
    }
}
