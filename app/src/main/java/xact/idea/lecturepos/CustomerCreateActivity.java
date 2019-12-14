package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Utils.Common;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;
import xact.idea.lecturepos.Utils.SharedPreferenceUtil;

public class CustomerCreateActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText phone;
    EditText retail_code;
    EditText address;
    EditText edit_shop_name;
    Button create;
    ImageView btn_header_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_create);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        firstName=findViewById(R.id.firstName);
        lastName=findViewById(R.id.lastName);
        edit_shop_name=findViewById(R.id.edit_shop_name);
        phone=findViewById(R.id.phone);
        retail_code=findViewById(R.id.retail_code);
        address=findViewById(R.id.address);
        create=findViewById(R.id.create);
        btn_header_back=findViewById(R.id.btn_header_back);
        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerCreateActivity.this,CustomerActivity.class));
                finish();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Fsdfs","SFs"+firstName.getText().toString());
                Log.e("Fsdfs","SFs"+lastName.getText().toString());
                Log.e("Fsdfs","SFs"+phone.getText().toString());
                Log.e("Fsdfs","SFs"+retail_code.getText().toString());
                Log.e("Fsdfs","SFs"+address.getText().toString());
                if (!firstName.getText().toString().equals("") &&!phone.getText().toString().equals("") &&!address.getText().toString().equals("")&&!retail_code.getText().toString().equals("") ){
                    Customer c = new Customer();
                    c.Name=firstName.getText().toString()+" "+lastName.getText().toString();
                    c.Address=address.getText().toString();
                    c.MobileNumber=phone.getText().toString();
                    c.StoreId= SharedPreferenceUtil.getUserID(CustomerCreateActivity.this);
                    c.RetailerCode=retail_code.getText().toString();
                    c.ShopName=edit_shop_name.getText().toString();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = new Date(System.currentTimeMillis());
                    String currentDate = formatter.format(date);
                    SimpleDateFormat formatters = new SimpleDateFormat("hh:mm:ss");
                    Date dates = new Date(System.currentTimeMillis());
                    String currentTime = formatters.format(dates);
                    c.UpdateDate=currentDate+" "+currentTime;
                    c.UpdateNo="0";
                    Common.customerRepository.insertToCustomer(c);

                    startActivity(new Intent(CustomerCreateActivity.this,CustomerActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(CustomerCreateActivity.this, "Please fill All the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
