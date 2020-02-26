package com.shekhfarid.myquizapp.home.model;

public class Question_Model {
    private String id;
    private String question;
    private String aOption;
    private String bOption;
    private String cOption;
    private String dOption;
    private String rightAns;

    public Question_Model() {

    }

    public Question_Model(String id, String question, String aOption, String bOption, String cOption, String dOption, String rightAns) {
        this.id = id;
        this.question = question;
        this.aOption = aOption;
        this.bOption = bOption;
        this.cOption = cOption;
        this.dOption = dOption;
        this.rightAns = rightAns;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getaOption() {
        return aOption;
    }

    public void setaOption(String aOption) {
        this.aOption = aOption;
    }

    public String getbOption() {
        return bOption;
    }

    public void setbOption(String bOption) {
        this.bOption = bOption;
    }

    public String getcOption() {
        return cOption;
    }

    public void setcOption(String cOption) {
        this.cOption = cOption;
    }

    public String getdOption() {
        return dOption;
    }

    public void setdOption(String dOption) {
        this.dOption = dOption;
    }

    public String getRightAns() {
        return rightAns;
    }

    public void setRightAns(String rightAns) {
        this.rightAns = rightAns;
    }

    @Override
    public String toString() {
        return "Question_Model{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", aOption='" + aOption + '\'' +
                ", bOption='" + bOption + '\'' +
                ", cOption='" + cOption + '\'' +
                ", dOption='" + dOption + '\'' +
                ", rightAns='" + rightAns + '\'' +
                '}';
    }
}
