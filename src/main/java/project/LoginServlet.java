
package project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/usermanagement","root","root" );

            String sql = "SELECT name, email, role FROM users "  + "WHERE email = ? AND password = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String name = rs.getString("name");
                String userEmail = rs.getString("email");
                String role = rs.getString("role");

                String token = JwtUtil.generateToken(userEmail, role);
                HttpSession session = request.getSession();
                session.setAttribute("name", name);
                session.setAttribute("email", userEmail);
                session.setAttribute("role", role);

               
                session.setAttribute("token", token);

              
                if ("ADMIN".equalsIgnoreCase(role)) {

                    response.sendRedirect("admin.html");

                } else {

                    response.sendRedirect("user.html");
                }

            } else {

                response.setContentType("text/html");

                response.getWriter().println("<h2>Login Failed!</h2>");
                response.getWriter().println("<p>Invalid email or password.</p>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();
            response.setContentType("text/html");
           response.getWriter().println("<h2>Database Error</h2>");
            response.getWriter().println("<p>" + e.getMessage() + "</p>");
        }
    }
}

