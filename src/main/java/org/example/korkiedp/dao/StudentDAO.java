package org.example.korkiedp.dao;

import org.example.korkiedp.model.Student;
import org.example.korkiedp.util.Database;

import java.sql.*;
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
                student = new Student(rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("localization"),
                        rs.getString("tel_number")
                );
            }
            else{
                System.out.println("Student not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return student;
    }

    public static boolean save(Student student) {
        String sql = "INSERT INTO students(first_name,last_name,localization,tel_number) VALUES(?,?,?,?) RETURNING id";
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getLocalization());
            stmt.setString(4, student.getTelNumber());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                student.setId(rs.getInt(1));
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static ArrayList<Student> findAll() {
        String sql = "SELECT * FROM students";
        ArrayList<Student> students = new ArrayList<>();

        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("localization"),
                        rs.getString("tel_number")
                ));
            }

        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return students;
    }
    public static ArrayList<Student> findAllNotInRelationWithTutorId(int tutorId) {
        String sql = "SELECT * FROM students where students.id not in (select student_id from tutor_student where tutor_student.tutor_id = ?)";
        ArrayList<Student> students = new ArrayList<>();

        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tutorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("localization"),
                        rs.getString("tel_number")
                ));
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
                "SET first_name = ? " +
                "last_name = ?" +
                "localization = ?" +
                "tel_number = ?" +
                "WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getLocalization());
            stmt.setString(4, student.getTelNumber());
            stmt.setInt(5, student.getId());
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
