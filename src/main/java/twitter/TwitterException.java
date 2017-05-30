package twitter;

import java.io.IOException;
import java.util.List;

import com.github.scribejava.core.model.*;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class TwitterException extends RuntimeException {

    private static final long serialVersionUID = -3227149020223036977L;

    private List<?> errors = null; 
    
    public TwitterException (String message) {
        super(message);
    }
    
    public TwitterException (String message, List<?> errors) {
        super(message);
        this.errors = errors;
    }
    
    public List<?> getErrors() {
        return errors;
    }
    
    public static TwitterException create (Response response) {
        DBObject body = null;
		try {
			body = (DBObject)JSON.parse(response.getBody());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        BasicDBList errors = (BasicDBList)body.get("errors");
        DBObject firstError = (DBObject)errors.get(0);
        int code = (int)firstError.get("code");
        String message = (String)firstError.get("message");
        switch (code) {
        case 34: return new PageNotExistException (message, errors);
        case 88: return new RateLimitExceededException (message, errors);
        default: return new TwitterException (message, errors);
        }
    }
}
