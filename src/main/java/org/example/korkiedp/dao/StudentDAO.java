package org.example.korkiedp.dao;

import org.example.korkiedp.model.Student;
import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentDAO {
    public static Student findById(int id) {
        Student student = null;
        String sql = "SELECT * FROM students WHERE id = ?";
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                student = new Student(rs.getInt("id"), rs.getString("name"));
            }
            else{
                System.out.println("Student not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return student;
    }

    public static void save(Student student) {
        String sql = "INSERT INTO students(name) VALUES(?)";
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getName());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                student.setId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Student> findAll() {
        String sql = "SELECT * FROM students";
        ArrayList<Student> students = new ArrayList<>();

        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(rs.getInt(1),rs.getString(2)));
            }

        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return students;
    }

    public static void delete(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully");
            }
            else {
                System.out.println("Student not found");
            }

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(Student student) {
        String sql = "UPDATE students " +
                "SET name = ? " +
                "WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setInt(2, student.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student updated successfully");
            }
            else {
                System.out.println("Student not found");
            }

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
