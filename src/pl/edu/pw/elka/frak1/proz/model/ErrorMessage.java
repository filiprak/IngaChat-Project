package pl.edu.pw.elka.frak1.proz.model;

import java.util.HashMap;

/** simple class for agregating error messages in hashmap*/
public class ErrorMessage {
    private HashMap<String, String> errorMap;
    
    public ErrorMessage() {
	errorMap = new HashMap<String, String>();
	initMap();
    }
    
    private void initMap() {
	errorMap.put("ConnectionFailedError", "Cannot connect to server!");
	errorMap.put("DuplicateNicknameError", "This nickname is reserved!");
	errorMap.put("NicknameNotExistError", "Error, cannot find nickname!");
	errorMap.put("InnerError", "Software bug !");
	errorMap.put("CannotConnectError", "Cannot connect to the server !");
	errorMap.put("WrongNicknameError", "This nickname is already used or wrong !");
	errorMap.put("ConnectingProcess", "connecting...");
	errorMap.put("NoConnection", "You are offline !");
	
    }

    public String getErrorMessage(final String errorType) {
	return errorMap.get(errorType);
    }
}
