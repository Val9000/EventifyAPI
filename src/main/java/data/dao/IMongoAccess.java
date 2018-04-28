package data.dao;

import java.util.Collection;
import java.util.List;
import org.bson.conversions.Bson;

interface IMongoAccess<T> {

        String add(T entity);

	T getOneFilter(Bson filterQuery, Bson _projection);
        
        List<T> getAllFilter(Bson filterQuery, Bson _projection);
	
	List<T> getLimitedFilteredResult(Bson filter, int limit);
	
	void remove(Bson filterQuery);
	
	void removeMany(Bson filterQuery);
        
        void update(Bson filterQuery, Bson updateObject);
}
