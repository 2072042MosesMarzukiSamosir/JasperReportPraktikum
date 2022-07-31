package com.example.database_connectivity.dao;

import com.example.database_connectivity.model.Category;
import com.example.database_connectivity.util.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDao implements DaoInterface<Category>{

    @Override
    public ObservableList<Category> getData() {
        ObservableList<Category> categoryList;
        categoryList = FXCollections.observableArrayList();

        Connection conn = MyConnection.getConnection();
        String query = "select * from category";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(query);
            ResultSet result = ps.executeQuery();
            while(result.next()){
                int id = result.getInt("idCategory");
                String nama = result.getString("nama");
                Category cat = new Category(id,nama);
                categoryList.add(cat);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryList;
    }

    @Override
    public void addData(Category data) {
        Connection conn = MyConnection.getConnection();
        String query = "insert into category(idCategory,nama) values(?,?)";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1,data.getId());
            ps.setString(2,data.getName());
            int result = ps.executeUpdate();
            if (result > 0){
                System.out.println("berhasil masukkin data");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delData(Category data) {
        return false;
    }

    @Override
    public int updateData(Category data) {
        return 0;
    }
}
