package pl.edu.pw.elka.frak1.proz.network;

import org.json.JSONObject;

import pl.edu.pw.elka.frak1.proz.model.Message;

/**
 * class used to convert messages to json object and the opposite way uses json
 * library
 */
public class JSONMessageFactory {

    public JSONObject createJSONObj(final Message message) {
	JSONObject jsonObj = new JSONObject();
	jsonObj.put("author", message.getAuthor());
	jsonObj.put("time", message.getTime());
	jsonObj.put("contents", message.getContents());
	jsonObj.put("isOwn", message.isOwn());
	jsonObj.put("isSys", message.isSystemMessage());
	jsonObj.put("header", message.isWithHeader());
	return jsonObj;
    }

    public Message createMessage(final String source) {
	String author, contents, time;
	boolean isSys, isOwn, isWithHeader;
	JSONObject jsonObj = new JSONObject(source);
	author = jsonObj.getString("author");
	contents = jsonObj.getString("contents");
	time = jsonObj.getString("time");
	isOwn = jsonObj.getBoolean("isOwn");
	isSys = jsonObj.getBoolean("isSys");
	isWithHeader = jsonObj.getBoolean("header");
	Message message = new Message(author, time, contents, isOwn, isWithHeader);
	message.setSystemMessage(isSys);
	return message;
    }
}
