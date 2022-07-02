package com.example.helloworld;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

public class User {
    private String email;
    private String userName;
    private List<String> friendList;

    public User (){}
    public User(String userName, String email)
    {
        this.userName = userName;
        this.email = email;
        this.friendList = new LinkedList<String>();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getFriendList() {
        return friendList;
    }

    public void addFriend(User friend) {
        this.friendList.add(friend.getUserName());
    }

    public void deleteFriend(User friend) {
        if(this.friendList.contains(friend.getUserName()))

    }
}
