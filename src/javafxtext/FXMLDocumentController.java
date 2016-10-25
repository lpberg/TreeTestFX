/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxtext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private Label filePathLabel;
    
    @FXML
    private TreeView myTreeView;

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
        TreeItem<String> root = new TreeItem<>("root");
        myTreeView.showRootProperty().set(false);
        while ((line = bufferedReader.readLine()) != null)
        {
           String[] splitLine = line.split("\\s*,\\s*");
           addNodeFromString(root,splitLine);         
        }
        bufferedReader.close();
        myTreeView.setRoot(root);
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
}
