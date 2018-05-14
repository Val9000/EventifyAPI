package data.dao;
import data.models.User;

public class UserMongoConcrete extends MongoConcrete<User> {
    private static UserMongoConcrete umc = null;
    
    private UserMongoConcrete() {
        super("Users");
    }
    public static UserMongoConcrete getInstance(){
        if(umc == null) umc = new UserMongoConcrete();
        return umc;
    }
}