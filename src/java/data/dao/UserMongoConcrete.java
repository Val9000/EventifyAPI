package data.dao;
import data.models.User;

public class UserMongoConcrete extends MongoConcrete<User> {
    public UserMongoConcrete() {
        super("Users");
    }
}
