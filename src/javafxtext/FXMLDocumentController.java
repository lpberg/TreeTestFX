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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Label filepath;
    
    @FXML
    private Button fileChooseBtn;
    
    @FXML
    private TreeView nodes;
    
    private ArrayList<String> nodesFromFile = new ArrayList<String>();
    
    @FXML
    private void fileChooserDialog(ActionEvent event) throws Exception {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        filepath.setText(selectedFile.getName());
        readInFile(selectedFile.getAbsolutePath());
        createTreeView();
    }
   
    public void readInFile(String filename) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line;
        
        while ((line = bufferedReader.readLine()) != null)
        {
           System.out.println(line);
           nodesFromFile.add(line);         
        }
        bufferedReader.close();                
    }
    
    public void createTreeView(){
        TreeItem<String> root = new TreeItem<>("root");
        TreeItem<String> a = new TreeItem<>("a");
        TreeItem<String> b = new TreeItem<>("b");
        TreeItem<String> c = new TreeItem<>("c");
        root.getChildren().add(a);
        a.getChildren().add(b);
        b.getChildren().add(c);
        nodes.setRoot(root);
        nodes.showRootProperty().set(false);
    }
}
