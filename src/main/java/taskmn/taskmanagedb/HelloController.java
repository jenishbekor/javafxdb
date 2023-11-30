package taskmn.taskmanagedb;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelloController {

    private TaskDAO taskDAO;

    @FXML
    private TextField input;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label label;

    ObservableList<Task> tasks = FXCollections.observableArrayList();

    @FXML
    TableView<Task> table;

    public void initialize(){

        taskDAO = new TaskDAO();
        try {
            tasks.setAll(taskDAO.getAllTasks());
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        table.setItems(tasks);

        TableColumn<Task, String> nameColumn = new TableColumn<>("Task Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));

        TableColumn<Task, Integer> ageColumn = new TableColumn<>("Description");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));

        // Add columns to the table
        table.getColumns().setAll(nameColumn, ageColumn);

    }

    @FXML
    public void addNewTask(){

        Task newTask = new Task(input.getText());
        newTask.setTaskDescription("Task description");
        newTask.setDeadline(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        newTask.setPriority(Priority.LOW);


        int id = taskDAO.createTask(newTask);
        newTask.setTaskID(id);
        System.out.println(newTask.getTaskID());
        tasks.add(newTask);

        input.setText("");
    }

    @FXML
    public void onTableClick(){
        try{
            int id = table.getSelectionModel().getSelectedIndex();

            label.setText( tasks.get(id).getTaskID() + tasks.get(id).getTaskName());

        }
        catch(Exception e){
            //System.out.println(e.toString());
        }
    }

}