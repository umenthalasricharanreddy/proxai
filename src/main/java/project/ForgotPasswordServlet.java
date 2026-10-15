package project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Random;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {

            // MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Database connection
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/usermanagement",
                    "root",
                    "root"
            );

            // Check whether email exists
            String sql = "SELECT * FROM users WHERE email = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // Generate 6-digit OTP
                Random random = new Random();
                int otp = 100000 + random.nextInt(900000);

                // Store email and OTP in session
                HttpSession httpSession = request.getSession();

                httpSession.setAttribute("resetEmail", email);
                httpSession.setAttribute("resetOTP", otp);

                // Gmail SMTP settings
                Properties properties = new Properties();

                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.starttls.required", "true");
                properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
                properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

                // Create Jakarta Mail session
                Session mailSession = Session.getInstance(
                        properties,
                        new Authenticator() {

                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {

                                return new PasswordAuthentication(
                                        "sricharanreddy111@gmail.com",
                                        "yraadhzbfdghmbad"
                                );
                            }
                        }
                );

                // Create email
                Message message = new MimeMessage(mailSession);

                message.setFrom(
                        new InternetAddress("sricharanreddy111@gmail.com")
                );

                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(email)
                );

                message.setSubject(
                        "ProxyAIproject - Password Reset OTP"
                );

                message.setText(
                        "Hello,\n\n"
                        + "Your OTP for resetting your ProxyAIproject password is:\n\n"
                        + otp
                        + "\n\n"
                        + "Please use this OTP to reset your password.\n\n"
                        + "If you did not request a password reset, please ignore this email."
                );

                // Send email
                Transport.send(message);

                // Success page
                out.println("<html>");
                out.println("<head>");
                out.println("<title>OTP Sent</title>");

                out.println("<style>");

                out.println("* {");
                out.println("margin: 0;");
                out.println("padding: 0;");
                out.println("box-sizing: border-box;");
                out.println("font-family: Arial, sans-serif;");
                out.println("}");

                out.println("body {");
                out.println("min-height: 100vh;");
                out.println("display: flex;");
                out.println("justify-content: center;");
                out.println("align-items: center;");
                out.println("background: linear-gradient(135deg, #050509, #160b35, #071d45);");
                out.println("color: white;");
                out.println("}");

                out.println(".card {");
                out.println("width: 430px;");
                out.println("padding: 45px;");
                out.println("text-align: center;");
                out.println("background: rgba(20,20,35,0.95);");
                out.println("border: 1px solid rgba(150,120,255,0.4);");
                out.println("border-radius: 20px;");
                out.println("box-shadow: 0 0 35px rgba(100,60,255,0.35);");
                out.println("}");

                out.println(".icon {");
                out.println("width: 70px;");
                out.println("height: 70px;");
                out.println("margin: 0 auto 20px;");
                out.println("border-radius: 50%;");
                out.println("display: flex;");
                out.println("align-items: center;");
                out.println("justify-content: center;");
                out.println("background: linear-gradient(135deg,#7c3aed,#2563eb);");
                out.println("font-size: 32px;");
                out.println("}");

                out.println("h2 {");
                out.println("margin-bottom: 15px;");
                out.println("}");

                out.println("p {");
                out.println("color: #aaa;");
                out.println("line-height: 1.6;");
                out.println("}");

                out.println(".email {");
                out.println("margin: 15px 0 25px;");
                out.println("color: #aeb8ff;");
                out.println("font-weight: bold;");
                out.println("}");

                out.println("a {");
                out.println("display: inline-block;");
                out.println("padding: 13px 25px;");
                out.println("background: linear-gradient(135deg,#7c3aed,#2563eb);");
                out.println("color: white;");
                out.println("text-decoration: none;");
                out.println("border-radius: 8px;");
                out.println("font-weight: bold;");
                out.println("}");

                out.println("</style>");
                out.println("</head>");

                out.println("<body>");

                out.println("<div class='card'>");

                out.println("<div class='icon'>✓</div>");

                out.println("<h2>OTP Sent Successfully</h2>");

                out.println("<p>An OTP has been sent to:</p>");

                out.println("<div class='email'>" + email + "</div>");

                out.println("<p>Check your email and enter the OTP to continue.</p>");

                out.println("<br>");

                out.println(
                        "<a href='verify-otp.html'>Continue to Verify OTP</a>"
                );

                out.println("</div>");

                out.println("</body>");
                out.println("</html>");

            } else {

                // Email not found
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Email Not Found</title>");

                out.println("<style>");

                out.println("body {");
                out.println("min-height: 100vh;");
                out.println("display: flex;");
                out.println("justify-content: center;");
                out.println("align-items: center;");
                out.println("background: linear-gradient(135deg,#050509,#160b35,#071d45);");
                out.println("color: white;");
                out.println("font-family: Arial,sans-serif;");
                out.println("}");

                out.println(".card {");
                out.println("width: 430px;");
                out.println("padding: 45px;");
                out.println("text-align: center;");
                out.println("background: rgba(20,20,35,0.95);");
                out.println("border-radius: 20px;");
                out.println("box-shadow: 0 0 35px rgba(100,60,255,0.35);");
                out.println("}");

                out.println("p {");
                out.println("color: #aaa;");
                out.println("margin: 20px 0;");
                out.println("}");

                out.println("a {");
                out.println("display: inline-block;");
                out.println("padding: 13px 25px;");
                out.println("background: linear-gradient(135deg,#7c3aed,#2563eb);");
                out.println("color: white;");
                out.println("text-decoration: none;");
                out.println("border-radius: 8px;");
                out.println("}");

                out.println("</style>");
                out.println("</head>");

                out.println("<body>");

                out.println("<div class='card'>");

                out.println("<h2>Email Not Found</h2>");

                out.println("<p>This email is not registered in the system.</p>");

                out.println(
                        "<a href='forgot-password.html'>Try Again</a>"
                );

                out.println("</div>");

                out.println("</body>");
                out.println("</html>");
            }

            // Close database resources
            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Error</title>");
            out.println("</head>");

            out.println("<body>");

            out.println("<h2>Error occurred</h2>");

            out.println("<p>" + e.getMessage() + "</p>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}