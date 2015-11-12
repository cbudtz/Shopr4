package com.nexb.shopr4.dataModel;

import java.util.ArrayList;

/**
 * @author Christian
 */
public class User {
    private String UserID = "";
    private String UserName = "New user";

    private ArrayList<String> ownLists = new ArrayList<>();
    private ArrayList<ForeignUserlist> foreignLists = new ArrayList<>();
    private String activeList = "Shoplist4";

    public User(){
        //For Testing puposes
        ownLists.add("Shoplist3");
        ownLists.add("Shoplist4");
    }


    public String getUserID() {
        return UserID;
    }
    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }

    public ArrayList<String> getOwnLists() {
        return ownLists;
    }
    public void setOwnLists(ArrayList<String> ownLists) {
        this.ownLists = ownLists;
    }

    public ArrayList<ForeignUserlist> getForeignLists() {
        return foreignLists;
    }
    public void setForeignLists(ArrayList<ForeignUserlist> foreignLists) {        this.foreignLists = foreignLists;    }

    public String getActiveList() {       return activeList;    }
    public void setActiveList(String activeList) {        this.activeList = activeList;    }
}
