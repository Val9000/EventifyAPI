package data.dao;

import com.mongodb.client.result.UpdateResult;
import java.util.List;
import org.bson.conversions.Bson;

interface IMongoAccess<T> {

        String add(T entity);

	T getOneFilter(Bson filterQuery, Bson _projection);
        
        <T> T getOneFilter(Bson filterQuery, Bson _projection, Class<T> clazz);
        
        List<T> getAllFilter(Bson filterQuery, Bson _projection);
       
        <T> List<T> getAllFilter(Bson filterExpression, Bson _projection, Class<T> clazz);
        
	List<T> getLimitedFilteredResult(Bson filter, int limit);
	
	void remove(Bson filterQuery);
	
	void removeMany(Bson filterQuery);
        
        UpdateResult update(Bson filterQuery, Bson updateObject);
        
        UpdateResult updateMany(Bson filterQuery, Bson updateObject);
        
}
