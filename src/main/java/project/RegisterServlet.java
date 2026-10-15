
package project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String gender = request.getParameter("gender");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/usermanagement",
                    "root",
                    "root"
            );

            String sql = "INSERT INTO users "
                       + "(name, gender, password, phone, email, role) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, gender);
            ps.setString(3, password);
            ps.setString(4, phone);
            ps.setString(5, email);
            ps.setString(6, "USER");

            int result = ps.executeUpdate();

            if (result > 0) {

                response.getWriter().println("Registration Successful!");
                response.getWriter().println("Your details have been saved.");
                response.getWriter().println("Please go back to the login page.");

            } else {

                response.getWriter().println("Registration Failed!");

            }

            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println("Error occurred.");
        }
    }
}