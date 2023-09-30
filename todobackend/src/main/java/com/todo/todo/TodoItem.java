package com.todo.todo;

public class TodoItem {


    private int id;
    private String textcontent;
    private boolean finished;

    public TodoItem() {
       
    }

    public TodoItem(int id, String textcontent, boolean finished) {
        this.id = id;
        this.textcontent = textcontent;
        this.finished = finished;
    }

    public int getId() {
        return this.id;
    }


    public void setId(int id){
        this.id = id;
    }

    public String getTextcontent() {
        return this.textcontent;
    }

    public void setTextcontent(String textcontent){
        this.textcontent = textcontent;
    }
    
    public boolean getFinished() {
        return this.finished;
    }

    public void setFinished(boolean finished){
        this.finished = finished;
    }
}
