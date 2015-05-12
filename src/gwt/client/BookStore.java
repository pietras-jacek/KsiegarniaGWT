package gwt.client;

import java.util.ArrayList;

import gwt.client.BookStore.newBookDialogBox;
import gwt.shared.Book;
import gwt.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BookStore implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	/* Initialize class parameters */
	private DialogBox dialogBox;
	private HTML serverResponseLabel;
	private Button closeButton;
	private Label statusLabel;
	private FlexTable booksFlexTable;
	private Button refreshButton;
	private Button openNewBookDialogButton;
	
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		// define controls
		final VerticalPanel mainPanel = new VerticalPanel();
		final HorizontalPanel ownerPanel = new HorizontalPanel();
		final Label lastUpdatedLabel = new Label();

		refreshButton = new Button("Odśwież listę książek");

		openNewBookDialogButton = new Button("Dodaj książkę");

		statusLabel = new Label();
		booksFlexTable = new FlexTable();
		
		
		// header for books table		
	    booksFlexTable.setText(0, 0, "Autor");
	    booksFlexTable.setText(0, 1, "Tytuł");
	    booksFlexTable.setText(0, 2, "Rodzaj");
	    booksFlexTable.setText(0, 3, "Cena");
	    booksFlexTable.setText(0, 4, "Strony");
	    booksFlexTable.setText(0, 5, "ISBN");
		booksFlexTable.setText(0, 6, "Edytuj");
	    booksFlexTable.setText(0, 7, "Usuń");

	    // apply formatting for the table
	    booksFlexTable.getRowFormatter().addStyleName(0, "bookListHeader");
	    booksFlexTable.addStyleName("bookList");
	    
	    ownerPanel.add(openNewBookDialogButton);
	    ownerPanel.add(refreshButton);

	    mainPanel.add(ownerPanel);
	    mainPanel.add(booksFlexTable);
	    mainPanel.add(lastUpdatedLabel);
	    mainPanel.add(statusLabel);


	    // Associate the Main panel with the HTML host page.
	    RootPanel.get("bookList").add(mainPanel);
	    
	    
		// Create the popup dialog box
		dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		

		// list all pets in the beginning
		readBook();
		
		
		
		
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
	    
	    // handler to refresh list of pets
	    refreshButton.addClickHandler(new ClickHandler(){
	    	public void onClick(ClickEvent event){
	    		readBook();	    		
	    	}
	    }
	    );


	    
		// dialog to create a new pet
		openNewBookDialogButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				// Create the popup dialog box

				newBookDialogBox addBookDialog = new newBookDialogBox(null);
				addBookDialog.showDialog(null);
				
				openNewBookDialogButton.setEnabled(false);
				refreshButton.setEnabled(false);
				
			}
			
		}
		);

		


		
	}
	
	
	// request book information from the server, and display them in booksFlexTable
	private void readBook(){
	    greetingService.readBook("", new AsyncCallback<ArrayList<Book>>() {
			
	    	public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				dialogBox
						.setText("Remote Procedure Call - Failure");
				serverResponseLabel
						.addStyleName("serverResponseLabelError");
				serverResponseLabel.setHTML(SERVER_ERROR);
				dialogBox.center();
				closeButton.setFocus(true);
			}

			public void onSuccess(final ArrayList<Book> books) {
				
			    int row = 1;			    
			    
			    // each row is one pet
			    for(final Book b: books){

				    booksFlexTable.setText(row, 0, b.getBookAuthor());
				    booksFlexTable.setText(row, 1, b.getBookTitle());
				    booksFlexTable.setText(row, 2, b.getBookType());
				    booksFlexTable.setText(row, 3, b.getBookPrice());
				    booksFlexTable.setText(row, 4, b.getBookPages());
				    booksFlexTable.setText(row, 5, b.getBookISBN());
				    
			    
				    
				    // adding the remove button in each row
				    Button removeBookButton = new Button("x");
				    removeBookButton.addClickHandler(new ClickHandler() {
				      public void onClick(ClickEvent event) {
				    	
				        int removedIndex = books.indexOf(b);
				        removeBook(b.bookID);
				        books.remove(removedIndex);     
				        booksFlexTable.removeRow(removedIndex + 1);
				        
				      }
				    });
				    booksFlexTable.setWidget(row, 7, removeBookButton);	
				    
				    //format table
				    booksFlexTable.getCellFormatter().addStyleName(row, 0, "bookListColumn");
				    booksFlexTable.getCellFormatter().addStyleName(row, 1, "bookListColumn");
				    booksFlexTable.getCellFormatter().addStyleName(row, 2, "bookListColumn");
				    booksFlexTable.getCellFormatter().addStyleName(row, 3, "bookListColumn");
				    booksFlexTable.getCellFormatter().addStyleName(row, 4, "bookListColumn");
				    booksFlexTable.getCellFormatter().addStyleName(row, 5, "bookListColumn");
				    booksFlexTable.getCellFormatter().addStyleName(row, 6, "bookListRemoveColumn");
				    booksFlexTable.getCellFormatter().addStyleName(row, 7, "bookListRemoveColumn");
				    
				    
				    // adding the edit button in each row
				    Button editBookButton = new Button("Edytuj");
				    
				    editBookButton.addClickHandler(new ClickHandler() {
				      public void onClick(ClickEvent event) {
				    	
				    	newBookDialogBox editBookDialog = new newBookDialogBox(b);
						editBookDialog.showDialog(b);
						
						openNewBookDialogButton.setEnabled(false);
						refreshButton.setEnabled(false);

				        
				      }
				    });
				    
				    booksFlexTable.setWidget(row, 6, editBookButton);		    
				    
				    
				    
				    
				    row++;
			    }


			}
		});
	}
	
	// request server to remove a pet
	private void removeBook(String keyString){
		
		greetingService.deleteBook(keyString,
				new AsyncCallback<String>() {
	    			public void onFailure(Throwable caught) {
	    				// Show the RPC error message to the user
	    				dialogBox
	    						.setText("Remote Procedure Call - Failure");
	    				serverResponseLabel
	    						.addStyleName("serverResponseLabelError");
	    				serverResponseLabel.setHTML(SERVER_ERROR);
	    				dialogBox.center();
	    				closeButton.setFocus(true);
	    			}

	    			public void onSuccess(String s) {
	    				dialogBox.setText("Server confirmation");
	    				serverResponseLabel.setHTML(s);
	    				dialogBox.center();
	    				closeButton.setFocus(true);
	    			}
		});
	}
	
	

	// the class pet dialog which enables users to create or update a pet
	public class newBookDialogBox extends DialogBox {
				
	    public newBookDialogBox(final Book b) {
	    	Button newBookButton;
	    		    	
			setAnimationEnabled(true);
			Button cancelButton = new Button("Anuluj");

			final FlexTable newBookFlexTable = new FlexTable();
			final TextBox newAuthorNameField = new TextBox();
			final TextBox newBookNameField = new TextBox();
			final ListBox newBookTypeField = new ListBox();
			final TextBox newBookPriceField = new TextBox();
			final TextBox newBookPagesField = new TextBox();
			final TextBox newBookISBNField = new TextBox();
			

			
			// Populate the gender listbox
			ArrayList<String> type_list = new ArrayList<String>();
			type_list.add("Kryminał");
			type_list.add("Fantastyka");
			type_list.add("Obyczajowa");
			for(String s:type_list){
				newBookTypeField.addItem(s);
			}

			
			newBookFlexTable.setText(0, 0, "Imię autora:");
			newBookFlexTable.setWidget(0, 1,  newAuthorNameField);
			newBookFlexTable.setText(1, 0, "Tytuł książki:");
			newBookFlexTable.setWidget(1, 1, newBookNameField);
			newBookFlexTable.setText(2, 0, "Rodzaj książki:");
			newBookFlexTable.setWidget(2, 1,  newBookTypeField);
			newBookFlexTable.setText(3, 0, "Cena:");
			newBookFlexTable.setWidget(3, 1,  newBookPriceField);
			newBookFlexTable.setText(4, 0, "Ilość stron:");
			newBookFlexTable.setWidget(4, 1, newBookPagesField);
			newBookFlexTable.setText(5, 0, "Numer ISBN:");
			newBookFlexTable.setWidget(5, 1, newBookISBNField);


			newBookFlexTable.setWidget(6, 1, cancelButton);
			//		final Label textToServerLabel = new Label();
//			serverResponseLabel = new HTML();
			VerticalPanel newBookVPanel = new VerticalPanel();
			newBookVPanel.add(newBookFlexTable);			

			
			cancelButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					hide();
					refreshButton.setEnabled(true);
					openNewBookDialogButton.setEnabled(true);
				}
			});
			
			// handle difference between create or edit a book

	    	if(b == null){
	    		// create a new book
				newBookButton = new Button("Zapisz książkę");
				setHTML("Dodaj nową książkę");
				newBookButton.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event){

						
						Book newb = new Book(newAuthorNameField.getText(), 
								newBookNameField.getText(), 
								newBookTypeField.getItemText(newBookTypeField.getSelectedIndex()), 
								newBookPriceField.getText(), 
								newBookPagesField.getText(), 
								newBookISBNField.getText());
						
						callAsyncCreateBook(newb);
						hide();
						refreshButton.setEnabled(true);
						openNewBookDialogButton.setEnabled(true);
			    		readBook();
					}
				}
				);
	    	}
			
	    	else{
	    		// edit existing pet
	    		newBookButton = new Button("Zaktualizuj książkę");
				setHTML("Zaktualizuj istniejącą książkę");
				
				newAuthorNameField.setText(b.getBookAuthor());
				newBookNameField.setText(b.getBookTitle());
				newBookTypeField.setSelectedIndex(type_list.indexOf(b.getBookType()));
				newBookPriceField.setText(b.getBookPrice());
				newBookPagesField.setText(b.getBookPages());
				newBookISBNField.setText(b.getBookISBN());
				
				
				
				

				
				
				newBookButton.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event){
						
						
						Book updatedb = new Book(newAuthorNameField.getText(), 
								newBookNameField.getText(), 
								newBookTypeField.getItemText(newBookTypeField.getSelectedIndex()), 
								newBookPriceField.getText(), 
								newBookPagesField.getText(),
								newBookISBNField.getText());
						
						callAsyncUpdateBook(b.bookID, updatedb);
						hide();
						refreshButton.setEnabled(true);
						openNewBookDialogButton.setEnabled(true);
						readBook();
					}
				}
				);

	    	}
			
			newBookFlexTable.setWidget(6, 0, newBookButton);			
			setWidget(newBookVPanel);
			newBookFlexTable.setSize("100%", "100%");
			
	    }
	    
	    private newBookDialogBox dialog;
	    
	    // show the petDialogBox
	    public void showDialog(Book b){
	    	if (dialog == null) {
	    		dialog = new newBookDialogBox(b);
	    		dialog.setSize("300px", "100px");
	    	}
	    	dialog.center();
	    }
	    
	    
	    // request server to create a pet 
	    private void callAsyncCreateBook(Book b){
			greetingService.createBook(b,
					new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							dialogBox
									.setText("Remote Procedure Call - Failure");
							serverResponseLabel
									.addStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML(SERVER_ERROR);
							dialogBox.center();
							closeButton.setFocus(true);
						}

						public void onSuccess(String result) {
							dialogBox.setText("Server confirmation");
							serverResponseLabel
									.removeStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML(result);
							dialogBox.center();
							closeButton.setFocus(true);
						}
					});

	    }
	    
	    // request server to update pet info 
	    private void callAsyncUpdateBook(String KeyString, Book b){
			greetingService.updateBook(KeyString, b,
					new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							dialogBox
									.setText("Remote Procedure Call - Failure");
							serverResponseLabel
									.addStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML(SERVER_ERROR);
							dialogBox.center();
							closeButton.setFocus(true);
						}

						public void onSuccess(String result) {
							dialogBox.setText("Server confirmation");
							serverResponseLabel
									.removeStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML(result);
							dialogBox.center();
							closeButton.setFocus(true);
						}
					});

	    }

	}
}
