package pl.edu.pw.elka.frak1.proz.model;

/**
 * Message inner program structure
 */
public class Message {
    private String author;
    private String time;
    private String contents;

    private boolean isOwn;
    private boolean withHeader;
    /**
     * this flag indicates if message is invisible (system) system messages
     * arent printed, they use isOwn and withHeader as flags to determine
     * actions such as some host disconnect/change state etc.
     */
    private boolean isSystemMessage = false;

    public Message(String author, String time, String contents, boolean isOwn, boolean withHeader) {
	this.author = author;
	this.time = time;
	this.contents = contents;
	this.setIsOwn(isOwn);
	this.setWithHeader(withHeader);
    }

    public String getAuthor() {
	return author;
    }
    
    public void setAuthor(final String author) {
	this.author = author;
    }

    public String getTime() {
	return time;
    }
    
    public void setTime(final String time) {
	this.time = time;
    }

    public String getContents() {
	return contents;
    }

    public boolean isOwn() {
	return isOwn;
    }

    public void setIsOwn(boolean isOwn) {
	this.isOwn = isOwn;
    }

    public boolean isWithHeader() {
	return withHeader;
    }

    public void setWithHeader(boolean withHeader) {
	this.withHeader = withHeader;
    }

    public boolean isSystemMessage() {
	return isSystemMessage;
    }

    public void setSystemMessage(boolean isSystemMessage) {
	this.isSystemMessage = isSystemMessage;
    }

}
