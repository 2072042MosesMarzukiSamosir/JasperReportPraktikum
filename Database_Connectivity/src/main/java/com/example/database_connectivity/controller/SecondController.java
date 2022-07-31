package com.example.database_connectivity.controller;

import com.example.database_connectivity.dao.CategoryDao;
import com.example.database_connectivity.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class SecondController {
    public TextField txtIDCat;
    public TextField txtNamaCat;
    public TableView tbviewCategory;
    public ObservableList<Category>olistCategory;
    public TableColumn colID;
    public TableColumn colNameCat;
    public Button btnSave;

    public void initialize(){
        CategoryDao dao = new CategoryDao();
        olistCategory = dao.getData();
        tbviewCategory.setItems(olistCategory);
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameCat.setCellValueFactory(new PropertyValueFactory<>("name"));

    }
    public void onSaveCategory(ActionEvent event) {
        CategoryDao dao = new CategoryDao();
        if (txtIDCat.getText().isEmpty() || txtNamaCat.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR,"please fill in all the field", ButtonType.OK);
            alert.showAndWait();
        }else {
            dao.addData(new Category(Integer.valueOf(txtIDCat.getText()),txtNamaCat.getText()));
            olistCategory = dao.getData();
            tbviewCategory.setItems(olistCategory);
        }

    }
}
