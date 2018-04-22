package data.dao;
import data.models.Participation;

public class ParticipateMongoConcrete extends MongoConcrete<Participation> {
    private static ParticipateMongoConcrete pmc = new ParticipateMongoConcrete();
    private ParticipateMongoConcrete() {
        super("Participations");
    }
    public static ParticipateMongoConcrete getInstance(){
        return pmc;
    }
}