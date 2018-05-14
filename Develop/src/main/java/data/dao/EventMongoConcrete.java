package data.dao;
import data.models.Event;

public class EventMongoConcrete extends MongoConcrete<Event> {
    private static EventMongoConcrete emc = null;
    
    private EventMongoConcrete() {
        super("Events");
    }
   
    public static EventMongoConcrete getInstance(){
        if(emc == null) emc = new EventMongoConcrete();
        return emc;
    }
}