package com.ismeralda_fabio.worldcityguide.model;


import java.util.List;

/**
 * Created by comsol on 11/2/2015.
 */
public class AutoCompletePlace {

    List<Prediction> predictions;

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }
}
