package project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String email = (String) session.getAttribute("email");

        if (email == null) {

            response.getWriter().println("<h2>Please login first.</h2>");
            return;
        }

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/usermanagement",
                    "root",
                    "root"
            );

            String sql = "SELECT name, gender, phone, email, role FROM users WHERE email=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String phone = rs.getString("phone");
                String userEmail = rs.getString("email");
                String role = rs.getString("role");

                response.setContentType("text/html");

                response.getWriter().println("<html>");
                response.getWriter().println("<head>");
                response.getWriter().println("<title>My Profile</title>");

                response.getWriter().println("<style>");

                response.getWriter().println("* {");
                response.getWriter().println("margin: 0;");
                response.getWriter().println("padding: 0;");
                response.getWriter().println("box-sizing: border-box;");
                response.getWriter().println("font-family: Arial;");
                response.getWriter().println("}");

                response.getWriter().println("body {");
                response.getWriter().println("min-height: 100vh;");
                response.getWriter().println("display: flex;");
                response.getWriter().println("justify-content: center;");
                response.getWriter().println("align-items: center;");
                response.getWriter().println("background: #000000;");
                response.getWriter().println("}");

                response.getWriter().println(".profile-card {");
                response.getWriter().println("width: 450px;");
                response.getWriter().println("padding: 40px;");
                response.getWriter().println("background: #111111;");
                response.getWriter().println("border-radius: 20px;");
                response.getWriter().println("box-shadow: 0 0 25px #444444;");
                response.getWriter().println("}");

                response.getWriter().println("h1 {");
                response.getWriter().println("text-align: center;");
                response.getWriter().println("color: white;");
                response.getWriter().println("margin-bottom: 30px;");
                response.getWriter().println("}");

                response.getWriter().println(".profile-info {");
                response.getWriter().println("margin-bottom: 15px;");
                response.getWriter().println("padding: 14px;");
                response.getWriter().println("background: #222222;");
                response.getWriter().println("border-radius: 8px;");
                response.getWriter().println("color: white;");
                response.getWriter().println("}");

                response.getWriter().println(".profile-info strong {");
                response.getWriter().println("color: #00ffff;");
                response.getWriter().println("}");

                response.getWriter().println(".buttons {");
                response.getWriter().println("display: flex;");
                response.getWriter().println("gap: 12px;");
                response.getWriter().println("margin-top: 25px;");
                response.getWriter().println("}");

                response.getWriter().println(".buttons a {");
                response.getWriter().println("flex: 1;");
                response.getWriter().println("padding: 12px;");
                response.getWriter().println("text-align: center;");
                response.getWriter().println("text-decoration: none;");
                response.getWriter().println("background: white;");
                response.getWriter().println("color: black;");
                response.getWriter().println("border-radius: 8px;");
                response.getWriter().println("font-weight: bold;");
                response.getWriter().println("}");

                response.getWriter().println(".buttons a:hover {");
                response.getWriter().println("background: #00ffff;");
                response.getWriter().println("}");

                response.getWriter().println("</style>");

                response.getWriter().println("</head>");

                response.getWriter().println("<body>");

                response.getWriter().println("<div class='profile-card'>");

                response.getWriter().println("<h1>My Profile</h1>");

                response.getWriter().println(
                    "<div class='profile-info'><strong>Name:</strong> "
                    + name + "</div>"
                );

                response.getWriter().println(
                    "<div class='profile-info'><strong>Gender:</strong> "
                    + gender + "</div>"
                );

                response.getWriter().println(
                    "<div class='profile-info'><strong>Phone:</strong> "
                    + phone + "</div>"
                );

                response.getWriter().println(
                    "<div class='profile-info'><strong>Email:</strong> "
                    + userEmail + "</div>"
                );

                response.getWriter().println(
                    "<div class='profile-info'><strong>Role:</strong> "
                    + role + "</div>"
                );

                response.getWriter().println("<div class='buttons'>");

                response.getWriter().println(
                    "<a href='updateProfile.html'>Update Profile</a>"
                );

                response.getWriter().println(
                    "<a href='user.html'>Back</a>"
                );

                response.getWriter().println("</div>");

                response.getWriter().println("</div>");

                response.getWriter().println("</body>");

                response.getWriter().println("</html>");

            } else {

                response.getWriter().println("<h2>Profile not found.</h2>");

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            response.setContentType("text/html");

            response.getWriter().println("<h2>Database Error</h2>");
            response.getWriter().println("<p>" + e.getMessage() + "</p>");
        }
    }
}