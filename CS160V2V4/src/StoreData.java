import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * The class builds a database and builds a table.
 * Stores Arraylist of data into the table.
 * 
 * @author Zognxiang(Frank) Liu
 */
public class StoreData {

	private ArrayList<DBEntry> data;

	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://localhost:8889/"; //connection to server
	private final String USER_NAME = "root";
	private final String PASSWORD = "root";
	private final String DATABASE_NAME = "CS160"; //database name
	private final String TABLE_URL = DB_URL + DATABASE_NAME; // connects to the databse
	private final String TABLE_NAME = "EDUCATION"; //table name
	private Connection connection;
	private Statement statement;

	public StoreData(ArrayList<DBEntry> data){
		this.data = data;
	}

	/**
	 * establish access driver, make connection, create table and store data into the table
	 */
	public void action(){
		establishAccessDriver();
		makeConnection();
		createTable();
		storeDate();
	}

	/**
	 * establish connection
	 */
	private void establishAccessDriver(){
		try {
			Class.forName(JDBC_DRIVER).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			System.out.println("Driver Error!");
		}
	}

	/**
	 * connection to the database
	 */
	private void makeConnection(){
		try {
			connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * create table in database
	 */
	private void createTable(){
		try {
			statement = connection.createStatement();
			String sql = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
			statement.executeUpdate(sql);

			connection = DriverManager.getConnection(TABLE_URL, USER_NAME, PASSWORD);
			statement = connection.createStatement();

			sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
			statement.executeUpdate(sql);

			sql = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME
					+ "(id int(5) NOT NULL AUTO_INCREMENT,"
					+ "title text CHARACTER SET latin1 NOT NULL,"
					+ "description text CHARACTER SET latin1 NOT NULL,"
					+ "lesson_link text CHARACTER SET latin1 NOT NULL,"
					+ "lesson_image text CHARACTER SET latin1 NOT NULL," 
					+ "category varchar(200) CHARACTER SET latin1 NOT NULL,"
					+ "student_grades set('1','2','3','4','5','6','7','8','9','10','11','12','k') CHARACTER SET latin1 NOT NULL,"
					+ " author text CHARACTER SET latin1 NOT NULL,"
					+ "content_type text CHARACTER SET latin1 NOT NULL,"
					+ "time_scraped datetime NOT NULL,"
					+ "PRIMARY KEY (id)"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;";

			statement.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * store Date to Database
	 */
	private void storeDate(){
		String title;
		String description;
		String lesson_link;
		String lesson_image;
		String category;
		String student_grade;
		String author;
		String content_type;
		String time_scraped;
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		String sql = "INSERT INTO " + TABLE_NAME
				+ " (title, description, lesson_link, lesson_image, category, student_grades, author, content_type, time_scraped) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)";
		//System.out.println(sql);
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			for (DBEntry dbEntry : data) {
				title = dbEntry.title;
				description = dbEntry.description;
				lesson_link = dbEntry.lesson_link;
				lesson_image =  dbEntry.lesson_image;
				category =  dbEntry.category;
				student_grade = dbEntry.student_grades;
				author =  dbEntry.author;
				content_type = dbEntry.content_type;
				time_scraped = df.format(dbEntry.time_scraped);

				preparedStatement.setString(1, title);
				preparedStatement.setString(2, description);
				preparedStatement.setString(3, lesson_link);
				preparedStatement.setString(4, lesson_image);
				preparedStatement.setString(5, category);
				preparedStatement.setString(6, student_grade);
				preparedStatement.setString(7, author);
				preparedStatement.setString(8, content_type);
				preparedStatement.setString(9, time_scraped);

				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
