package data.dao;
import data.models.Rating;

public class RatingMongoConcrete extends MongoConcrete<Rating> {
    public RatingMongoConcrete() {
        super("Ratings");
    }
}
