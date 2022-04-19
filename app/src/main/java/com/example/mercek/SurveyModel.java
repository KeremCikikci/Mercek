package com.example.mercek;

public class SurveyModel {

    private String surveyName;
    private String creatorId;
    private String surveyId;

    private SurveyModel(){}

    private SurveyModel(String surveyName, String creatorId, String surveyId){
        this.surveyName = surveyName;
        this.creatorId = creatorId;
        this.surveyId = surveyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }
}
