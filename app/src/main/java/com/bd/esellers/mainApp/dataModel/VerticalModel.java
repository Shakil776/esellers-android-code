package com.bd.esellers.mainApp.dataModel;

import java.io.Serializable;
import java.util.ArrayList;

public class VerticalModel extends RecyclerViewItem implements Serializable {
    private ArrayList<HorizontalModel>horizontalModels;

    public VerticalModel(ArrayList<HorizontalModel> horizontalModels) {
        this.horizontalModels = horizontalModels;
    }

    public ArrayList<HorizontalModel> getHorizontalModels() {
        return horizontalModels;
    }

    public void setHorizontalModels(ArrayList<HorizontalModel> horizontalModels) {
        this.horizontalModels = horizontalModels;
    }


}
