import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisitCounter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        int visits = 0;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("visits")) {
                    try {
                        visits = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        visits = 0;
                    }
                    break;
                }
            }
        }

        visits++;

        Cookie counter = new Cookie("visits", String.valueOf(visits));
        counter.setPath(request.getContextPath());
        counter.setMaxAge(365 * 24 * 60 * 60);
        response.addCookie(counter);

        response.setContentType("text/html;charset=UTF-8");

        response.getWriter().println(
            "<!DOCTYPE html><html><head><title>Visit Counter</title></head><body>"
            + "<h2>Visit Counter</h2>"
            + "<p>You have visited this page " + visits + " times.</p>"
            + "<p><a href='" + request.getContextPath()
            + "/VisitCounter'>Visit again</a></p>"
            + "<p><a href='" + request.getContextPath()
            + "/index.html'>Back to form</a></p>"
            + "</body></html>"
        );
    }
}