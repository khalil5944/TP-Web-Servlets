
import java.io.IOException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ShowSession extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Count visits to this servlet during this session.
        Integer visits = (Integer) session.getAttribute("visits");

        if (visits == null) {
            visits = 1;
        } else {
            visits++;
        }

        session.setAttribute("visits", visits);

        response.setContentType("text/html;charset=UTF-8");

        String fullName = (String) session.getAttribute("fullName");
        String birthDate = (String) session.getAttribute("birthDate");

        response.getWriter().println(
            "<!DOCTYPE html><html><head><title>HTTP Session</title></head><body>"
        );

        if (fullName == null || birthDate == null) {

            response.getWriter().println(
                "<h2>Welcome to my site!</h2>"
                + "<p>This is your first visit in this session.</p>"
                + "<p>Visits: " + visits + "</p>"
                + "<h3>Enter your information</h3>"
                + "<form action='" + request.getContextPath()
                + "/ShowSession' method='POST'>"
                + "Full name: <input type='text' name='fullName' required><br><br>"
                + "Date of birth: <input type='date' name='birthDate' required><br><br>"
                + "<input type='submit' value='Submit'>"
                + "</form>"
            );

        } else {

            LocalDate birth = LocalDate.parse(birthDate);
            LocalDate today = LocalDate.now();

            // Find the next birthday, including the case where
            // the birthday is February 29.
            MonthDay birthday = MonthDay.from(birth);
            LocalDate nextBirthday = birthday.atYear(today.getYear());

            if (nextBirthday.isBefore(today)) {
                nextBirthday = birthday.atYear(today.getYear() + 1);
            }

            long days = ChronoUnit.DAYS.between(today, nextBirthday);

            response.getWriter().println(
                "<h2>Welcome back!</h2>"
                + "<p>Hi, " + escape(fullName) + ".</p>"
                + "<p>There are " + days
                + " days to your birthday.</p>"
                + "<p>Visits in this session: " + visits + "</p>"
            );
        }

        response.getWriter().println(
            "<p><a href='" + request.getContextPath()
            + "/ShowSession'>Refresh / visit again</a></p>"
            + "<p><a href='" + request.getContextPath()
            + "/index.html'>Back to home</a></p>"
            + "</body></html>"
        );
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String birthDate = request.getParameter("birthDate");

        if (fullName == null || fullName.trim().isEmpty()
                || birthDate == null || birthDate.trim().isEmpty()) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Full name and date of birth are required."
            );
            return;
        }

        try {
            LocalDate.parse(birthDate);
        } catch (java.time.format.DateTimeParseException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid date of birth."
            );
            return;
        }

        HttpSession session = request.getSession();

        session.setAttribute("fullName", fullName.trim());
        session.setAttribute("birthDate", birthDate);

        // Show the result without counting the form submission
        // as another visit.
        response.setContentType("text/html;charset=UTF-8");

        response.getWriter().println(
            "<!DOCTYPE html><html><head><title>Welcome</title></head><body>"
            + "<h2>Welcome to my site, " + escape(fullName.trim()) + "!</h2>"
            + "<p>Your information has been saved in this session.</p>"
            + "<p><a href='" + request.getContextPath()
            + "/ShowSession'>Continue</a></p>"
            + "</body></html>"
        );
    }

    private String escape(String value) {
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
