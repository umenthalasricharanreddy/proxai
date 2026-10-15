package project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String oldEmail = (String) session.getAttribute("email");

        String name = request.getParameter("name");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/usermanagement","root","root" );

            String sql = "UPDATE users SET name=?, gender=?, phone=?, email=?, password=? WHERE email=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, gender);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, password);
            ps.setString(6, oldEmail);

            int result = ps.executeUpdate();

            if (result > 0) {

            	session.setAttribute("email", email);
            	session.setAttribute("name", name);

            	response.setContentType("text/html");

            	response.getWriter().println("<html>");
            	response.getWriter().println("<head>");
            	response.getWriter().println("<title>Profile Updated</title>");

            	response.getWriter().println("<style>");
            	response.getWriter().println("body {");
            	response.getWriter().println("font-family: Arial;");
            	response.getWriter().println("background: linear-gradient(135deg, #4f46e5, #7c3aed);");
            	response.getWriter().println("display: flex;");
            	response.getWriter().println("justify-content: center;");
            	response.getWriter().println("align-items: center;");
            	response.getWriter().println("height: 100vh;");
            	response.getWriter().println("}");

            	response.getWriter().println(".card {");
            	response.getWriter().println("background: white;");
            	response.getWriter().println("padding: 40px;");
            	response.getWriter().println("border-radius: 20px;");
            	response.getWriter().println("text-align: center;");
            	response.getWriter().println("box-shadow: 0 20px 50px rgba(0,0,0,0.3);");
            	response.getWriter().println("}");

            	response.getWriter().println("h1 { color: #312e81; }");

            	response.getWriter().println("a {");
            	response.getWriter().println("background: #4f46e5;");
            	response.getWriter().println("color: white;");
            	response.getWriter().println("padding: 12px 25px;");
            	response.getWriter().println("text-decoration: none;");
            	response.getWriter().println("border-radius: 8px;");
            	response.getWriter().println("}");

            	response.getWriter().println("</style>");
            	response.getWriter().println("</head>");

            	response.getWriter().println("<body>");

            	response.getWriter().println("<div class='card'>");
            	response.getWriter().println("<h1>✅ Profile Updated Successfully!</h1>");
            	response.getWriter().println("<p>Your profile details have been updated successfully.</p>");
            	response.getWriter().println("<br>");
            	response.getWriter().println("<a href='profile.html'>View My Profile</a>");
            	response.getWriter().println("</div>");

            	response.getWriter().println("</body>");
            	response.getWriter().println("</html>");
            

            	}


          

                
             else {

                response.getWriter().println("Profile update failed.");

            }

            con.close();

        } catch (Exception e) {

            response.getWriter().println(e.getMessage());

        }
    }
}