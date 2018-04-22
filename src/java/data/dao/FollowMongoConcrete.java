package data.dao;
import data.models.Follow;

public class FollowMongoConcrete extends MongoConcrete<Follow> {
    private static FollowMongoConcrete fmc = new FollowMongoConcrete();
    private FollowMongoConcrete() {
        super("Follows");
    }
    public static FollowMongoConcrete getInstance(){
        return fmc;
    }
}