package pl.edu.pw.elka.frak1.proz.model;

/**
 * host representation class in program mainly it is used in model for holding
 * users connected to server and their nickname/status
 */
public class UserHost {
    private String nickname;
    private boolean isOnline;

    public UserHost(final String nickname, boolean isOnline) {
	this.nickname = nickname;
	this.isOnline = isOnline;
    }

    public String getNickname() {
	return nickname;
    }

    public boolean getIsOnline() {
	return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
	this.isOnline = isOnline;
    }

    public void setNickname(final String nickname) {
	this.nickname = nickname;
    }
}
