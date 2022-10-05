package br.com.alura.ecommerce;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.math.BigDecimal;
import java.util.UUID;

public class NewOrderServlet extends HttpServlet {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            var email = req.getParameter("email");

            var orderID = UUID.randomUUID().toString();
            var value = new BigDecimal(req.getParameter("value"));

            var order = new Order(orderID, value, email);
            orderDispatcher.send("ECOMMERCE_NEWORDER", email, order);

            var emailCode = new Email("New order processing", "Thank you for your order! We are processing your order!");
            emailDispatcher.send("ECOMMERCE_SENDEMAIL", email, emailCode);

            System.out.println("New order sent successfuly! --<" + orderID + ">");

            resp.setStatus(200);
            resp.getWriter().println("New order sent successfuly! --<" + orderID + ">--<" + email + ">--<" + value + ">");
        } catch (Exception e) {
            throw new ServletException(e);
        }


    }
}
