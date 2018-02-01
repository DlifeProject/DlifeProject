package db;

public class ChatroomMessage {
	
	private int sk;
    private int chatroom_sk;
    private int member_sk;
    private String message;
    private String message_stamp;
    private String post_date;

    public ChatroomMessage() {
    		super();
    }
    
	public ChatroomMessage(int sk, int chatroom_sk, int member_sk, String message, String message_stamp,
			String post_date) {
		super();
		this.sk = sk;
		this.chatroom_sk = chatroom_sk;
		this.member_sk = member_sk;
		this.message = message;
		this.message_stamp = message_stamp;
		this.post_date = post_date;
	}

	public int getSk() {
		return sk;
	}

	public void setSk(int sk) {
		this.sk = sk;
	}

	public int getChatroom_sk() {
		return chatroom_sk;
	}

	public void setChatroom_sk(int chatroom_sk) {
		this.chatroom_sk = chatroom_sk;
	}

	public int getMember_sk() {
		return member_sk;
	}

	public void setMember_sk(int member_sk) {
		this.member_sk = member_sk;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage_stamp() {
		return message_stamp;
	}

	public void setMessage_stamp(String message_stamp) {
		this.message_stamp = message_stamp;
	}

	public String getPost_date() {
		return post_date;
	}

	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}
    

}
