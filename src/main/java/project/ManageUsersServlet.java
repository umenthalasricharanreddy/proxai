
package project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ManageUsersServlet")
public class ManageUsersServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Manage Users</title>");

        out.println("<style>");

        out.println("body {");
        out.println("font-family: Arial;");
        out.println("background: #111;");
        out.println("color: white;");
        out.println("padding: 30px;");
        out.println("}");

        out.println("h1 {");
        out.println("text-align: center;");
        out.println("color: #ffffff;");
        out.println("}");

        out.println("table {");
        out.println("width: 100%;");
        out.println("border-collapse: collapse;");
        out.println("margin-top: 30px;");
        out.println("background: white;");
        out.println("color: black;");
        out.println("}");

        out.println("th, td {");
        out.println("padding: 12px;");
        out.println("border: 1px solid #ccc;");
        out.println("text-align: center;");
        out.println("}");

        out.println("th {");
        out.println("background: #6a11cb;");
        out.println("color: white;");
        out.println("}");

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        out.println("<h1>Manage Users</h1>");

        out.println("<table>");

        out.println("<tr>");
        out.println("<th>ID</th>");
        out.println("<th>Name</th>");
        out.println("<th>Email</th>");
        out.println("<th>Gender</th>");
        out.println("<th>Phone</th>");
        out.println("<th>Role</th>");
        out.println("</tr>");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/usermanagement",
                    "root",
                    "root"
            );

            String sql = "SELECT id, name, email, gender, phone, role FROM users";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                out.println("<tr>");

                out.println("<td>" + rs.getInt("id") + "</td>");

                out.println("<td>" + rs.getString("name") + "</td>");

                out.println("<td>" + rs.getString("email") + "</td>");

                out.println("<td>" + rs.getString("gender") + "</td>");

                out.println("<td>" + rs.getString("phone") + "</td>");

                out.println("<td>" + rs.getString("role") + "</td>");

                out.println("</tr>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            out.println("<tr>");
            out.println("<td colspan='6'>");
            out.println("Database Error: " + e.getMessage());
            out.println("</td>");
            out.println("</tr>");
        }

        out.println("</table>");

        out.println("</body>");

        out.println("</html>");
    }
}

