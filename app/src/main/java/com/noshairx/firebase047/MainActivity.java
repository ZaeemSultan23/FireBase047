package com.noshairx.firebase047;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore objectFirebaseFireStore;
    private CollectionReference ObjectCollectionReference;
    private DocumentReference objectDocumentReference;
    private Dialog objectDialog;
    private EditText  htlId,htlname,htlno;
    private TextView TV1;
    private static final String CollectionName="HOTEL";
    private static final String Hotel_ID="";
    private static final String Hotel_Name="hotel_name";
    private static final String Hotel_No="hotel_no";
    private String allData ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectDialog=new Dialog(this);
        objectDialog.setContentView(R.layout.please_wait);

        htlId=findViewById(R.id.htlIDET);
        htlname=findViewById(R.id.htlNameET);
        htlno=findViewById(R.id.htlnoET);

        TV1 = findViewById(R.id.tv_1);

        try {
            objectFirebaseFireStore=FirebaseFirestore.getInstance();
            ObjectCollectionReference=objectFirebaseFireStore.collection(CollectionName);
        }
        catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    public void  SHOWDATA(View v)
    {

        try
        {
            objectDialog.show();
            ObjectCollectionReference.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            objectDialog.dismiss();
                            TV1.setText("");
                            htlId.setText("");
                            for (DocumentSnapshot objectDocumentReference : queryDocumentSnapshots) {
                                String htl_ID = objectDocumentReference.getId();
                                String htl_Name = objectDocumentReference.getString(Hotel_Name);
                                String htl_no = objectDocumentReference.getString(Hotel_No);
                                allData += "Hotel ID : " + htl_ID + '\n' + "Hotel Name : " + htl_Name + '\n' + "Hotel No : " + htl_no ;
                            }
                            TV1.setText(allData);
                            Toast.makeText(MainActivity.this,"SUCCESSFULLY RETRIEVE DATA: ",Toast.LENGTH_LONG).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    objectDialog.dismiss();
                    Toast.makeText(MainActivity.this, "FAILED TO RETRIEVE: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch(Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void  COLLECTIONDELETE(View v)
    {
        try
        {
            if(!htlId.getText().toString().isEmpty())
            {


                objectDocumentReference = objectFirebaseFireStore.collection(CollectionName)
                        .document(htlId.getText().toString());



                objectDocumentReference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"DELETED SUCCESSFULLY",Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"FAILED TO DELETE",Toast.LENGTH_LONG).show();
                            }
                        });
            }
            else
            {
                Toast.makeText(this,"Fails to delete the Document",Toast.LENGTH_LONG);
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    public void addvalues(View v) {

        try {
            objectFirebaseFireStore = FirebaseFirestore.getInstance();
            objectFirebaseFireStore.collection(CollectionName).document(htlId.getText().toString()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                            if (task.getResult().exists()) {
                                Toast.makeText(MainActivity.this, "IT EXISTS TRY NEW ONE", Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                if(!htlId.getText().toString().isEmpty() && !htlname.getText().toString().isEmpty() && !htlno.getText().toString().isEmpty()) {
                                    objectDialog.show();

                                    Map<String,Object> objMap=new HashMap<>();
                                    objMap.put("hotel_name", htlname.getText().toString());
                                    objMap.put("hotel_no", htlno.getText().toString());
                                    objectFirebaseFireStore.collection(CollectionName)
                                            .document(htlId.getText().toString()).set(objMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    objectDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    objectDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "DATA ADDING FAILED", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "ENTER ALL VALID DETAILS", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });

        }
        catch (Exception e)
        {

            Toast.makeText(this, "ADD VALUES"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }
}