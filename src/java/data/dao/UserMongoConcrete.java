package data.dao;
import data.models.User;

public class UserMongoConcrete extends MongoConcrete<User> {
    private static UserMongoConcrete umc = new UserMongoConcrete();
    private UserMongoConcrete() {
        super("Users");
    }
    public static UserMongoConcrete getInstance(){
        return umc;
    }
}