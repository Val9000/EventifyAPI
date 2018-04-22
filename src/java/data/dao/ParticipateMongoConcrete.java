package data.dao;
import data.models.Participation;

public class ParticipateMongoConcrete extends MongoConcrete<Participation> {
    public ParticipateMongoConcrete() {
        super("Participations");
    }
}
