package customlistviewcustomlistview.cheng.com.baithuchanhso3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import customlistviewcustomlistview.cheng.com.baithuchanhso3.model.Contact;

public class sua_contact extends AppCompatActivity {
    private EditText edtName,edtPhone;
    private Button btnBack,btnHuy,btnSua;
    private   Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_contact);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact != null){
                    edtName.setText(contact.getName()+"");
                    edtPhone.setText(contact.getPhone()+"");
                }else {
                    Toast.makeText(getApplicationContext(),"Loi load du lieu",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cap nhat len firebase
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                String id = contact.getId();

                //tim id tren firebase
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("DBContact");
                myRef.child(id).child("name").setValue(name);
                myRef.child(id).child("phone").setValue(phone);
                finish();
                Toast.makeText(getApplicationContext(),"Sua thanh cong",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addControls() {
        edtName = findViewById(R.id.edtName);
        edtPhone= findViewById(R.id.edtPhone);

        btnBack = findViewById(R.id.btnBack);
        btnHuy = findViewById(R.id.btnHuy);
        btnSua = findViewById(R.id.btnSua);

        //lay goi tin tu man hinh ngoai
        Intent intent = getIntent();
       contact = (Contact) intent.getSerializableExtra("CONTACT");
        if (contact != null){
            edtName.setText(contact.getName()+"");
            edtPhone.setText(contact.getPhone()+"");
        }else {
            Toast.makeText(this,"Loi load du lieu",Toast.LENGTH_LONG).show();
        }

    }

}
