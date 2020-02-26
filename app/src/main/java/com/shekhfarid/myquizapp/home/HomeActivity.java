package com.shekhfarid.myquizapp.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.shekhfarid.myquizapp.R;
import com.shekhfarid.myquizapp.databinding.ActivityHomeBinding;
import com.shekhfarid.myquizapp.home.model.Question_Adepter;
import com.shekhfarid.myquizapp.home.model.Question_Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements Question_Adepter.AdepterListener {

    ActivityHomeBinding homeBinding;
    FirebaseFirestore firestore;
    static final String collection_tbls = "tbls_Question";
    List<Question_Model> questionModelsList;
    Question_Adepter questionAdepter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(HomeActivity.this, R.layout.activity_home);
        firestore = FirebaseFirestore.getInstance();

        interRecyclerView();

        homeBinding.addbtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = homeBinding.questionID.getText().toString().trim();
                String aText = homeBinding.optionAID.getText().toString().trim();
                String bText = homeBinding.optionBID.getText().toString().trim();
                String cText = homeBinding.optionCID.getText().toString().trim();
                String dText = homeBinding.optionDID.getText().toString().trim();
                String currectAns = homeBinding.currectAns.getText().toString().trim();

//                String toast_Text = "Question : "+question+"\nA : "+aText+"\nB : "+bText+"\nC : "+cText+"\nD : "+dText+"\nCurrect Ans : "+currectAns;
//                Toast.makeText(HomeActivity.this,toast_Text,Toast.LENGTH_SHORT).show();
                questionData(question,aText,bText,cText,dText,currectAns);
                clear();
            }
        });
        homeBinding.updateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!homeBinding.idtextSimple.getText().toString().trim().isEmpty()) {
                    String question = homeBinding.questionID.getText().toString().trim();
                    String aText = homeBinding.optionAID.getText().toString().trim();
                    String bText = homeBinding.optionBID.getText().toString().trim();
                    String cText = homeBinding.optionCID.getText().toString().trim();
                    String dText = homeBinding.optionDID.getText().toString().trim();
                    String answer = homeBinding.currectAns.getText().toString().trim();

                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("Question",question);
                    hashMap.put("A_Text",aText);
                    hashMap.put("B_Text",bText);
                    hashMap.put("C_Text",cText);
                    hashMap.put("D_Text",dText);
                    hashMap.put("Currect_Ans",answer);

                    clear();

                    firestore.collection(collection_tbls).document(homeBinding.idtextSimple.getText().toString().trim()).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(HomeActivity.this,"Data Update",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(HomeActivity.this,"Data Not Update",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(HomeActivity.this,"Field is Empty!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        homeBinding.deleteID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!homeBinding.idtextSimple.getText().toString().trim().isEmpty()){
                    firestore.collection(collection_tbls).document(homeBinding.idtextSimple.getText().toString().trim()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(HomeActivity.this,"Data delete",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(HomeActivity.this,"Data delete",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    clear();
                }
            }
        });
        datarealTimeGetListener();
    }

    private void clear() {
        homeBinding.questionID.setText("");
        homeBinding.optionAID.setText("");
        homeBinding.optionBID.setText("");
        homeBinding.optionCID.setText("");
        homeBinding.optionDID.setText("");
        homeBinding.currectAns.setText("");
    }

    public void questionData(String question,String aText,String bText,String cText,String dText,String answer){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Question",question);
        hashMap.put("A_Text",aText);
        hashMap.put("B_Text",bText);
        hashMap.put("C_Text",cText);
        hashMap.put("D_Text",dText);
        hashMap.put("Currect_Ans",answer);
        firestore.collection(collection_tbls).add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(HomeActivity.this,"Success",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(HomeActivity.this,"Failed"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void datarealTimeGetListener(){
        firestore.collection(collection_tbls).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentChange> documentChangeList = queryDocumentSnapshots.getDocumentChanges();
//                questionModelsList.clear();
//                questionAdepter.notifyDataSetChanged();
                getAllData();
                /*for(DocumentChange docChange:documentChangeList){
                    QueryDocumentSnapshot documentReference = docChange.getDocument();
                    Question_Model questionmodel = new Question_Model();
                    questionmodel.setId(documentReference.getId());
                    questionmodel.setQuestion(""+documentReference.get("Question"));
                    questionmodel.setaOption(""+documentReference.get("A_Text"));
                    questionmodel.setbOption(""+documentReference.get("B_Text"));
                    questionmodel.setcOption(""+documentReference.get("C_Text"));
                    questionmodel.setdOption(""+documentReference.get("D_Text"));
                    questionmodel.setRightAns(""+documentReference.get("Currect_Ans"));

                    Toast.makeText(HomeActivity.this,"Success",Toast.LENGTH_SHORT).show();
                    questionModelsList.add(questionmodel);
                    questionAdepter.notifyDataSetChanged();
                    switch (docChange.getType()){
                        case ADDED:
                            QueryDocumentSnapshot documentReference = docChange.getDocument();
                            Question_Model questionmodel = new Question_Model();
                            questionmodel.setId(documentReference.getId());
                            questionmodel.setQuestion(""+documentReference.get("Question"));
                            questionmodel.setaOption(""+documentReference.get("A_Text"));
                            questionmodel.setbOption(""+documentReference.get("B_Text"));
                            questionmodel.setcOption(""+documentReference.get("C_Text"));
                            questionmodel.setdOption(""+documentReference.get("D_Text"));
                            questionmodel.setRightAns(""+documentReference.get("Currect_Ans"));

                            Toast.makeText(HomeActivity.this,"Success",Toast.LENGTH_SHORT).show();
                            questionModelsList.add(questionmodel);
                            questionAdepter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            QueryDocumentSnapshot mdocumentReference = docChange.getDocument();
                            Question_Model mquestionmodel = new Question_Model();
                            mquestionmodel.setId(mdocumentReference.getId());
                            mquestionmodel.setQuestion(""+mdocumentReference.get("Question"));
                            mquestionmodel.setaOption(""+mdocumentReference.get("A_Text"));
                            mquestionmodel.setbOption(""+mdocumentReference.get("B_Text"));
                            mquestionmodel.setcOption(""+mdocumentReference.get("C_Text"));
                            mquestionmodel.setdOption(""+mdocumentReference.get("D_Text"));
                            mquestionmodel.setRightAns(""+mdocumentReference.get("Currect_Ans"));

                            Toast.makeText(HomeActivity.this,"Success",Toast.LENGTH_SHORT).show();
                            questionModelsList.set(Integer.parseInt(homeBinding.idtextSimple2.getText().toString().trim()),mquestionmodel);
                            break;
                        case REMOVED:
                            QueryDocumentSnapshot rdocumentReference = docChange.getDocument();
                            Question_Model rquestionmodel = new Question_Model();
                            rquestionmodel.setId(rdocumentReference.getId());
                            rquestionmodel.setQuestion(""+rdocumentReference.get("Question"));
                            rquestionmodel.setaOption(""+rdocumentReference.get("A_Text"));
                            rquestionmodel.setbOption(""+rdocumentReference.get("B_Text"));
                            rquestionmodel.setcOption(""+rdocumentReference.get("C_Text"));
                            rquestionmodel.setdOption(""+rdocumentReference.get("D_Text"));
                            rquestionmodel.setRightAns(""+rdocumentReference.get("Currect_Ans"));

                            Toast.makeText(HomeActivity.this,"Success",Toast.LENGTH_SHORT).show();
                            break;
                            default:
                                break;
                    }
               }*/
            }
        });
    }
    public void interRecyclerView(){
        questionModelsList = new ArrayList<>();
        questionAdepter = new Question_Adepter(questionModelsList);
        questionAdepter.setListener(HomeActivity.this);
        homeBinding.recyclerViewID.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        homeBinding.recyclerViewID.setAdapter(questionAdepter);
    }

    @Override
    public void onItemClick(Question_Model question_model, int position) {
        homeBinding.idtextSimple.setText(question_model.getId());
        homeBinding.questionID.setText(question_model.getQuestion());
        homeBinding.optionAID.setText(question_model.getaOption());
        homeBinding.optionBID.setText(question_model.getbOption());
        homeBinding.optionCID.setText(question_model.getcOption());
        homeBinding.optionDID.setText(question_model.getdOption());
        homeBinding.currectAns.setText(question_model.getRightAns());
        homeBinding.idtextSimple2.setText(homeBinding.idtextSimple2.getText().toString().trim());
    }
    public void getAllData(){
        questionModelsList.clear();
        questionAdepter.notifyDataSetChanged();
        firestore.collection(collection_tbls).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                for(DocumentSnapshot documentReference : snapshots){
                    Question_Model questionmodel = new Question_Model();
                    questionmodel.setId(documentReference.getId());
                    questionmodel.setQuestion(""+documentReference.get("Question"));
                    questionmodel.setaOption(""+documentReference.get("A_Text"));
                    questionmodel.setbOption(""+documentReference.get("B_Text"));
                    questionmodel.setcOption(""+documentReference.get("C_Text"));
                    questionmodel.setdOption(""+documentReference.get("D_Text"));
                    questionmodel.setRightAns(""+documentReference.get("Currect_Ans"));

                    Toast.makeText(HomeActivity.this,"Success",Toast.LENGTH_SHORT).show();
                    questionModelsList.add(questionmodel);
                    questionAdepter.notifyDataSetChanged();

                }
            }
        });
    }
}
