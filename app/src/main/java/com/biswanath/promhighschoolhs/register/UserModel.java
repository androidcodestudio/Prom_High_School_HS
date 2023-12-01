package com.biswanath.promhighschoolhs.register;

public class UserModel {
    String  profilePic = "R.drawable.user"
            , userName
            , userMail
            , userId
            , userPassword = "null"
            , recentMessage
            , about
            , token
            ,image
            ,name
            ,promCoin
            ,roll
            ,section
            ,studentClass
            ,phone
            ,myUid;
    long  recentMsgTime;

    public UserModel(String profilePic, String userName, String userMail, String userId, String userPassword, String about) {
        this.profilePic = profilePic;
        this.userName = userName;
        this.userMail = userMail;
        this.userId = userId;
        this.userPassword = userPassword;
        this.about = about;
    }

    // For storing in DB
    public UserModel(String userName, String userMail, String userPassword, String profilePic, String about,String image, String name, String promCoin, String roll, String section,String studentClass, String phone, String myUid){

        this.profilePic = profilePic;
        this.userName = userName;
        this.userMail = userMail;
        this.userPassword = userPassword;
        this.about = about;
        this.image = image;
        this.name = name;
        this.promCoin = promCoin;
        this.roll = roll;
        this.section = section;
        this.studentClass = studentClass;
        this.phone = phone;
        this.myUid = myUid;

    }

    public UserModel() {
    }

    // for displaying in chats list and search list
    public UserModel(String userName, String userMail, String profilePic) {
        this.userName = userName;
        this.userMail = userMail;
        this.profilePic = profilePic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAbout() {
        return about;
    }

    public long getRecentMsgTime() {
        return recentMsgTime;
    }

    public void setRecentMsgTime(long recentMsgTime) {
        this.recentMsgTime = recentMsgTime;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


    public String getRecentMessage() {
        return recentMessage;
    }

    public void setRecentMessage(String recentMessage) {
        this.recentMessage = recentMessage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPromCoin() {
        return promCoin;
    }

    public void setPromCoin(String promCoin) {
        this.promCoin = promCoin;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMyUid() {
        return myUid;
    }

    public void setMyUid(String myUid) {
        this.myUid = myUid;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }
}
