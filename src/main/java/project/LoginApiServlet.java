package project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/api/login")
public class LoginApiServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Response will be JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // Read JSON request
        BufferedReader reader = request.getReader();

        StringBuilder requestBody = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        String json = requestBody.toString();

        // Get email and password from JSON
        String email = getJsonValue(json, "email");
        String password = getJsonValue(json, "password");

        // Check input
        if (email == null || password == null) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            out.println(
                "{"
                + "\"message\":\"Email and password are required\""
                + "}"
            );

            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            // MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Database connection
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/usermanagement",
                "root",
                "root"
            );

            // Login query
            String sql =
                "SELECT name, email, role FROM users "
                + "WHERE email = ? AND password = ?";

            ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            rs = ps.executeQuery();

            if (rs.next()) {

                String name = rs.getString("name");
                String userEmail = rs.getString("email");
                String role = rs.getString("role");

                // Generate JWT
                String token = JwtUtil.generateToken(
                    userEmail,
                    role
                );

                // Successful response
                response.setStatus(
                    HttpServletResponse.SC_OK
                );

                out.println(
                    "{"
                    + "\"message\":\"Login successful\","
                    + "\"name\":\"" + name + "\","
                    + "\"email\":\"" + userEmail + "\","
                    + "\"role\":\"" + role + "\","
                    + "\"token\":\"" + token + "\""
                    + "}"
                );

            } else {

                // Login failed
                response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
                );

                out.println(
                    "{"
                    + "\"message\":\"Invalid email or password\""
                    + "}"
                );
            }

        } catch (Exception e) {

            response.setStatus(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            out.println(
                "{"
                + "\"message\":\"Database error\","
                + "\"error\":\"" + e.getMessage() + "\""
                + "}"
            );

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }

                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                // Nothing required here
            }
        }
    }

    // Method to get values from JSON
    private String getJsonValue(String json, String key) {

        String search = "\"" + key + "\"";

        int start = json.indexOf(search);

        if (start == -1) {
            return null;
        }

        start = json.indexOf(":", start);

        if (start == -1) {
            return null;
        }

        start++;

        while (start < json.length()
                && Character.isWhitespace(json.charAt(start))) {

            start++;
        }

        if (start < json.length()
                && json.charAt(start) == '"') {

            start++;

            int end = json.indexOf("\"", start);

            if (end != -1) {
                return json.substring(start, end);
            }
        }

        return null;
    }
}