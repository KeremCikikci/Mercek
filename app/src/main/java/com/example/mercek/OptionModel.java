package com.example.mercek;

public class OptionModel {

    private String option;
    private String optionId;

    private OptionModel(){}

    private OptionModel(String option, String optionId){
        this.option = option;
        this.optionId = optionId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }
}
