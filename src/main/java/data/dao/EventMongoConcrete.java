package data.dao;
import data.models.Event;

public class EventMongoConcrete extends MongoConcrete<Event> {
    private static EventMongoConcrete emc = new EventMongoConcrete();
    private EventMongoConcrete() {
        super("Events");
    }
    public static EventMongoConcrete getInstance(){
        return emc;
    }
}