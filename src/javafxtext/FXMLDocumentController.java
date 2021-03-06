package javafxtext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;

public class FXMLDocumentController implements Initializable {
    @FXML
    private Button nextTaskBtn, prevTaskBtn, resetBtn;
    @FXML
    private Label treeFilePathLabel,  taskFilePathLabel, taskDescriptionLabel;
    @FXML
    public TreeView myTreeView;
    
    public ArrayList<String> taskList;
    
    TreeItem<String> root;
    
    @FXML
    private void resetEventHandler(ActionEvent event) throws Exception {
        root.getChildren().stream().forEach((child) -> {
            colapseAllTreeItems(child);
        });
    }
    @FXML
    private void treeFileEventHandler(ActionEvent event) throws Exception {
        String path = fileChooserDialog();
        treeFilePathLabel.setText(path);
        readInTreeFile(path);
    }    
    @FXML
    private void taskFileEventHandler(ActionEvent event) throws Exception {
        String path = fileChooserDialog();
        taskFilePathLabel.setText(path);
        readInTaskFile(path);
    }
    @FXML
    private void nextOrPrevTaskEventHandler(ActionEvent event) throws Exception {
        int setTask;
        int currentTaskIdx = taskList.indexOf(taskDescriptionLabel.getText());
        //see if the button clicked is the "next task" button, else, its the previous task button
        if (((Button) event.getSource()).getText().equals("New Task")){
            setTask = (currentTaskIdx == taskList.size()-1) ? 0 : currentTaskIdx + 1;
        } else {
             setTask = (currentTaskIdx == 0) ? taskList.size() - 1 : currentTaskIdx - 1;
        }
        taskDescriptionLabel.setText(taskList.get(setTask));   
    }
    private String fileChooserDialog(){
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        return selectedFile.getAbsolutePath();
    }
    private void readInTreeFile(String filename) throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;
            root = new TreeItem<>("root");
            myTreeView.setRoot(root);
            myTreeView.showRootProperty().set(false);
            while ((line = bufferedReader.readLine()) != null)
            {
                addTreeItemToTreeViewFromString(root,line.split("\\s*,\\s*"));
            }
        }
        resetBtn.setDisable(false);
    }   
    private void readInTaskFile(String filename) throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;
            taskList = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {taskList.add(line);}
        }
        taskDescriptionLabel.setText(taskList.get(0));
        nextTaskBtn.setDisable(false);
        prevTaskBtn.setDisable(false);
    }  
    private int getTreeItemIndexbyStringValue(TreeItem<String> parent,String val){
        for(TreeItem<String> child : parent.getChildren()){
            if(val.equals(child.getValue())){
                return parent.getChildren().indexOf(child);
            }
        }
        return -1;        
    }
    private void addTreeItemToTreeViewFromString(TreeItem<String> parent, String[] splitLine){
        String nodeName = splitLine[0];
        if (splitLine.length < 2) {
            parent.getChildren().add(new TreeItem<>(nodeName));
        } else {                 
            int indexOfChild = getTreeItemIndexbyStringValue(parent,nodeName);
            TreeItem<String> newParent = parent.getChildren().get(indexOfChild);
            String[] subset = Arrays.copyOfRange(splitLine, 1, splitLine.length);
            addTreeItemToTreeViewFromString(newParent,subset);
        }  
    }
    private void enableChildrenExpansionMutuallyExclusive(TreeItem<String> parent){
        parent.getChildren().stream().forEach((child) -> {
            child.expandedProperty().addListener((Observable observable) -> {
                if (child.isExpanded()) {
                    parent.getChildren().stream().filter((thisChild) -> (!child.equals(thisChild))).forEach((thisChild) -> {
                        thisChild.setExpanded(false);
                    });
                }
            });
        });
    }
    private void colapseAllTreeItems(TreeItem<String> parent){
        parent.setExpanded(false);
        parent.getChildren().stream().forEach((child) -> {
            colapseAllTreeItems(child);
        });
    }
    private String  getPathToItem(TreeItem<String> child){
        if (child.getParent() != null){
            return getPathToItem(child.getParent())+" | "+child.getValue();
        }
        return "";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       myTreeView.getSelectionModel().selectedItemProperty()
            .addListener(new ChangeListener<TreeItem<String>>() {
                @Override
                public void changed(
                    ObservableValue<? extends TreeItem<String>> observable,
                    TreeItem<String> old_val, TreeItem<String> new_val) {
                    TreeItem<String> selectedItem = new_val;
                    //System.out.println("Selected Node Path : " + getPathToItem(selectedItem));
                }
            });
    }
}
        
