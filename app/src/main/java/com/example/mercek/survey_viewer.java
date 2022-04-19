package com.example.mercek;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class survey_viewer extends AppCompatActivity {

    private TextView surveyNameTextView, questionTextView, questionHolderTextView;
    private ListView questionListView;
    private RecyclerView mFirestoreList;

    private FirebaseFirestore firebaseFirestore;

    private FirestoreRecyclerAdapter adapter;

    public static String questionId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_viewer);
        firebaseFirestore = FirebaseFirestore.getInstance();

        mFirestoreList = findViewById(R.id.firestore_list);
        surveyNameTextView = findViewById(R.id.surveyNameTextView);
        questionTextView = findViewById(R.id.questionTextView);
        questionHolderTextView = findViewById(R.id.questionHolderTextView);
        questionListView = findViewById(R.id.questionListView);

        questionListView.setAdapter(new yourAdapter(this, new String[] { "data1", "data2" }));

        Bundle extras = getIntent().getExtras();

        String surveyName = extras.getString("surveyName");
        String surveyId = extras.getString("surveyId");
        //String creatorId = extras.getString("creatorId");

        surveyNameTextView.setText(surveyName);

        int questionNumber = 0;



        questionId = String.valueOf(a(firebaseFirestore, surveyId, questionNumber, adapter));

        Log.d("test1", questionId);

        //Şıkların çekilmesi
        Query query = firebaseFirestore.collection("surveys").document(surveyId).collection("questions").document(questionId).collection("options");
        FirestoreRecyclerOptions<OptionModel> options = new FirestoreRecyclerOptions.Builder<OptionModel>()
                .setQuery(query, OptionModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<OptionModel, survey_viewer.OptionViewHolder>(options) {
            @NonNull
            @Override
            public survey_viewer.OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_single, parent, false);
                return new survey_viewer.OptionViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull survey_viewer.OptionViewHolder holder, int position, @NonNull OptionModel model) {

                DocumentReference document = firebaseFirestore.collection("surveys").document(surveyId).collection("questions").document(questionId).collection("options").document(model.getOptionId());
                document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        holder.optionRadioButton.setText(documentSnapshot.getString("option"));
                    }
                });
            }
        };

        mFirestoreList.setHasFixedSize(true);
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        //mFirestoreList.setLayoutManager(mLayoutManager);
        mFirestoreList.setAdapter(adapter);


    }


    private class OptionViewHolder extends RecyclerView.ViewHolder{

        private RadioButton optionRadioButton;

        public OptionViewHolder(@NonNull View itemView){
            super(itemView);

            optionRadioButton = itemView.findViewById(R.id.optionRadioButton);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private String a(FirebaseFirestore firebaseFirestore, String surveyId, int questionNumber, FirestoreRecyclerAdapter adapter){
        List<String> list = new ArrayList<>();

        firebaseFirestore.collection("surveys").document(surveyId).collection("questions")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                questionId = list.get(questionNumber);
                Log.d("test2", questionId);
                //Sorunun çekilmesi
                DocumentReference document = firebaseFirestore.collection("surveys").document(surveyId).collection("questions").document(questionId);
                document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        questionTextView.setText(documentSnapshot.getString("question"));
                    }
                });
            }
        });



        return questionId;
    }
}
