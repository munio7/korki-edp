package org.example.korkiedp.dao;

import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.util.Database;

import java.io.Console;
import java.sql.*;
import java.util.ArrayList;

public class TutorDAO {
    public static Tutor findById(int id) {
        Tutor tutor = null;
        String sql = "SELECT * FROM tutors WHERE id = ?";
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tutor = new Tutor(rs.getInt("id"), rs.getString("username"),rs.getString("password_hash"), rs.getString("full_name"),rs.getString("email"),rs.getTimestamp("created_at").toLocalDateTime());
            }
            else{
                System.out.println("Tutor not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tutor;
    }

    public static void save(Tutor tutor) {
        String sql = "INSERT INTO tutors(username, password_hash, full_name, email) VALUES(?, ?, ?, ?)";
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tutor.getUsername());
            stmt.setString(2, tutor.getPasswordHash());
            stmt.setString(3, tutor.getFullName());
            stmt.setString(4, tutor.getEmail());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                tutor.setId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Tutor> findAll() {
        String sql = "SELECT * FROM tutors";
        ArrayList<Tutor> tutors = new ArrayList<>();

        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tutors.add(new Tutor(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5),rs.getTimestamp(6).toLocalDateTime() ));
            }

        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return tutors;
    }

    public static void delete(int id) {
        String sql = "DELETE FROM tutors WHERE id = ?";
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tutor deleted successfully");
            }
            else {
                System.out.println("Tutor not found");
            }

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(Tutor tutor) {
        String sql = "UPDATE tutors " +
                "SET username = ?, password_hash = ?, full_name = ?, email = ? " +
                "WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tutor.getUsername());
            stmt.setString(2, tutor.getPasswordHash());
            stmt.setString(3, tutor.getFullName());
            stmt.setString(4, tutor.getEmail());
            stmt.setInt(5, tutor.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tutor updated successfully");
            }
            else {
                System.out.println("Tutor not found");
            }

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
