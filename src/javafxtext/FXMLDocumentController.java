/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxtext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;

/**
 *
 * @author lpberg
 */
public class FXMLDocumentController {
    @FXML
    private Button nextTaskBtn, prevTaskBtn;
    @FXML
    private Label treeFilePathLabel;
    @FXML
    private Label taskFilePathLabel;
    @FXML
    private Label taskDescriptionLabel;
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
    private void nextTaskEventHandler(ActionEvent event) throws Exception {
        int nextTaskIdx;
        int currentTaskIdx = taskList.indexOf(taskDescriptionLabel.getText());
        if (currentTaskIdx == taskList.size()-1){
            nextTaskIdx = 0;
        } else {
            nextTaskIdx = currentTaskIdx + 1;
        }
        taskDescriptionLabel.setText(taskList.get(nextTaskIdx));            
    }
    @FXML
    private void previousTaskEventHandler(ActionEvent event) throws Exception {
        int previousTaskIdx;
        int currentTaskIdx = taskList.indexOf(taskDescriptionLabel.getText());
        if (currentTaskIdx == 0){
            previousTaskIdx = taskList.size()-1;
        } else {
            previousTaskIdx = currentTaskIdx - 1;
        }
        taskDescriptionLabel.setText(taskList.get(previousTaskIdx));
    }
    private String fileChooserDialog(){
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        return selectedFile.getAbsolutePath();
    }
    private void readInTreeFile(String filename) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line;
        root = new TreeItem<>("root");
        myTreeView.setRoot(root);
        myTreeView.showRootProperty().set(false);
        while ((line = bufferedReader.readLine()) != null)
        {
           addTreeItemToTreeViewFromString(root,line.split("\\s*,\\s*"));         
        }
        bufferedReader.close();        
        //enableChildrenExpansionMutuallyExclusive(root);
    }   
    private void readInTaskFile(String filename) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line;
        taskList = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {taskList.add(line);}
        bufferedReader.close();
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
        for (TreeItem<String> child : parent.getChildren()){
            child.expandedProperty().addListener((Observable observable) -> {
                if (child.isExpanded()) {
                    for (TreeItem<String> thisChild : parent.getChildren()){
                        if(!child.equals(thisChild)){
                            thisChild.setExpanded(false);
                        }
                    }
                }
            });
        }
    }
    private void colapseAllTreeItems(TreeItem<String> parent){
        parent.setExpanded(false);
        parent.getChildren().stream().forEach((child) -> {
            colapseAllTreeItems(child);
        });
    }
}
        
