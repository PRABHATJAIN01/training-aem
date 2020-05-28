package com.training.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables= Resource.class)
public class UserHobbies {

    @Inject
    @Default(values = {"Swimming", "Dancing", "Aerobics", "Gym"})
    private String[] hobbiesList;

    public String[] getHobbiesList() {
        return hobbiesList;
    }

    public void setHobbiesList(String[] hobbiesList) {
        this.hobbiesList = hobbiesList;
    }
}
