package db;

public class FriendRelation {

	private int sk;
	private int member_sk;
	private String friend_type;
	private String friend_account;
	private int is_shareable;
	private String post_date;
	
	public FriendRelation() {
		super();
	}

	public FriendRelation(int sk, int member_sk, String friend_type, String friend_account, int is_shareable,
			String post_date) {
		super();
		this.sk = sk;
		this.member_sk = member_sk;
		this.friend_type = friend_type;
		this.friend_account = friend_account;
		this.is_shareable = is_shareable;
		this.setPost_date(post_date);
	}

	public int getSk() {
		return sk;
	}

	public void setSk(int sk) {
		this.sk = sk;
	}

	public int getMember_sk() {
		return member_sk;
	}

	public void setMember_sk(int member_sk) {
		this.member_sk = member_sk;
	}

	public String getFriend_type() {
		return friend_type;
	}

	public void setFriend_type(String friend_type) {
		this.friend_type = friend_type;
	}

	public String getFriend_account() {
		return friend_account;
	}

	public void setFriend_account(String friend_account) {
		this.friend_account = friend_account;
	}

	public int getIs_shareable() {
		return is_shareable;
	}

	public void setIs_shareable(int is_shareable) {
		this.is_shareable = is_shareable;
	}

	public String getPost_date() {
		return post_date;
	}

	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}

}
