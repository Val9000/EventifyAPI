package data.dao;

import Util.InstantTypeConverter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import static com.mongodb.client.model.Projections.fields;
import java.time.Instant;

public abstract class MongoConcrete<T> implements IMongoAccess<T> {

    private static final MongoClient myClient = new MongoClient(new MongoClientURI("mongodb://val:val123@ds249079.mlab.com:49079/eventifydb"));
    private static final MongoDatabase database = myClient.getDatabase("eventifydb");
    private final MongoCollection<Document> collection;
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(Instant.class, new InstantTypeConverter())
        .create();

    private final Class<T> entityClass;

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
    public T getOneFilter(Bson filterExpression, Bson _projection) {
        T entityToReturn;
        Gson custom_gson  = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantTypeConverter()).create();
        Document first = collection.find(filterExpression).projection(fields(Projections.excludeId(), _projection)).first();
        if(first == null) return null; // means couldn't find anything
        entityToReturn = custom_gson.fromJson(first.toJson(), entityClass);
        return entityToReturn;
    }
    
    @Override
    public <T> T getOneFilter(Bson filterExpression, Bson _projection, Class<T> clazz) {
        T entityToReturn;
        Gson custom_gson  = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantTypeConverter()).create();
        Document first = collection.find(filterExpression).projection(fields(Projections.excludeId(), _projection)).first();
        if(first == null) return null; // means couldn't find anything
        entityToReturn = custom_gson.fromJson(first.toJson(), clazz);
        return entityToReturn;
    }
    
    
    @Override
    public <T> List<T> getAllFilter(Bson filterExpression, Bson _projection, Class<T> clazz) {
        List<T> listToReturn = new ArrayList<>(); 
        FindIterable<Document> col = collection.find(filterExpression).projection(fields(Projections.excludeId(), _projection));
        for(Document doc : col) listToReturn.add(gson.fromJson(doc.toJson(), clazz));
        if(listToReturn.isEmpty()) return null;
        return listToReturn;
    }
    
    @Override
    public List<T> getAllFilter(Bson filterExpression, Bson _projection) {
        List<T> listToReturn = new ArrayList<>(); 
        FindIterable<Document> col = collection.find(filterExpression).projection(fields(Projections.excludeId(), _projection));
        for(Document doc : col) listToReturn.add(gson.fromJson(doc.toJson(), entityClass));
        if(listToReturn.isEmpty()) return null;
        return listToReturn;
    }

    // maps document results into a list of objects
    @Override
    public List<T> getLimitedFilteredResult(Bson filter, int limit) {
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
    public UpdateResult update(Bson filterQuery, Bson updateObject) {
        UpdateResult res = collection.updateOne(filterQuery, updateObject);
        return res;
    }

}
