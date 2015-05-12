package gwt.client;

import gwt.shared.Book;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void createBook(Book b, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void readBook(String publishing, AsyncCallback<ArrayList<Book>> callback)
			throws IllegalArgumentException;

	void updateBook(String KeyString, Book b, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void deleteBook(String keyString, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void greetServer(String name, AsyncCallback<String> callback);
}
