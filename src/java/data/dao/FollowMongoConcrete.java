package data.dao;
import data.models.Follow;

public class FollowMongoConcrete extends MongoConcrete<Follow> {
    public FollowMongoConcrete() {
        super("Follows");
    }
}
