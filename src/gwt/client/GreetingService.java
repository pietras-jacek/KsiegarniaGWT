package gwt.client;

import gwt.shared.Book;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	
	String createBook(Book b) throws IllegalArgumentException;
	ArrayList<Book> readBook(String publishing) throws IllegalArgumentException;

	String updateBook(String keyString, Book b) throws IllegalArgumentException;
	
	String deleteBook(String keyString) throws IllegalArgumentException;
	
	
}
