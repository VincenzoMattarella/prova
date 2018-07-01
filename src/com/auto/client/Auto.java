package com.auto.client;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.auto.shared.FieldVerifier;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Auto implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	

	MyFactory factory = GWT.create(MyFactory.class);
	

	Person makePerson(){
		AutoBean<Person> person = factory.person();
		
		return person.as();
	}
	Address makeAddress(){
		AutoBean<Address> address = factory.address();
		
		return address.as();
	}
	
	Worker makeWorker(){
		AutoBean<Worker> worker = factory.worker();
		return worker.as();
	}
	
	String serializeToJson(Person person){
		AutoBean<Person> bean = AutoBeanUtils.getAutoBean(person);
		
		return AutoBeanCodex.encode(bean).getPayload();
	}
	
	Person deserializeFromJson(String json){
		AutoBean<Person> bean = AutoBeanCodex.decode(factory, Person.class, json);
		return bean.as();
	}

	String serializeToJson2(Worker person){
		AutoBean<Worker> bean = AutoBeanUtils.getAutoBean(person);
		
		return AutoBeanCodex.encode(bean).getPayload();
	}
	
	Worker deserializeFromJson2(String json){
		AutoBean<Worker> bean = AutoBeanCodex.decode(factory, Worker.class, json);
		return bean.as();
	}
	
	
	public void onModuleLoad() {
		String j = "{ \"name\" : \"John Doe\", \"timestamp\" : 12324131343 ,\"address\" : { \"street\" : \"1234 Maple St\", \"city\" : \"Nowhere\" } }";
		Address a1 = makeAddress();
		Address a2 = makeAddress();
		Address a3 = makeAddress();
		Address a4 = makeAddress();
		a1.setCity("Milano");
		a1.setStreet("AA");
		a2.setCity("Bol");
		a3.setCity("Torino");
		a4.setCity("Padova");
		a2.setStreet("BB");
		a3.setStreet("CC");
		a4.setStreet("DD");
		List<Address> l= new ArrayList<>();
		l.add(a1);
		l.add(a2);
		l.add(a3);
		l.add(a4);
		
		
		Person p = makePerson();
		Address a = makeAddress();
		a.setCity("Bologna");
		a.setStreet("Via Matteotti");
		p.setName("Vincenzino");
		p.setTimestamp(new Timestamp(System.currentTimeMillis()));
		p.setAddress(a);
		p.setAnni(27);
		String input = serializeToJson(p);
		
		Person vins = deserializeFromJson(input);
		
		Worker w = makeWorker();
		w.setAddress(a);
		w.setAnni(20);
		w.setFreeday("Sabato");
		w.setName("Enzo");
		w.setAltAddress(l);
		String jsonWorker = serializeToJson2(w);
		
		Worker vinsW = deserializeFromJson2(jsonWorker);
		
		
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		System.err.println(jsonWorker);

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML(input + "<b>JSON parsed:</b>" + vins.getAnni() + 
				vins.getName() + " and " + vins.getTimestamp() + " and " + vins.getAddress().getStreet()));
		dialogVPanel.add(new HTML("<b>" +jsonWorker + " eeee  " + vinsW.getFreeday() + vinsW.getName() + "</b>"));
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogVPanel.setSize("600px", "600px");
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						dialogBox.setText("Remote Procedure Call - Failure");
						serverResponseLabel.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						dialogBox.center();
						closeButton.setFocus(true);
					}

					public void onSuccess(String result) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(result);
						dialogBox.center();
						closeButton.setFocus(true);
					}
				});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
