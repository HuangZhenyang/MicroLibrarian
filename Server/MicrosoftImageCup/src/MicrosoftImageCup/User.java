package MicrosoftImageCup;

public class User {
	private int id;
	private String userName;
	private String passwd;
	private String sex;
	private String job;
	private int age;
	private String email;
	
	//构造方法重载
	public User(String initUserName){
		this.userName = initUserName;
	}
	public User(String initUserName, String initPasswd){
		this.userName = initUserName;
		this.passwd = initPasswd;
	}
	public User(String initUserName, String initPasswd, String initSex, int initAge, String initEmail){
		this.userName = initUserName;
		this.passwd = initPasswd;
		this.sex = initSex;
		this.age = initAge;
		this.email = initEmail;
	}
	
	@SuppressWarnings("unused")
	private User(){
		
	}
		
	// getter和setter方法 
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
