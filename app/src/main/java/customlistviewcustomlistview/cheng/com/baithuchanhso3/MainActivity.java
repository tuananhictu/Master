package customlistviewcustomlistview.cheng.com.baithuchanhso3;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import customlistviewcustomlistview.cheng.com.baithuchanhso3.adapter.ContactAdapter;
import customlistviewcustomlistview.cheng.com.baithuchanhso3.model.Contact;


public class MainActivity extends AppCompatActivity {

    private ListView lvContact;
    private ArrayList<Contact> contactArrayList;
    private ContactAdapter adapter;
    private EditText edt_Name;
    private EditText edt_Phone;
    private Button btnAddContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidget();

        contactArrayList = new ArrayList<>();
        GetData();
        addContact();
        adapter = new ContactAdapter(this, R.layout.item_contact_listview,contactArrayList);
        lvContact.setAdapter(adapter);

        checkAndRequestPermissions();
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               showDialogConfirm(position);
//                Intent itent = new Intent(MainActivity.this, MainActivityA.class);
//                startActivity(itent);
            }
        });
    }

    public void addContact(){

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = edt_Name.getText().toString();
                String Phone = edt_Phone.getText().toString();

                Contact contact = new Contact(Name,Phone);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("DBContact");

                String id = myRef.push().getKey();

                myRef.child(id).setValue(contact).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Them thanh cong",Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Them that bai: "+e.toString(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        }

    private void GetData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DBContact");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //xoa du lieu tren listview va cap nhat
                adapter.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    //conver data qua Contact
                    Contact contact = data.getValue(Contact.class);
                    //them contact vao list
                    contact.setId(data.getKey());
                    adapter.add(contact);
                    Log.d("MyTag","onDataChange" +contact.getName());
                }
                Toast.makeText(getApplicationContext(),"Load data succes",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Load data fail"+databaseError.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    public void setWidget() {
        edt_Name =  findViewById(R.id.edt_name);
        edt_Phone = findViewById(R.id.edt_number);
        btnAddContact = findViewById(R.id.btn_add_contact);
        lvContact =  findViewById(R.id.lv_contact);
    }


    public void showDialogConfirm(final int position) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_layout);
        Button btnCall = (Button) dialog.findViewById(R.id.btn_call);
        Button btnSendMessage = (Button) dialog.findViewById(R.id.btn_send_message);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentCall(position);
            }
        });
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSendMesseage(position);
            }
        });
        dialog.show();

    }

    private void intentSendMesseage(int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contactArrayList.get(position).getPhone()));
        startActivity(intent);
    }

    private void intentCall(int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contactArrayList.get(position).getPhone()));
        startActivity(intent);
    }



}


