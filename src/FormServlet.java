import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String birthDay = request.getParameter("birthDate");
        String sex = request.getParameter("sex");

        Cookie c1 = new Cookie("firstName", encode(firstName));
        Cookie c2 = new Cookie("LastName", encode(lastName));
        Cookie c3 = new Cookie("birthDay", encode(birthDay));
        Cookie c4 = new Cookie("Sex", encode(sex));

        String path = request.getContextPath();

        Cookie[] cookies = {c1, c2, c3, c4};

        for (Cookie cookie : cookies) {
            cookie.setPath(path);
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }

        response.setContentType("text/html;charset=UTF-8");

        response.getWriter().println(
            "<!DOCTYPE html><html><head><title>Form Result</title></head><body>"
            + "<h2>Information submitted successfully!</h2>"
            + "<p>First name: " + escape(firstName) + "</p>"
            + "<p>Last name: " + escape(lastName) + "</p>"
            + "<p>Birthday: " + escape(birthDay) + "</p>"
            + "<p>Sex: " + escape(sex) + "</p>"
            + "<p><a href='" + path + "/ReadCookie'>Read cookies</a></p>"
            + "<p><a href='" + path + "/VisitCounter'>Visit counter</a></p>"
            + "<p><a href='" + path + "/index.html'>Back to form</a></p>"
            + "</body></html>"
        );
    }

    private String encode(String value) {
        return URLEncoder.encode(
            value == null ? "" : value,
            StandardCharsets.UTF_8
        );
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}