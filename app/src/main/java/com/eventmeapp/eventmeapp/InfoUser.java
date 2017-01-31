package com.eventmeapp.eventmeapp;

import java.util.ArrayList;

/**
 * Created by erikf on 1/14/2017.
 */

public class InfoUser {

    private static int id_counter = 1; //TODO Retrieve From DataBase

    private int userID;
    private String firstName, lastName, email;
    private ArrayList<InfoGroup> memberOfGroup = new ArrayList();

    public InfoUser(String first, String last, String email) {
        userID = id_counter;
        id_counter++;
        firstName = first;
        lastName = last;
        this.email = email;
    }



}
