package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


@WebServlet(name = "OrderController", urlPatterns = {"/order"}, loadOnStartup = 1)
public class OrderController extends HttpServlet {
    OrderDao orderDao = OrderDaoMem.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());
        String name = request.getParameter("fname") + " " + request.getParameter("lname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String namesAndQuantities = request.getParameter("namesAndQuantities");
        namesAndQuantities = namesAndQuantities.substring(1, namesAndQuantities.length()-1);
        List<String> productsDetails = Arrays.asList(namesAndQuantities.split(","));
        String total = request.getParameter("total");
        System.out.println(namesAndQuantities);
        System.out.println(name);
        System.out.println(email);
        context.setVariable("name", name);
        context.setVariable("email", email);
        context.setVariable("phone", phone);
        context.setVariable("address", address);
        context.setVariable("total", total);
        context.setVariable("productsDetails", productsDetails);
        orderDao.add(new Order(1, total, name, email, phone, address, productsDetails));
//        String orderInfo = orderDao.find(1).getOrderInfo();
        engine.process("product/payment.html", context, response.getWriter());
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());
        engine.process("product/check_out.html", context, response.getWriter());
    }


}
