package org.example.korkiedp.dao;

import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.util.Database;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TutorStudentDAO {
    public static ArrayList<TutorStudent> findByTutorId(int id) {
        ArrayList<TutorStudent> assigned_students = new ArrayList<TutorStudent>();
        String sql = "SELECT * FROM tutor_student WHERE tutor_id = ?";
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TutorStudent tutor_student = new TutorStudent(
                        rs.getInt(1),
                        rs.getInt(2),
                        (rs.getDate(3) != null) ? rs.getDate(3).toLocalDate() : null,
                        rs.getBoolean(4),
                        rs.getBigDecimal(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        (rs.getTimestamp(10) != null) ? rs.getTimestamp(10).toLocalDateTime() : null,
                        (rs.getTimestamp(11) != null) ? rs.getTimestamp(11).toLocalDateTime() : null,
                        rs.getString(12)
                );
                assigned_students.add(tutor_student);
                System.out.println(tutor_student);
            }

            if (assigned_students == null) {
                System.out.println("No students assigned to tutor id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return assigned_students;
    }

    public static ArrayList<TutorStudent> findByStudentId(int id) {
        ArrayList<TutorStudent> assigned_tutors = new ArrayList<TutorStudent>();
        String sql = "SELECT * FROM tutor_student WHERE student_id = ?";
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TutorStudent tutor_student = new TutorStudent(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDate(3).toLocalDate(),
                        rs.getBoolean(4),
                        rs.getBigDecimal(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getTimestamp(10).toLocalDateTime(),
                        (rs.getTimestamp(11) != null) ? rs.getTimestamp(11).toLocalDateTime() : null,
                        rs.getString(12)
                );
                assigned_tutors.add(tutor_student);
            }
            if (assigned_tutors == null) {
                System.out.println("No tutors assigned to student id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return assigned_tutors;
    }

    public static void save(TutorStudent tutor_student) {
        String sql = "INSERT INTO tutor_student(" +
                "tutor_id, " +
                "student_id," +
                "start_date," +
                "default_price," +
                "preferred_days," +
                "preferred_hours," +
                "class," +
                "notes) VALUES(?, ?, ?, ?,?,?,?,?,?)";
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tutor_student.getTutorId());
            stmt.setInt(2, tutor_student.getStudentId());
            stmt.setDate(3, Date.valueOf(tutor_student.getStartDate()));
            stmt.setBoolean(4, tutor_student.getActive());
            stmt.setBigDecimal(5, tutor_student.getDefault_price());
            stmt.setString(6, tutor_student.getPreferredDays());
            stmt.setString(7, tutor_student.getPreferredHours());
            stmt.setString(8, tutor_student.getclassName());
            stmt.setString(9, tutor_student.getNotes());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully saved new tutor_student");
            }
            else{
                System.out.println("Failed to save new tutor_student");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static TutorStudent findExactTutorStudent(int tutor_id, int student_id) {
        String sql = "SELECT * FROM tutor_student WHERE tutor_id = ? AND student_id = ?";
        TutorStudent tutor_student = null;

        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tutor_id);
            stmt.setInt(2, student_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tutor_student = new TutorStudent(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDate(3).toLocalDate(),
                        rs.getBoolean(4),
                        rs.getBigDecimal(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getTimestamp(10).toLocalDateTime(),
                        (rs.getTimestamp(11) != null) ? rs.getTimestamp(11).toLocalDateTime() : null,
                        rs.getString(12)
                );
                System.out.println("Found tutor_student: " + tutor_student);
            }
            else
                System.out.println("No such tutor_student");

        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return tutor_student;
    }

    public static void deleteExactTutorStudent(int tutor_id, int student_id) {
        String sql = "DELETE FROM tutor_student WHERE tutor_id = ? AND student_id = ?";
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tutor_id);
            stmt.setInt(2, student_id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tutor_student deleted successfully");
            }
            else {
                System.out.println("Tutor_student not found");
            }

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public static void updateExactTutorStudent(TutorStudent tutor_student) {
//        String sql = "UPDATE tutor_student " +
//                "SET start_date = ?,active  = ?,default_price = ?,preferred_days = ?,preferred_hours = ?,level = ?,notes = ?, last_contatced_at = ?" +
//                "WHERE tutor_id = ? And student_id = ?";
//        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setDate(1, Date.valueOf(tutor_student.getStartDate()));
//            stmt.setBoolean(2, tutor_student.getActive());
//            stmt.setBigDecimal(3, tutor_student.getDefaultPrice());
//            stmt.setString(4, tutor_student.getPreferredDays());
//            stmt.setString(5, tutor_student.getPreferredHours());
//            stmt.setString(6, tutor_student.getLevel());
//            stmt.setString(7, tutor_student.getNotes());
//            stmt.setTimestamp(8,Timestamp.valueOf(tutor_student.getLastContactedAt()));
//            stmt.setInt(9, tutor_student.getTutorId());
//            stmt.setInt(10, tutor_student.getStudentId());
//
//            int affectedRows = stmt.executeUpdate();
//            if (affectedRows > 0) {
//                System.out.println("Successfully updated tutor_student");
//            }
//            else {
//                System.out.println("Failed to update tutor_student");
//            }
//
//        }catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static boolean updateAll(TutorStudent tutorStudent) {
        String sql = """
        UPDATE tutor_student SET 
            start_date = ?, 
            active = ?, 
            default_price = ?, 
            preferred_days = ?, 
            preferred_hours = ?, 
            class = ?, 
            notes = ?, 
            last_contacted_at = ?, 
            created_at = ?, 
            student_name = ?
        WHERE tutor_id = ? AND student_id = ?
        """;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, tutorStudent.getStartDate() != null ? Date.valueOf(tutorStudent.getStartDate()) : null);
            stmt.setBoolean(2, tutorStudent.getActive());
            stmt.setBigDecimal(3, tutorStudent.getDefault_price());
            stmt.setString(4, tutorStudent.getPreferredDays());
            stmt.setString(5, tutorStudent.getPreferredHours());
            stmt.setString(6, tutorStudent.getclassName());
            stmt.setString(7, tutorStudent.getNotes());
            stmt.setTimestamp(8, tutorStudent.getLastContactedAt() != null ? Timestamp.valueOf(tutorStudent.getLastContactedAt()) : null);
            stmt.setTimestamp(9, tutorStudent.getCreated_at() != null ? Timestamp.valueOf(tutorStudent.getCreated_at()) : null);
            stmt.setString(10, tutorStudent.getDefault_name());

            stmt.setInt(11, tutorStudent.getTutorId());
            stmt.setInt(12, tutorStudent.getStudentId());

            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean updateStartDate(int tutorId, int studentId, LocalDate date) {
        String sql = "UPDATE tutor_student SET start_date = ? WHERE tutor_id = ? AND student_id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setDate(1, date != null ? Date.valueOf(date) : null);
            stmt.setInt(2, tutorId);
            stmt.setInt(3, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updateActive(int tutorId, int studentId, boolean active) {
        String sql = "UPDATE tutor_student SET active = ? WHERE tutor_id = ? AND student_id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setBoolean(1, active);
            stmt.setInt(2, tutorId);
            stmt.setInt(3, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updateDefaultPrice(int tutorId, int studentId, BigDecimal price) {
        String sql = "UPDATE tutor_student SET default_price = ? WHERE tutor_id = ? AND student_id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setBigDecimal(1, price);
            stmt.setInt(2, tutorId);
            stmt.setInt(3, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updatePreferredDays(int tutorId, int studentId, String days) {
        String sql = "UPDATE tutor_student SET preferred_days = ? WHERE tutor_id = ? AND student_id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setString(1, days);
            stmt.setInt(2, tutorId);
            stmt.setInt(3, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updatePreferredHours(int tutorId, int studentId, String hours) {
        String sql = "UPDATE tutor_student SET preferred_hours = ? WHERE tutor_id = ? AND student_id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setString(1, hours);
            stmt.setInt(2, tutorId);
            stmt.setInt(3, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updateClass(int tutorId, int studentId, String className) {
        String sql = "UPDATE tutor_student SET class = ? WHERE tutor_id = ? AND student_id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setString(1, className);
            stmt.setInt(2, tutorId);
            stmt.setInt(3, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updateNotes(int tutorId, int studentId, String notes) {
        String sql = "UPDATE tutor_student SET notes = ? WHERE tutor_id = ? AND student_id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setString(1, notes);
            stmt.setInt(2, tutorId);
            stmt.setInt(3, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updateCreatedAt(int tutorId, int studentId, LocalDateTime createdAt) {
        String sql = "UPDATE tutor_student SET created_at = ? WHERE tutor_id = ? AND student_id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setTimestamp(1, createdAt != null ? Timestamp.valueOf(createdAt) : null);
            stmt.setInt(2, tutorId);
            stmt.setInt(3, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updateLastContactedAt(int tutorId, int studentId, LocalDateTime contactTime) {
        String sql = "UPDATE tutor_student SET last_contacted_at = ? WHERE tutor_id = ? AND student_id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setTimestamp(1, contactTime != null ? Timestamp.valueOf(contactTime) : null);
            stmt.setInt(2, tutorId);
            stmt.setInt(3, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }









}
