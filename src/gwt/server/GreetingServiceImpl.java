package gwt.server;

import gwt.client.GreetingService;
import gwt.shared.Book;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;

import java.util.ArrayList;
import java.util.Date;



/**
 * The server-side implementation of the RPC service.
 */

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	
	DatastoreService datastore;
	
	// create a new book on server
	public String createBook(Book b) throws IllegalArgumentException{
		
		try{
            if (datastore == null) {
                datastore = DatastoreServiceFactory.getDatastoreService();
            }

            Entity book = new Entity("Book");
          //String author, String title, String type, String price, String pages, String ISBN
            book.setProperty("bookAuthor",b.getBookAuthor());
            book.setProperty("bookTitle", b.getBookTitle());
            book.setProperty("bookType", b.getBookType());
            book.setProperty("bookPrice",b.getBookPrice());
            book.setProperty("bookPages",b.getBookPages());
            book.setProperty("bookISBN", b.getBookISBN());
            
            book.setProperty("dateUpdated", new Date());

		
			datastore.put(book);
			return "Dodałeś książke o tytule, "+b.getBookTitle()+", autora: "+b.getBookAuthor();
		}
		catch(Exception e){
            e.printStackTrace();
            return e.toString();
		}
		
		
	}
	
	// read a new book from server
	public ArrayList<Book> readBook(String publishing) throws IllegalArgumentException{

		try{
            if (datastore == null) {
                datastore = DatastoreServiceFactory.getDatastoreService();
            }
            
			ArrayList<Book> books = new ArrayList<Book>();

			// queries to fill in the arraylist
			Query q = new Query("Book");

			q.addSort("bookTitle");
			
			PreparedQuery pq = datastore.prepare(q);

			for (Entity result : pq.asIterable()) {
				
				Book b = new Book((String)result.getProperty("bookAuthor"), (String)result.getProperty("bookTitle"),
						(String)result.getProperty("bookType"), (String)result.getProperty("bookPrice"),
						(String)result.getProperty("bookPages"), (String)result.getProperty("bookISBN"),
						(String)KeyFactory.keyToString(result.getKey()));
				books.add(b);
			}


			return books;
		}
		catch(Exception e){
            e.printStackTrace();
            return null;
		}
	}
	
	// update a pet on server with a unique petID (keyString)
	public String updateBook(String keyString, Book b) throws IllegalArgumentException{
		try{
            if (datastore == null) {
                datastore = DatastoreServiceFactory.getDatastoreService();
            }

            // convert the key string to Key
            Key bookKey = KeyFactory.stringToKey(keyString);
            
            // then, get the entity from that key
            Entity bookEntity = datastore.get(bookKey);
            
            bookEntity.setProperty("bookAuthor",b.getBookAuthor());
            bookEntity.setProperty("bookTitle", b.getBookTitle());
            bookEntity.setProperty("bookType", b.getBookType());
            bookEntity.setProperty("bookPrice",b.getBookPrice());
            bookEntity.setProperty("bookPages",b.getBookPages());
            bookEntity.setProperty("bookISBN", b.getBookISBN());
            
            bookEntity.setProperty("dateUpdated", new Date());
            
            datastore.put(bookEntity);
            
    		return "Zaktualizowałeś książkę.";

		}
		catch(Exception e){
            e.printStackTrace();
            return e.toString();
		}
		
	}
	
	// delete the pet with a petID (keyString)
	public String deleteBook(String keyString) throws IllegalArgumentException{
		
		try{
            if (datastore == null) {
                datastore = DatastoreServiceFactory.getDatastoreService();
            }            

            Key bookKey = KeyFactory.stringToKey(keyString);

            datastore.delete(bookKey);
    		return "Usunąłeś książkę.";

		}
		catch(Exception e){
            e.printStackTrace();
            return e.toString();
		}
	}

	@Override
	public String greetServer(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
