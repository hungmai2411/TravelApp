package com.travelappproject.model.hotel;

public class Sort {
    private int name;
    private boolean isChecked;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Sort(int name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }
}
