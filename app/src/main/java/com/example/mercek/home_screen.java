package com.example.mercek;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.List;

public class home_screen extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;

    private FirestoreRecyclerAdapter adapter;
    FirebaseFirestore db;
    private ImageButton addSurveyImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        addSurveyImageButton = findViewById(R.id.addSurveyImageButton);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.firestore_list);

        addSurveyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home_screen.this, create_survey.class));
            }
        });

        Query query = firebaseFirestore.collection("surveys");
        FirestoreRecyclerOptions<SurveyModel> options = new FirestoreRecyclerOptions.Builder<SurveyModel>()
                .setQuery(query, SurveyModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<SurveyModel, home_screen.SurveyViewHolder>(options) {
            @NonNull
            @Override
            public home_screen.SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_single, parent, false);
                return new home_screen.SurveyViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull home_screen.SurveyViewHolder holder, int position, @NonNull SurveyModel model) {

                DocumentReference document = firebaseFirestore.collection("users").document(model.getCreatorId());
                document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        holder.surveyCreatorTextView.setText(documentSnapshot.getString("userName"));
                    }
                });

                //String
                holder.surveyNameTextView.setText(model.getSurveyName());
                
                holder.survey_single.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(home_screen.this, survey_viewer.class);
                        intent.putExtra("surveyName", model.getSurveyName());
                        intent.putExtra("surveyId", model.getSurveyId());
                        intent.putExtra("creatorId", model.getCreatorId());
                        startActivity(intent);
                    }
                });

            }
            
        };

        mFirestoreList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mFirestoreList.setLayoutManager(mLayoutManager);
        mFirestoreList.setAdapter(adapter);
    }

    private class SurveyViewHolder extends RecyclerView.ViewHolder{

        private TextView surveyNameTextView, surveyCreatorTextView;
        private androidx.percentlayout.widget.PercentRelativeLayout survey_single;

        public SurveyViewHolder(@NonNull View itemView){
            super(itemView);
            
            survey_single = itemView.findViewById(R.id.survey_single);
            surveyNameTextView = itemView.findViewById(R.id.surveyNameTextView);
            surveyCreatorTextView = itemView.findViewById(R.id.surveyCreatorTextView);
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
}