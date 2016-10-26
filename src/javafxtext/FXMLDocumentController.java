/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxtext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

/**
 *
 * @author lpberg
 */
public class FXMLDocumentController {
    
    @FXML
    private Label filePathLabel;
    
    @FXML
    public TreeView myTreeView;
    
    TreeItem<String> root;

    @FXML
    private void fileChooserDialog(ActionEvent event) throws Exception {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        filePathLabel.setText(selectedFile.getName());
        readInFile(selectedFile.getAbsolutePath());
    }
   
    private void readInFile(String filename) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line;
        root = new TreeItem<>("root");
        myTreeView.showRootProperty().set(false);
        while ((line = bufferedReader.readLine()) != null)
        {
           String[] splitLine = line.split("\\s*,\\s*");
           addNodeFromString(root,splitLine);         
        }
        bufferedReader.close();
        myTreeView.setRoot(root);
        makeOnlyOneChildExpandableAtATime(root);
    }   
    
    private int getIndexbyValue(TreeItem<String> parent,String val){
        for(TreeItem<String> child : parent.getChildren()){
            if(val.equals(child.getValue())){
                return parent.getChildren().indexOf(child);
            }
        }
        return -1;        
    }
    
    private void addNodeFromString(TreeItem<String> parent, String[] splitLine){
        String nodeName = splitLine[0];
        if (splitLine.length < 2) {
            parent.getChildren().add(new TreeItem<>(nodeName));
        } else {                 
            int indexOfChild = getIndexbyValue(parent,nodeName);
            TreeItem<String> newParent = parent.getChildren().get(indexOfChild);
            String[] subset = Arrays.copyOfRange(splitLine, 1, splitLine.length);
            addNodeFromString(newParent,subset);
        }  
    }
    /*@FXML
    private void mouseClicked(MouseEvent mouseEvent){
        TreeItem<String> item = (TreeItem<String>) myTreeView.getSelectionModel().getSelectedItem();
        String selectedItem = item.getValue();
        //System.out.println(selectedItem);
        for (TreeItem<String> child : root.getChildren()){
            if(true){
                //System.out.println(child.getValue());
            }
        }*/
    private void makeOnlyOneChildExpandableAtATime(TreeItem<String> parent){
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
    }
