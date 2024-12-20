package org.example.network;
import lombok.Data;
import org.example.dao.DataBaseManager;
import org.example.models.Menu;
import org.example.models.Order;
import org.example.models.OrderItem;
import org.example.services.OrderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
public class ServerManager {
        private final OrderService orderService;


        public void handleClient(Creator creator) throws IOException {
            try {
                while (true)
                {
                    String request = creator.readLine();
                    System.out.println("request: " + request);
                    if ((request.equalsIgnoreCase("menu"))) {
                        sendMenuToClient(creator);
                    }
                    else if ((request.equalsIgnoreCase("order"))) {
                        processOrder(creator);
                    }
                    else if ((request.equalsIgnoreCase("status"))) {
                        checkOrderStatus(creator);
                    }
                    else if ((request.equalsIgnoreCase("exit"))) {
                        exitFromServer(creator);
                        break;
                    }
                    else {
                        creator.writeLine("Unknown command, try 'menu' or 'order' or 'status' " );
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            try {
                creator.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendMenuToClient(Creator creator) throws IOException {
            creator.writeLine("That's a menu");
            Menu.printMenuToClient(creator);
            creator.writeLine("Please type 'order' to make the order");
            creator.writeLine("end");
        }

        private void processOrder(Creator creator) throws IOException {
            creator.writeLine("Please provide your name: ");
            creator.writeLine("end");
            String customerName = creator.readLine();

            creator.writeLine("Please provide your delivery address: ");
            creator.writeLine("end");
            String address = creator.readLine();

            List<OrderItem> orderItemList = new ArrayList<>();
            while (true) {
                creator.writeLine("Please enter the dish name (or 'DONE' to finish): ");
                creator.writeLine("end");
                String dishName = creator.readLine();
                if (dishName.equalsIgnoreCase("DONE")) {
                    break;
                }
                creator.writeLine("Please enter the quantity " + dishName + ":");
                creator.writeLine("end");
                try {
                    int quantity = Integer.parseInt(creator.readLine());
                    orderItemList.add(new OrderItem(dishName, quantity));
                }
                catch (NumberFormatException e) {
                    creator.writeLine("Please enter an integer number");
                }


            }
            int id = orderService.addOrder( customerName, address, orderItemList);
            creator.writeLine("Your order has been placed successfully! Your Order number is: " + id);
            creator.writeLine("end");
            System.out.println("Order № " + id + " Successfully created");

        }

        private void checkOrderStatus(Creator creator) throws IOException {
            creator.writeLine("Please enter your order ID: ");
            creator.writeLine("end");
            int orderID =Integer.parseInt(creator.readLine());
            String status =  orderService.checkOrderStatus(orderID);
            creator.writeLine(status);
            creator.writeLine("end");
        }
        private void exitFromServer(Creator creator) throws IOException {
            creator.writeLine("Thank you for your time. Your session is over");
            creator.writeLine("end");
        }
}
