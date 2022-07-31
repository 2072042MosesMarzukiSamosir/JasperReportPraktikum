package com.example.database_connectivity.controller;

import com.example.database_connectivity.DatabaseConnectivity;
import com.example.database_connectivity.dao.ItemsDao;
import com.example.database_connectivity.model.Category;
import com.example.database_connectivity.model.Items;
import com.example.database_connectivity.util.MyConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.*;

public class FirstController {
    public TextField txtID;
    public TextField txtName;
    public TextField txtPrice;
    public TextArea txtDesc;
    public ComboBox cboxCategory;
    public Button btnUpdate;
    public Button btnDelete;
    public TableView tbView;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn<Items, String> colPrice;
    public TableColumn colCategory;
    public Button btnSave;
    private Stage stage;

    private SecondController sc;
    private ObservableList<Items> itemsOList;

    private Items selUpd;

    public void initialize() throws IOException {
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(DatabaseConnectivity.class.getResource("second_page.fxml"));
        Scene scene = new Scene(loader.load());

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("CategoryManagement");
        stage.setScene(scene);

        sc = loader.getController();

        cboxCategory.setItems(sc.olistCategory);
        cboxCategory.getSelectionModel();

        ItemsDao Idao = new ItemsDao();
        itemsOList = Idao.getData();
        tbView.setItems(itemsOList);
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        Locale id = new Locale("id", "ID");
        Currency rupiah = Currency.getInstance(id);
        NumberFormat rupiahformat = NumberFormat.getCurrencyInstance(id);
        colPrice.setCellValueFactory(data-> new SimpleStringProperty(rupiahformat.format(data.getValue().getPrice())));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        btnSave.setDisable(false);

    }

    public void onSave(ActionEvent event) {
        ItemsDao Idao = new ItemsDao();
        if (txtID.getText().isEmpty() || txtName.getText().isEmpty() || txtDesc.getText().isEmpty() || txtPrice.getText().isEmpty() || cboxCategory.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "please fill in all the field", ButtonType.OK);
            alert.showAndWait();
        } else {
            Idao.addData(new Items(Integer.valueOf(txtID.getText()), txtName.getText(), Double.valueOf(txtPrice.getText()), txtDesc.getText(), (Category) this.cboxCategory.getValue()));
            itemsOList = Idao.getData();
            tbView.setItems(itemsOList);
        }

    }

    public void onReset() {
        txtID.clear();
        txtName.clear();
        txtPrice.clear();
        txtDesc.clear();
        cboxCategory.setItems(sc.olistCategory);
        cboxCategory.getSelectionModel();
        btnSave.setDisable(false);
        txtID.setDisable(false);
    }

    public void onShowCategoryManagement(ActionEvent event) {
        stage.showAndWait();
    }

    public void onClose(ActionEvent event) {
        cboxCategory.getScene().getWindow().hide();
        System.out.println("Close");
    }

    public void onDelete(ActionEvent event) {
        Items selDel = (Items) tbView.getSelectionModel().getSelectedItem();
        if (selDel == null){
            Alert alert = new Alert(Alert.AlertType.ERROR,"please select data", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"are you sure to delete this data ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK){
            ItemsDao dao = new ItemsDao();
            dao.delData(selDel);
            itemsOList = dao.getData();
            tbView.setItems(itemsOList);
        }else {
            Alert cancelled = new Alert(Alert.AlertType.INFORMATION,"deleting cancelled");
            cancelled.showAndWait();
        }
    }

    public void onUpdate(ActionEvent event) {
        ItemsDao dao = new ItemsDao();
        selUpd.setId(Integer.parseInt(txtID.getText()));
        selUpd.setName(txtName.getText());
        selUpd.setPrice(Double.valueOf(txtPrice.getText()));
        selUpd.setDescription(txtDesc.getText());
        selUpd.setCategory((Category) this.cboxCategory.getValue());
        onReset();
        dao.updateData(selUpd);
        itemsOList.set(tbView.getSelectionModel().getFocusedIndex(), selUpd);

    }

    public void onTbClicked(MouseEvent event) {
        selUpd =(Items) tbView.getSelectionModel().getSelectedItem();
        if (selUpd == null){
            btnUpdate.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.ERROR,"please select data", ButtonType.OK);
            alert.showAndWait();
            return;
        }else {
            txtID.setText(String.valueOf(selUpd.getId()));
            txtName.setText(selUpd.getName());
            txtPrice.setText(String.valueOf(selUpd.getPrice()));
            txtDesc.setText(selUpd.getDescription());
            cboxCategory.getSelectionModel().select(selUpd.getCategory());
            btnSave.setDisable(true);
            txtID.setDisable(true);
        }

    }

    public void onSimpleReport(ActionEvent event) {
        JasperPrint jp;
        Connection conn = MyConnection.getConnection();
        Map param = new HashMap<>();
        try {
            jp = JasperFillManager.fillReport("report/Report.jasper",param,conn);
            JasperViewer viewer = new JasperViewer(jp,false);
            viewer.setTitle("Laporan Item");
            viewer.setVisible(true);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }

    }

    public void onGroupReport(ActionEvent event) {
        JasperPrint jp;
        Connection conn = MyConnection.getConnection();
        Map param = new HashMap<>();
        try {
            jp = JasperFillManager.fillReport("report/ReportCat.jasper",param,conn);
            JasperViewer viewer = new JasperViewer(jp,false);
            viewer.setTitle("Laporan Item By Category");
            viewer.setVisible(true);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}