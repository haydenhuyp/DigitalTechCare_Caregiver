package com.techcare.techcare;

import android.view.View;

import androidx.annotation.NonNull;

public class GridCell {
    private int _id;
    private String title;
    private String icon;
    private String actionParameter;
    private String action;

    public GridCell(int _id, String title, String icon, String actionParameter, String action) {
        this._id = _id;
        this.title = title;
        this.icon = icon;
        this.actionParameter = actionParameter;
        this.action = action;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getActionParameter() {
        return actionParameter;
    }

    public void setActionParameter(String actionParameter) {
        this.actionParameter = actionParameter;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @NonNull
    @Override
    public String toString() {
        return "GridCell: ID:" + _id + ", Title: " + title + ", Icon: " + icon + ", Action: "+ action +"ActionParameter: " + actionParameter + "\n";
    }
}
