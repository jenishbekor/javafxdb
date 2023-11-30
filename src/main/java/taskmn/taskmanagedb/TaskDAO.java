package taskmn.taskmanagedb;

import javafx.fxml.LoadException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    private Connection conn;

    public TaskDAO(){
        String url = "jdbc:postgresql:testdb";
        String username = "postgres";
        String pass = "postgres";

        try{
            this.conn = DriverManager
                    .getConnection(url, username, pass);
            System.out.println("database successfully connected");
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Task getTaskById(int id){
        String sql = "SELECT * FROM tasks WHERE taskid=?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,id);

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){

                    String taskName = rs.getString("taskname");
                    String description = rs.getString("description");
                    Date deadline = rs.getDate("deadline");
                    String priority = rs.getString("priority");

                    Task task = new Task();
                    task.setTaskID(id);
                    task.setTaskName(taskName);
                    task.setTaskDescription(description);
                    task.setPriority(Priority.valueOf(priority));
                    task.setDeadline(deadline);
                    return task;

                }
            }catch(SQLException e){
                System.out.println(e.toString());
            }

        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        return null;
    }

    private Task getTaskFromResultSet(ResultSet rs) throws SQLException{

        int taskID = rs.getInt("taskid");
        String taskName = rs.getString("taskname");
        String description = rs.getString("description");
        Date deadline = rs.getDate("deadline");
        String priority = rs.getString("priority");

        Task task = new Task();
        task.setTaskID(taskID);
        task.setTaskName(taskName);
        task.setTaskDescription(description);
        task.setPriority(Priority.valueOf(priority));
        task.setDeadline(deadline);
        return task;
    }

    public List<Task> getAllTasks(){
        List<Task> tasks = new ArrayList<Task>();

        String sql = "SELECT * FROM tasks";

        try(Statement stmt = conn.createStatement()){

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                Task task = getTaskFromResultSet(rs);
                tasks.add(task);
            }

        }
        catch(SQLException e){
            System.out.println(e.toString());
        }


        return tasks;
    }

    public int createTask(Task newTask){
        int generatedId=-1;

        String sql = "INSERT INTO tasks(" +
                " taskname, description, deadline, priority)" +
                " VALUES (?, ?, ?, ?);";

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, newTask.getTaskName());
            stmt.setString(2, newTask.getTaskDescription());
            stmt.setDate(3, new java.sql.Date(newTask.getDeadline().getTime()));
            stmt.setString(4, newTask.getPriority().toString());

            int affectedRows = stmt.executeUpdate();

            if( affectedRows == 0){
                throw new SQLException("Creating task failed.");
            }

            try( ResultSet generatedKeys = stmt.getGeneratedKeys()){

                if(generatedKeys.next()){
                    generatedId = generatedKeys.getInt(1);
                    return generatedId;
                }
            }

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return generatedId;
    }

    public void updateTask(Task task){
        String sql = "UPDATE tasks SET taskname=?, description=?, priority=? " +
                "WHERE taskid=?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, task.getTaskName());
            stmt.setString(2, task.getTaskDescription());
            stmt.setString (3, task.getPriority().toString());
            stmt.setInt(4, task.getTaskID());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0){
                System.out.println("Update failed.");
            }
            System.out.println("Updated successfully!");
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
    }

    public void deleteTask(int id){

        String sql = "DELETE from tasks WHERE taskid = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();

            if(result ==0){
                throw new SQLException("Delete operation failed with id="+id);
            }

            System.out.println("Row successfully deleted");
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }

    }

}
