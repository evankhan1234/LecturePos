package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.logging.Logger;

import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;

public class LoginActivity extends AppCompatActivity {

    Button sign_in;
    EditText edit_text_password;
    EditText edit_text_email;
    ImageView show_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        edit_text_password=findViewById(R.id.edit_text_password);
        edit_text_email=findViewById(R.id.edit_text_email);
        sign_in=findViewById(R.id.sign_in);
        show_pass=findViewById(R.id.show_pass);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferenceUtil.saveShared(LoginActivity.this, SharedPreferenceUtil.TYPE_USER_ID, edit_text_email.getText().toString() + "");
                startActivity(new Intent(LoginActivity.this,OnBoardingActivity.class));
            }
        });
        edit_text_password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                show_pass.setImageDrawable(getResources().getDrawable(R.drawable.show_password));
                //  edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }
}
