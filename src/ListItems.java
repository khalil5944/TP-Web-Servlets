
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ListItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        ArrayList<String> items =
            (ArrayList<String>) session.getAttribute("items");

        Map<String, Integer> itemCounts =
            (Map<String, Integer>) session.getAttribute("itemCounts");

        if (items == null) {
            items = new ArrayList<String>();
            session.setAttribute("items", items);
        }

        if (itemCounts == null) {
            itemCounts = new HashMap<String, Integer>();
            session.setAttribute("itemCounts", itemCounts);
        }

        displayItems(request, response, items, itemCounts);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String newItem = request.getParameter("newItem");

        HttpSession session = request.getSession();

        ArrayList<String> items =
            (ArrayList<String>) session.getAttribute("items");

        Map<String, Integer> itemCounts =
            (Map<String, Integer>) session.getAttribute("itemCounts");

        if (items == null) {
            items = new ArrayList<String>();
        }

        if (itemCounts == null) {
            itemCounts = new HashMap<String, Integer>();
        }

        if (newItem != null && !newItem.trim().isEmpty()) {
            newItem = newItem.trim();

            // Keep every entry, including duplicates.
            items.add(newItem);

            // Count how many times this item was entered.
            int count = itemCounts.getOrDefault(newItem, 0);
            itemCounts.put(newItem, count + 1);
        }

        session.setAttribute("items", items);
        session.setAttribute("itemCounts", itemCounts);

        displayItems(request, response, items, itemCounts);
    }

    private void displayItems(
            HttpServletRequest request,
            HttpServletResponse response,
            ArrayList<String> items,
            Map<String, Integer> itemCounts)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        response.getWriter().println(
            "<!DOCTYPE html><html><head><title>My Items</title></head><body>"
            + "<h2>My Items</h2>"
        );

        if (items.isEmpty()) {
            response.getWriter().println("<p>No items entered yet.</p>");
        } else {
            response.getWriter().println("<h3>All entries</h3><ul>");

            for (String item : items) {
                response.getWriter().println(
                    "<li>" + escape(item) + "</li>"
                );
            }

            response.getWriter().println("</ul>");

            response.getWriter().println(
                "<h3>Number of times each item was entered</h3><ul>"
            );

            for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
                response.getWriter().println(
                    "<li>" + escape(entry.getKey())
                    + " : " + entry.getValue() + " time(s)</li>"
                );
            }

            response.getWriter().println("</ul>");
        }

        response.getWriter().println(
            "<p><a href='" + request.getContextPath()
            + "/items.html'>Add another item</a></p>"
            + "<p><a href='" + request.getContextPath()
            + "/index.html'>Back to home</a></p>"
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
