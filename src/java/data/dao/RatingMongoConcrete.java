package data.dao;
import data.models.Rating;

public class RatingMongoConcrete extends MongoConcrete<Rating> {
    private static RatingMongoConcrete rmc = new RatingMongoConcrete();
    private RatingMongoConcrete() {
        super("Ratings");
    }
    public static RatingMongoConcrete getInstance(){
        return rmc;
    }
}