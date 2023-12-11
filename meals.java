import java.sql.*;      // needed for the JDBC-related classes
import java.io.*;       // needed for the PrintStream class


public class meals{
    private Connection db;

    public meals(String dbFilename) throws SQLException {
        this.db = DriverManager.getConnection("jdbc:sqlite:" + dbFilename);
    }
    
    public ResultSet resultsFor(String query) throws SQLException {
        Statement stmt = this.db.createStatement();
        ResultSet results = stmt.executeQuery(query);
        return results;
    }

   
    public String getURL(String[] ingredients) throws SQLException {
        String whereClause="WHERE ";
        if (ingredients.length==1){
            whereClause="WHERE ingredients LIKE '%" + ingredients[0] + "%';";
        }

        else{
        for (int i=0; i<ingredients.length; i++){
            if (i==0){
            String to_add="ingredients LIKE '%" + ingredients[i] + "%'";
            whereClause+=to_add;
            }
            else if (i==ingredients.length-1){
            String to_add="AND ingredients LIKE '%" + ingredients[i] + "%';";
            whereClause+=to_add;
            }
            else{
            String to_add="AND ingredients LIKE '%" + ingredients[i] + "%'";
            whereClause+=to_add;
            }
        }
    }
        String query = "SELECT meal_name, recipe_info FROM combined_meals " + whereClause;
        ResultSet results = resultsFor(query);
        String result ="";
        if (results.next()){
            result=result+results.getString(1)+", "+results.getString(2)+"\n";
        while(results.next()){
            result=result+results.getString(1)+", "+results.getString(2)+"\n";
        }
        return result;
    }
         else {
            return "sorry...no available recipes";
        }
    }   

    public String exist(String username, String password) throws SQLException {
        String whereClause="Where username='"+username+"' and password='"+password+"';" ;
        String query="SELECT * FROM user_info "+whereClause;
        ResultSet results = resultsFor(query);
        if (results.next()){
            return "login success! loading...";
        }
        else{
            return "no credential found, please double check your usernane and password or hit 'register'!";
        }

    }

    public boolean existUser(String username) throws SQLException{
        String query= "SELECT * FROM user_info Where username='"+username+"';";
        ResultSet results=resultsFor(query);
        if (results.next()){
            return true;
        }else{
            return false;
        }
    }

    public String register(String username, String password) throws SQLException {
        if (existUser(username)) {
            return "username already taken, please use another username";
        } else {
            String query = "INSERT INTO user_info (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = this.db.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate(); // Use executeUpdate for INSERT, UPDATE, and DELETE
            return "success!";
        }
    }
    
    
        
    public static void main(String[] args) { 
        try {meals project = new meals("meals.db"); 
        String result=project.register("adinfsij","asddfdfd");  
        System.out.println(result);
        } 
        catch (SQLException e) { e.printStackTrace(); } 
    }
}