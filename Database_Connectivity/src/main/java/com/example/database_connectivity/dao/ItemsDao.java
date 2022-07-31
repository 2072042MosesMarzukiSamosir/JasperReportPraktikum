package com.example.database_connectivity.dao;

import com.example.database_connectivity.model.Category;
import com.example.database_connectivity.model.Items;
import com.example.database_connectivity.util.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemsDao implements DaoInterface<Items> {
    @Override
    public ObservableList<Items> getData() {
        ObservableList<Items> itemsList;
        itemsList = FXCollections.observableArrayList();

        Connection conn = MyConnection.getConnection();
        String query = "select items.idItems, items.name, items.price, items.description, category.idCategory, category.nama from items join category on Category_idCategory = idCategory ";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(query);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                int id = result.getInt("idItems");
                String nama = result.getString("name");
                double price = result.getDouble("price");
                String desc = result.getString("description");
                int idCategory = result.getInt("idCategory");
                String namaCategory = result.getString("nama");
                Category category = new Category(idCategory, namaCategory);
                Items itm = new Items(id, nama, price, desc, category);
                itemsList.add(itm);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itemsList;
    }

    @Override
    public void addData(Items data) {
        Connection conn = MyConnection.getConnection();
        String query = "insert into items(idItems,name,price,description,Category_idCategory) values(?,?,?,?,?)";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, data.getId());
            ps.setString(2, data.getName());
            ps.setDouble(3, data.getPrice());
            ps.setString(4, data.getDescription());
            ps.setInt(5, data.getCategory().getId());
            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("berhasil masukkin data");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delData(Items data) {
        Connection conn = MyConnection.getConnection();
        String query = "DELETE FROM items WHERE idItems = ?";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1,data.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public int updateData(Items data) {
        Connection conn = MyConnection.getConnection();
        String query = "UPDATE items SET name = ?,price = ?,description = ?, Category_idCategory = ? WHERE idItems = ? ";
        PreparedStatement ps;
        int result = 0;
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(5, data.getId());
            ps.setString(1, data.getName());
            ps.setDouble(2, data.getPrice());
            ps.setString(3, data.getDescription());
            ps.setInt(4, data.getCategory().getId());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
