package live.videosdk.rtc.android.java;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

public class User {
    private String email;
    private String userName;
    private LinkedList<String> friendList;

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
        return this.email;
    }

    public String getUserName() {
        return this.userName;
    }

    public LinkedList<String> getFriendList() {
        return friendList;
    }

    public void addFriend(User friend) {
        this.friendList.add(friend.getUserName());
    }

    public void deleteFriend(User friend) {
        this.friendList.remove(friend.getUserName());
    }
}
