package com.example.mercek;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class create_survey extends AppCompatActivity {

    Button createButton;
    ImageButton createQuestionButton;
    EditText surveyNameEditText;
    ListView listView;
    TextView creatorTextView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String uniqueID = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);
        surveyNameEditText = findViewById(R.id.surveyNameEditText);
        createButton = findViewById(R.id.createButton);
        createQuestionButton = findViewById(R.id.createQuestionButton);
        listView = findViewById(R.id.listView);
        creatorTextView = findViewById(R.id.creatorTextView);


        Array[] array = new Array[] {};

        // Initializing a new String Array
        String[] fruits = new String[] {"question"};

        // Create a List from String Array elements
        final List<String> fruits_list = new ArrayList<String>(Arrays.asList(fruits));

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, R.layout.question_list_item, R.id.questionEditText, fruits_list){

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View item = layoutInflater.inflate(R.layout.question_list_item, parent, false);
                EditText questionEditText = item.findViewById(R.id.questionEditText);
                ImageButton optionDropDownImageButton = item.findViewById(R.id.optionDropDownImageButton);
                ImageButton createOptionImageButton = item.findViewById(R.id.createOptionImageButton);
                ListView optionListView = item.findViewById(R.id.optionListView);

                optionDropDownImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        optionListView.setVisibility(View.VISIBLE);
                    }
                });

/*
                // Create an ArrayAdapter from List
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                        (this,R.layout.option_list_item, R.id.optionEditText, option_list){

                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View option_item = layoutInflater.inflate(R.layout.option_list_item, parent, false);

                        return super.getView(position, convertView, parent);
                    }

                };*/

                createOptionImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                return super.getView(position, convertView, parent);
            }

        };

        // Initializing a new String Array
        String[] options = new String[] {"option", "option2", "option3"};

        // Create a List from String Array elements
        final List<String> option_list = new ArrayList<String>(Arrays.asList(options));

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String surveyName = surveyNameEditText.getText().toString().trim();
                String creatorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String surveyId = UUID.randomUUID().toString();
                String questionId = UUID.randomUUID().toString();
                //Create Survey
                Map<String, Object> survey = new HashMap<>();
                survey.put("surveyName", surveyName);
                survey.put("creatorId", creatorId);
                survey.put("isPublished", false);
                survey.put("surveyId", surveyId);



                db.collection("surveys").document(surveyId).set(survey).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        for (int questionIndex = 0; questionIndex < fruits_list.toArray().length; questionIndex++) {
                            String questionId = UUID.randomUUID().toString();

                            Map<String, Object> questionMap = new HashMap<>();
                            questionMap.put("questionId", questionId);
                            questionMap.put("question", fruits_list.get(questionIndex));

                            db.collection("surveys").document(surveyId).collection("questions").document(questionId)
                                    .set(questionMap);
                            for (int optionIndex = 0; optionIndex < option_list.toArray().length; optionIndex++){
                                String optionId = UUID.randomUUID().toString();

                                Map<String, Object> optionMap = new HashMap<>();
                                optionMap.put("option", option_list.get(optionIndex));
                                optionMap.put("optionId", optionId);

                                db.collection("surveys").document(surveyId).collection("questions").document(questionId).collection("options").document(optionId)
                                        .set(optionMap);
                            }

                        }
                    }
                });
            }
        });

        // DataBind ListView with items from ArrayAdapter
        listView.setAdapter(arrayAdapter);

        createQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add new Items to List
                fruits_list.add("Soru");
                /*
                    notifyDataSetChanged ()
                        Notifies the attached observers that the underlying
                        data has been changed and any View reflecting the
                        data set should refresh itself.
                 */
                arrayAdapter.notifyDataSetChanged();
            }
        });
/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                creatorTextView.setText(selectedItem);
            }
        });*/
    }
}