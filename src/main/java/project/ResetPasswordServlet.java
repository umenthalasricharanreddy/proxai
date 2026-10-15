
package project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get passwords from reset-password.html
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        // Create session
        HttpSession session = request.getSession();

        // Get email stored during Forgot Password
        String email = (String) session.getAttribute("resetEmail");

        // Check whether OTP was verified
        Boolean otpVerified = (Boolean) session.getAttribute("otpVerified");

        // Check OTP verification
        if (otpVerified == null || !otpVerified) {

            out.println("<html>");
            out.println("<body>");

            out.println("<h2>OTP verification required</h2>");

            out.println("<p>Please verify the OTP before resetting your password.</p>");

            out.println("<a href='forgot-password.html'>Forgot Password</a>");

            out.println("</body>");
            out.println("</html>");

            return;
        }

        // Check passwords
        if (!newPassword.equals(confirmPassword)) {

            out.println("<html>");
            out.println("<body>");

            out.println("<h2>Passwords do not match</h2>");

            out.println("<p>Please enter the same password in both fields.</p>");

            out.println("<a href='reset-password.html'>Try Again</a>");

            out.println("</body>");
            out.println("</html>");

            return;
        }

        try {

            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to database
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/usermanagement",
                    "root",
                    "root"
            );

            // Update password
            String sql = "UPDATE users SET password = ? WHERE email = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, newPassword);

            ps.setString(2, email);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {

                // Remove reset information from session
                session.removeAttribute("resetOTP");
                session.removeAttribute("otpVerified");
                session.removeAttribute("resetEmail");

                out.println("<html>");
                out.println("<body>");

                out.println("<h2>Password Reset Successfully</h2>");

                out.println("<p>Your password has been changed successfully.</p>");

                out.println("<br>");

                out.println("<a href='login.html'>Go to Login</a>");

                out.println("</body>");
                out.println("</html>");

            } else {

                out.println("<html>");
                out.println("<body>");

                out.println("<h2>Password Reset Failed</h2>");

                out.println("<p>User email was not found.</p>");

                out.println("<a href='forgot-password.html'>Try Again</a>");

                out.println("</body>");
                out.println("</html>");
            }

            ps.close();

            con.close();

        } catch (Exception e) {

            out.println("<html>");
            out.println("<body>");

            out.println("<h2>Error occurred</h2>");

            out.println("<p>" + e.getMessage() + "</p>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}

