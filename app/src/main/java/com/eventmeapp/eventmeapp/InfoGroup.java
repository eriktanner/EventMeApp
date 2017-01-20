package com.eventmeapp.eventmeapp;

import java.util.ArrayList;

/**
 * Created by erikf on 1/14/2017.
 */

public class InfoGroup {

    private static int group_id_counter = 1; //TODO Retrieve From DataBase

    private int groupID;
    private String groupName;
    private ArrayList<InfoUser> groupMembers = new ArrayList();

    public InfoGroup(String groupName) {
        groupID = group_id_counter;
        group_id_counter++;
        this.groupName = groupName;
    }

    public void addMember(InfoUser user) {groupMembers.add(user);}
    public void removeMember(InfoUser user) {groupMembers.remove(user);}
}
