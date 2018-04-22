package data.dao;
import data.models.Event;

public class EventMongoConcrete extends MongoConcrete<Event> {

    public EventMongoConcrete() {
        super("Events");
    }
}
