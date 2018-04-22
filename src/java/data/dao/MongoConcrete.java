package data.dao;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import static com.mongodb.client.model.Projections.excludeId;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import data.models.User;
import data.models.UserState;
import data.models.UserType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import static javax.ws.rs.client.Entity.json;
import static org.hibernate.validator.internal.engine.messageinterpolation.el.RootResolver.FORMATTER;

public abstract class MongoConcrete<T> implements IMongoAccess<T> {

    private static final MongoClient myClient = new MongoClient(new MongoClientURI("mongodb://val:val123@ds249079.mlab.com:49079/eventifydb"));
    private static MongoDatabase database = myClient.getDatabase("eventifydb");
    private MongoCollection<Document> collection;

    private Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public MongoConcrete(String collectionName) {
        collection = database.getCollection(collectionName);
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public String add(T entity) {
        String id = "";

        JSONObject entityObject = new JSONObject(entity);
        Document x = Document.parse(entityObject.toString());
        collection.insertOne(x);
        ObjectId u = (ObjectId) x.get("_id");

        System.out.println(u.toString());
        id = u.toString();
        return id;
    }

    @Override
    public void remove(Bson filterExpression) {
        collection.findOneAndDelete(filterExpression);

    }

    @Override
    public T getOneFilter(Bson filterExpression) {
        T entityToReturn;
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        entityToReturn = gson.fromJson(collection.find(filterExpression).projection(fields(Projections.excludeId())).first().toJson(), entityClass);
        if (entityToReturn == null) return null; // means couldn't find anything
        return entityToReturn;
    }
    
    @Override
    public List<T> getAllFilter(Bson filterExpression) {
        List<T> listToReturn = new ArrayList<>(); 
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        FindIterable<Document> col = collection.find(filterExpression).projection(fields(Projections.excludeId()));
        for(Document doc : col) listToReturn.add(gson.fromJson(doc.toJson(), entityClass));
        return listToReturn;
    }

    // maps document results into a list of objects
    @Override
    public List<T> getLimitedFilteredResult(Bson filter, int limit) {
        Gson gson = new GsonBuilder().create();
        List<T> innerList = new ArrayList<>();
        collection.find(filter).limit(limit).forEach((Block<Document>) document -> {
            innerList.add(gson.fromJson(document.toJson(), entityClass));
        });
        return innerList;

    }

    @Override
    public void removeMany(Bson filterQuery) {
        collection.deleteMany(filterQuery);
    }

    @Override
    public void update(Bson filterQuery, Bson updateObject) {
        UpdateResult res = collection.updateOne(filterQuery, updateObject);
        System.err.println(res);
    }

    public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> { // evtl. ins extra file -> Util rein

        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

}

// TESTING Parser fo GSN
/*
User usa1 = new User("Valon", "Berisa", LocalDate.now(), "valibaer", "val@val.com", "VerysecurePassword", "mypic.com/x.png");
usa1.setUID("5adc6cc606bc630670a23824");
String test = gson.toJson(usa1);
T dure = gson2.fromJson(test, entityClass);

Gson gson2 = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String date = json.getAsString();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        return LocalDate.parse(date, formatter);
    }
}).create();
*/
