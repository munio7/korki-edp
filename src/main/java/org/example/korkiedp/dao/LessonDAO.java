package org.example.korkiedp.dao;

import org.example.korkiedp.model.Lesson;
import org.example.korkiedp.util.Database;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LessonDAO {

    public static List<Lesson> findByTutorId(int tutorId) {
        String sql = "SELECT * FROM lesson WHERE tutor_id = ?";
        return fetchLessons(sql, tutorId);
    }

    public static List<Lesson> findByTutorAndStudentId(int tutorId, int studentId) {
        String sql = "SELECT * FROM lesson WHERE tutor_id = ? AND student_id = ?";
        return fetchLessons(sql, tutorId, studentId);
    }

    public static List<Lesson> findByTutorAndStudentIdFinished(int tutorId, int studentId) {
        String sql = "SELECT * FROM lesson " +
                "WHERE tutor_id = ? " +
                "AND student_id = ? " +
                "AND (date + start_time + (duration_minutes || ' minutes')::interval) < CURRENT_TIMESTAMP";
        return fetchLessons(sql, tutorId, studentId);
    }
    public static List<Lesson> findByTutorAndDate(int tutorId, LocalDate date) {
        String sql = "SELECT * FROM lesson WHERE tutor_id = ? AND date = ?";
        return fetchLessons(sql, tutorId, Date.valueOf(date));
    }
    public static boolean deleteById(int id) {
        String sql = "DELETE FROM lesson WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean save(Lesson lesson) {
        String sql = """
            INSERT INTO lesson (tutor_id, student_id, date, start_time, duration_minutes, price,
                                 paid, attendance, canceled, cancel_reason, updated_by,
                                 created_at, modified_at, topic, subject)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try(Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            fillStatement(stmt, lesson, false);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    lesson.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error while saving lesson: " + e.getMessage());
            return false;
        }
        return false;
    }

    public static boolean update(Lesson lesson) {
        String sql = """
            UPDATE lesson SET tutor_id = ?, student_id = ?, date = ?, start_time = ?, duration_minutes = ?, 
                               price = ?, paid = ?, attendance = ?, canceled = ?, cancel_reason = ?, updated_by = ?, 
                               created_at = ?, modified_at = ?, topic = ?, subject = ?
            WHERE id = ?
            """;
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            fillStatement(stmt, lesson, true);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ========================= HELPERS ============================

    private static List<Lesson> fetchLessons(String sql, Object... params) {
        List<Lesson> lessons = new ArrayList<>();
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lessons.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lessons;
    }

    private static Lesson mapRow(ResultSet rs) throws SQLException {
        Lesson l = new Lesson();
        l.setId(rs.getInt("id"));
        l.setTutorId(rs.getInt("tutor_id"));
        l.setStudentId(rs.getInt("student_id"));
        l.setDate(rs.getDate("date").toLocalDate());
        l.setStartTime(rs.getTime("start_time").toLocalTime());
        l.setDurationMinutes(rs.getInt("duration_minutes"));
        l.setPrice(rs.getBigDecimal("price"));
        l.setPaid(rs.getBoolean("paid"));
        l.setAttendance((Boolean) rs.getObject("attendance"));
        l.setCanceled((Boolean) rs.getObject("canceled"));
        l.setCancelReason(rs.getString("cancel_reason"));
        l.setUpdatedBy(rs.getInt("updated_by"));
        l.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        l.setModifiedAt(rs.getTimestamp("modified_at").toLocalDateTime());
        l.setTopic(rs.getString("topic"));
        l.setSubject(rs.getString("subject"));
        return l;
    }

    private static void fillStatement(PreparedStatement stmt, Lesson l, boolean includeIdLast) throws SQLException {
        stmt.setInt(1, l.getTutorId());
        stmt.setInt(2, l.getStudentId());
        stmt.setDate(3, Date.valueOf(l.getDate()));
        stmt.setTime(4, Time.valueOf(l.getStartTime()));
        stmt.setInt(5, l.getDurationMinutes());
        stmt.setBigDecimal(6, l.getPrice());
        stmt.setBoolean(7, l.isPaid());

        // Nullable attendance
        if (l.getAttendance() != null) {
            stmt.setBoolean(8, l.getAttendance());
        } else {
            stmt.setNull(8, Types.BOOLEAN);
        }

        // Nullable canceled
        if (l.isCanceled() != null) {
            stmt.setBoolean(9, l.isCanceled());
        } else {
            stmt.setNull(9, Types.BOOLEAN);
        }

        stmt.setString(10, l.getCancelReason());
        stmt.setInt(11, l.getUpdatedBy());
        stmt.setTimestamp(12, Timestamp.valueOf(l.getCreatedAt()));
        stmt.setTimestamp(13, Timestamp.valueOf(l.getModifiedAt()));
        stmt.setString(14, l.getTopic());
        stmt.setString(15, l.getSubject());

        if (includeIdLast) {
            stmt.setInt(16, l.getId());
        }
    }

    public static int countFinishedLessons(int tutorId) {
        String sql = """
        SELECT COUNT(*) 
        FROM lesson 
        WHERE tutor_id = ? 
          AND (date + start_time + (duration_minutes || ' minutes')::interval) < CURRENT_TIMESTAMP
            AND canceled IS NOT TRUE
    """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tutorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error counting finished lessons for tutor ID " + tutorId, e);
        }
    }
    public static BigDecimal sumFinishedLessonsPrice(int tutorId) {
        String sql = """
        SELECT SUM(price) 
        FROM lesson 
        WHERE tutor_id = ? 
          AND (date + start_time + (duration_minutes || ' minutes')::interval) < CURRENT_TIMESTAMP
          AND canceled IS NOT TRUE
        And paid IS TRUE
    """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tutorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : BigDecimal.ZERO;
            } else {
                return BigDecimal.ZERO;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error summing finished lessons price for tutor ID " + tutorId, e);
        }
    }
    public static List<Lesson> findFinishedLessonsByTutorId(int tutorId) {
        String sql = """
        SELECT * 
        FROM lesson 
        WHERE tutor_id = ? 
          AND (date + start_time + (duration_minutes || ' minutes')::interval) < CURRENT_TIMESTAMP
          AND canceled IS NOT TRUE
          And paid = false
    """;

        return fetchLessons(sql, tutorId);
    }
    public static BigDecimal sumPaidFinishedLessons(int tutorId, int studentId) {
        String sql = """
        SELECT COALESCE(SUM(price), 0)
        FROM lesson
        WHERE tutor_id = ? AND student_id = ? 
          AND paid IS TRUE 
          AND canceled IS NOT TRUE
          AND (date + start_time + (duration_minutes || ' minutes')::interval) < CURRENT_TIMESTAMP
    """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tutorId);
            stmt.setInt(2, studentId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static BigDecimal sumUnpaidFinishedLessons(int tutorId, int studentId) {
        String sql = """
        SELECT COALESCE(SUM(price), 0)
        FROM lesson
        WHERE tutor_id = ? AND student_id = ? 
          AND paid IS FALSE
          AND canceled IS NOT TRUE
          AND (date + start_time + (duration_minutes || ' minutes')::interval) < CURRENT_TIMESTAMP
    """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tutorId);
            stmt.setInt(2, studentId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
