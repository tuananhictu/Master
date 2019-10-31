package customlistviewcustomlistview.cheng.com.baithuchanhso3.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import customlistviewcustomlistview.cheng.com.baithuchanhso3.R;
import customlistviewcustomlistview.cheng.com.baithuchanhso3.model.Contact;
import customlistviewcustomlistview.cheng.com.baithuchanhso3.sua_contact;


public class ContactAdapter extends ArrayAdapter<Contact>{
@NonNull
   private Activity activity;
   private int resource;
@NonNull
    private List<Contact> objects;
    public ContactAdapter(@NonNull Activity activity, int resource,@NonNull List<Contact> objects) {
        super(activity, resource, objects);
        this.activity =activity;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,@NonNull final ViewGroup parent) {
        //moi doi tuong contact se tra ve mot view, voi 1 view tuong ung voi doi tuong contact va luu lai
        //position(vi tri ) cua contact do
        LayoutInflater inflater = this.activity.getLayoutInflater();
        View view = inflater.inflate(this.resource,null);
            ImageView imgAvatar = view.findViewById(R.id.img_avatar);
            TextView tvName = view.findViewById(R.id.tv_name);
            TextView tvNumber = view.findViewById(R.id.tv_number);
            ImageView btnMenu = view.findViewById(R.id.btnMenu);

        final Contact contact = this.objects.get(position);

        tvName.setText(contact.getName());
        tvNumber.setText(contact.getPhone());

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(activity,view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId()==R.id.sua){
                                //khi nhan sua hien thi thong tin contact can sua
                                Intent intent = new Intent(activity, sua_contact.class);
                                //gui contact sang man hinh sua

                                intent.putExtra("CONTACT",contact);
                                activity.startActivity(intent);
                            }else if (menuItem.getItemId()==R.id.delete){
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("DBContact");
                                myRef.child(contact.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        Toast.makeText(activity,"Xoa Thanh Cong",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        return false;
                    }
                });
                //truyen menu
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                //show icon
                try {
                    Field field = popupMenu.getClass().getDeclaredField("mPopup");
                    field.setAccessible(true);
                    Object popUpMenuHelper = field.get(popupMenu);
                    Class<?> cls = Class.forName("com.android.internal.view.menu.MenuPopUpHelper");
                    Method method = cls.getDeclaredMethod("setForceShowicon",new Class[]{boolean.class});
                    method.setAccessible(true);
                    method.invoke(popUpMenuHelper,new Object[]{true});
                }catch (Exception e){
                    Log.d("MYTAG","onClick: " +e.toString() );
                }
                popupMenu.show();
            }
        });

        return view;
    }

}
