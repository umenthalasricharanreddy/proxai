
package project;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/VerifyOTPServlet")
public class VerifyOTPServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String enteredOTP = request.getParameter("otp");

        HttpSession session = request.getSession();

        Object savedOTP = session.getAttribute("resetOTP");

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        if (savedOTP != null && enteredOTP.equals(savedOTP.toString())) {

            session.setAttribute("otpVerified", true);

            response.sendRedirect("reset-password.html");

        } else {

            out.println("<html>");
            out.println("<body>");

            out.println("<h2>Invalid OTP</h2>");

            out.println("<p>The OTP you entered is incorrect.</p>");

            out.println("<a href='verify-otp.html'>Try Again</a>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}

