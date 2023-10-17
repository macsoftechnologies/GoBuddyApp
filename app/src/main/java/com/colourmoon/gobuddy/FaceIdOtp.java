package com.colourmoon.gobuddy;

/*import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.view.activities.LoginActivity;
import com.poovam.pinedittextfield.PinField;
import com.poovam.pinedittextfield.SquarePinField;

public class FaceIdOtp extends AppCompatActivity {

    private PinField squarePinField1, squarePinField2;
    private TextView button_click;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_id_otp);
        squarePinField1 = findViewById(R.id.square_field1);
        squarePinField2 = findViewById(R.id.square_field2);
        button_click = findViewById(R.id.button_click);


        button_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(FaceIdOtpActivity.this, "Wrong Pin", Toast.LENGTH_SHORT).show();
                submit();
            }
        });



    }


    private void submit() {

        String text1 = squarePinField1.getText().toString();
        String text2 = squarePinField2.getText().toString();

        if (text1.equals(text2)) {


            String savedText = text1;

            // Intent intent=new Intent(FaceIdOtpActivity.this, LoginActivity.class);
            //startActivity(intent);
            // Save the PIN in SharedPreferences
            SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("value", savedText);
            editor.apply();


        }
        // Create an Intent to start the other activity (Activity B)

        else{

            Toast.makeText(this, "not matched PIN", Toast.LENGTH_SHORT).show();


            // Handle the case where the texts are not equal
        }

    }




}*/
