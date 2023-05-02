package Bean;

public class UserItem {
    private int userId;
    private String userNickname;

    public UserItem(int userId,String userNickname)
    {
        this.userId=userId;
        this.userNickname=userNickname;
    }
    public int getUserId() {
        return userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
