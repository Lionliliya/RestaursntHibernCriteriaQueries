package com.gmail.liliyayalovchenko;

import com.gmail.liliyayalovchenko.controllers.*;
import com.gmail.liliyayalovchenko.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.UnexpectedRollbackException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private EmployeeController employeeController;
    private IngredientController ingredientController;
    private DishController dishController;
    private OrderController orderController;
    private MenuController menuController;
    private WarehouseController warehouseController;
    private ReadyMealController readyMealController;
    private int orderNumber;



    private boolean stopApp;

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml", "hibernate-context.xml");
        Main main = applicationContext.getBean(Main.class);
        main.start();
    }

    private void start() {

        startApplication();
        Scanner sc = new Scanner(System.in);
        String selection = null;
        stopApp = false;

        try {
            orderNumber = orderController.getLastOrder() + 1;
        } catch (RuntimeException ex) {
            LOGGER.error("Could not connect! " + ex);
            System.out.println("Could not connect!");
            stopApp = true;
            selection = "q";
        }


        while (!"q".equals(selection) && !stopApp) {

            selection = sc.next();

            if (selection.equals("d")) {
                goToDishPage(sc, selection);

            } else if (selection.equals("e")) {
                goToEmployeePage(sc, selection);


            } else if (selection.equals("m")) {
                goToMenuPage(sc, selection);

            } else if (selection.equals("o")) {
                goToOrdersPage(sc, selection);

            } else if (selection.equals("rm")) {
                goToReadyMealPage(sc, selection);

            } else if (selection.equals("w")) {
                goToWarehousePage(sc, selection);

            } else if ("q".equals(selection)) {
                System.out.println("Good Bay!");
                stopApp = true;
                LOGGER.info("User left the application");
                sc.close();
                break;
            } else {
                System.out.println("Wrong input!!! Try again");
            }
            System.out.println("Do you want continue? - enter 'y'");
            String next = sc.next();
            if (next.equals("y")) {
                selection = "continue";
                stopApp = false;
                startApplication();
            } else {
                stopApp = true;
                LOGGER.info("User left the application");
                sc.close();
                break;
            }
        }

    }

    private void goToDishPage(Scanner sc, String selection) {
        System.out.println("Dish page. You have following options:");
        System.out.println("Add new dish - enter d01\nRemove dish - enter d02\n" +
                "Get dish by name - enter d03\nGet all dishes - enter d04");
        System.out.println("To exit database - enter q");
        System.out.println("To start menu - enter 'start'");

        while (!"q".equals(selection) && !stopApp) {
            selection = sc.next();
            if (selection.equals("d01")) {
                addNewDish(sc);

            } else if (selection.equals("d02")) {
                deleteDish(sc);

            } else if (selection.equals("d03")) {
                getDishByName(sc);

            } else if (selection.equals("d04")) {
                getAllDishes();

            } else if (selection.equals("start")) {
                break;

            } else if (selection.equals("q")){
                stopApp = true;
                break;
            } else {
                System.out.println("Wrong input! Try again!");
            }
            System.out.println("Dish page. You have following options:");
            System.out.println("Add new dish - enter d01\nRemove dish - enter d02\n" +
                    "Get dish by name - enter d03\nGet all dishes - enter d04");
            System.out.println("To exit database - enter q");
            System.out.println("To start menu - enter 'start'");
        }

    }
    /**
     * Starts private methods for dish page **/
    private void getAllDishes() {
        dishController.printAllDishes();
    }

    private void getDishByName(Scanner sc) {
        showAllDishNames();
        System.out.println("Enter dish name");
        String name = sc.next();
        //Dish dish = dishController.getDishByName(name);
        //System.out.println(dish != null ? dish : "Dish was nog retrieved. Error!");
        dishController.printDishByName(name);
    }

    private void deleteDish(Scanner sc) {
        showAllDishNames();
        System.out.println("Enter dish name to delete");
        String name = sc.next();
        Dish dish = dishController.getDishByName(name);
        if (dish != null) {
            dishController.removeDish(dish);
        } else {
            LOGGER.info("Cant remove this dish.");
            System.out.println("Cant remove this dish");
        }
    }

    private void addNewDish(Scanner sc) {
        Dish dish = new Dish();
        System.out.println("Enter dish name");
        String dishName = sc.next();
        dish.setName(dishName);
        int number = 1;
        for (DishCategory dc : DishCategory.values()) {
            System.out.println(dc + " enter 0" + number++);
        }
        System.out.println("Choose and enter category");
        String category = sc.next();
        DishCategory dishCategory;
        if (category.equals("01")) {
            dishCategory = DishCategory.MAIN_COURSES;
        } else if (category.equals("02")) {
            dishCategory = DishCategory.SALADS;
        } else if (category.equals("03")) {
            dishCategory = DishCategory.SIDE;
        } else if (category.equals("04")) {
            dishCategory = DishCategory.DRINKS;
        } else if (category.equals("05")) {
            dishCategory = DishCategory.WINE;
        } else if (category.equals("06")) {
            dishCategory = DishCategory.SAUCES;
        } else {
            dishCategory = DishCategory.MAIN_COURSES;
            LOGGER.error("Wrong input! " + category +"\nDefault dish category is MAIN_COURSES");
            System.out.println("Wrong input. Default dish category is MAIN_COURSES.");
        }
        dish.setDishCategory(dishCategory);
        try {
            System.out.println("Enter price");
            dish.setPrice(sc.nextDouble());
            System.out.println("Enter weight");
            dish.setWeight(sc.nextInt());
        } catch (InputMismatchException ex) {
            LOGGER.error("Error wile parsing " + ex);
            System.out.println("Wrong input");
        }
        warehouseController.getAllIngredients().forEach(System.out::println);
        System.out.println("Enter ingredients. Then enter - f");
        List<Ingredient> ingredientList = new ArrayList<>();
        while (true) {
            String ingre = sc.next();
            if (ingre.equals("f")) break;
            try {
                Ingredient ingredientByName = ingredientController.getIngredientByName(ingre);
                ingredientList.add(ingredientByName);
                dish.setIngredients(ingredientList);
                System.out.println("Enter name Of menu");
                menuController.printMenuNames();
                dish.setMenu(menuController.getMenuByName(sc.next()));
                dishController.createDish(dish);
            } catch (UnexpectedRollbackException ex) {
                System.out.println("Error! Dish was not saved!");
                LOGGER.error("Cant save dish with such parameters " + Arrays.toString(ex.getStackTrace()));
            }
        }
    }

    private void showAllDishNames() {
        List<Dish> allDishes = dishController.getAllDishes();
        if (allDishes != null)  {
            for (Dish dish : allDishes) {
                System.out.println(dish.getName());
            }
        } else {
            System.out.println("There is no dish.");
        }
    }
    /**
     * Stop private methods for dish page**/

    private void goToEmployeePage(Scanner sc, String selection) {
        System.out.println("Employee page. You have following options:");
        System.out.println("Add new employee - enter e01\nRemove employee - enter e02\n" +
                "Get all employees - enter e03\nFind employee by name - enter e04");
        System.out.println("To exit database - enter q");
        System.out.println("To start menu - enter 'start'");

        while (!"q".equals(selection)) {
            selection = sc.next();
            if (selection.equals("e01")) {
                addNewEmployee(sc);

            } else if (selection.equals("e02")) {
                removeEmployee(sc);

            } else if (selection.equals("e03")) {
                getAllEmployees();

            } else if (selection.equals("e04")) {
                findEmployeeByName(sc);

            } else if (selection.equals("start")) {
                break;

            } else if (selection.equals("q")){
                stopApp = true;
                break;
            } else {
                System.out.println("Wrong input!Try again!");
            }
            System.out.println("Employee page. You have following options:");
            System.out.println("Add new employee - enter e01\nRemove employee - enter e02\n" +
                    "Get all employees - enter e03\nFind employee by name - enter e04");
            System.out.println("To exit database - enter q");
            System.out.println("To start menu - enter 'start'");
        }
    }

    /**
     * Start private methods for employee page**/
    private void findEmployeeByName(Scanner sc) {
        employeeController.printAllEmployeesName();
        System.out.println("Enter second name of employee");
        String secondName = sc.next();
        System.out.println("Enter first name of employee");
        String firstName = sc.next();
        try {
            employeeController.printEmployeeByName(firstName, secondName);
            //Employee employee = employeeController.getEmployeeByName(firstName, secondName);
            //System.out.println(employee != null ? employee : "Cannot get employee by this name");
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Error wile getting employee by this name ");
            LOGGER.error("Error wile getting employee by this name " + ex);
        }

    }

    private void getAllEmployees() {
        employeeController.printAllEmployee();
    }

    private void removeEmployee(Scanner sc) {
        employeeController.printAllEmployeesName();
        System.out.println("Enter second name of employee to delete");
        String secondName = sc.next();
        System.out.println("Enter first name of employee");
        String firstName = sc.next();
        try {
            employeeController.deleteEmployee(firstName, secondName);
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Error wile getting employee by this name ");
            LOGGER.error("Error wile getting employee by this name " + ex);
        }
    }

    private void addNewEmployee(Scanner sc) {
        Employee employee;
        System.out.println("Enter Second name of new employee");
        String secondName = sc.next();
        //employee.setSecondName(secondName);
        System.out.println("Enter First name of new employee");
        String firstName = sc.next();
       // employee.setFirstName(firstName);
        System.out.println("Enter date of employment in format yyyy-mm-dd. For example, 2008-10-3");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date date;
        try {
            date = format.parse(sc.next());
            //employee.setEmplDate(date);
            System.out.println("Enter phone");
            String phone = sc.next();
            //employee.setPhone(phone);
            int i = 1;
            for (Position p : Position.values()) {
                System.out.println(p + " enter 0" + i++);
            }
            System.out.println("Enter position");
            String position = sc.next();
            Position positionOfEmployee;
            if (position.equals("01"))  {
                positionOfEmployee = Position.ADMINISTRATOR;
                employee = new Employee();
            } else if (position.equals("02")) {
                positionOfEmployee = Position.SENIOUR_MANAGER;
                employee = new Employee();
            } else if (position.equals("03")) {
                positionOfEmployee = Position.MANAGER;
                employee = new Employee();
            } else if (position.equals("04")) {
                positionOfEmployee = Position.COOK;
                employee = new Cook();
            } else if (position.equals("05")) {
                positionOfEmployee = Position.WAITRESS;
                employee = new Waiter();
            } else if (position.equals("06")) {
                positionOfEmployee = Position.WAITER;
                employee = new Waiter();
            } else {
                positionOfEmployee = Position.NO_POSITION;
                employee = new Employee();
                LOGGER.error("Wrong input! " + position +"\nDefault position is NO_POSITION");
                System.out.println("Wrong input. Default position is NO_POSITION.");
            }
            employee.setPosition(positionOfEmployee);
            System.out.println("Enter salary");
            employee.setSalary(sc.nextInt());
            if (position.equals("04")) {
                //enter list of readymeals
                //employee.setReadymeals
            } else if (position.equals("05") || position.equals("06")) {
                //enter list of orders
                //employee.setOrderList
            }

            employeeController.createEmployee(employee);
        } catch (ParseException e) {
            LOGGER.error("Exception while parsing date" + e);
        } catch (InputMismatchException ex) {
            LOGGER.error("Wrong format of salary! It should be an integer number.");
        }
    }
    /**
     * Stop private methods for employee page**/

    private void goToMenuPage(Scanner sc, String selection) {
        System.out.println("Menu page. You have following options:");
        System.out.println("Add new menu - enter m01\nRemove menu - enter m02\n" +
                "Get menu by name - enter m03\nTo see all menus - enter m04\n" +
                "To remove dish from menu - enter m05\nTo add dish to menu - enter m06");
        System.out.println("To exit database - enter q");
        System.out.println("To start menu - enter 'start'");

        while (!"q".equals(selection)) {
            selection = sc.next();
            if ("m01".equals(selection)) {
                addNewMenue(sc);

            } else if ("m02".equals(selection)) {
                removeMenu(sc);

            } else if ("m03".equals(selection)) {
                getMenuByName(sc);

            } else if ("m04".equals(selection)) {
                showAllMenues();

            } else if ("m05".equals(selection)) {
                removeDishFromMenu(sc);

            } else if ("m06".equals(selection)) {
                addDishToMenu(sc);

            } else if ("start".equals(selection)) {
                break;
            } else if(selection.equals("q"))  {
                stopApp = true;
                break;
            } else {
                System.out.println("Wrong input!Try again!");
            }
            System.out.println("Menu page. You have following options:");
            System.out.println("Add new menu - enter m01\nRemove menu - enter m02\n" +
                    "Get menu by name - enter m03\nTo see all menus - enter m04\n" +
                    "To remove dish from menu - enter m05\nTo add dish to menu - enter m06");
            System.out.println("To exit database - enter q");
            System.out.println("To start menu - enter 'start'");
        }

    }

    /**
     * Start private methods for menu page**/
    private void addNewMenue(Scanner sc) {
        menuController.printMenuNames();
        System.out.println("Enter name of new Menu");
        String nameMenu = sc.next();
        System.out.println("Enter dish name to add to menu. To finish enter - 'f'");
        List<Dish> dishList = new ArrayList<>();
        String dishName;

        while (true) {
            dishName = sc.next();
            if (dishName.equals("f")) break;
            Dish dish = null;
            try {
                dish = dishController.getDishByName(dishName);
            } catch (UnexpectedRollbackException ex) {
                System.out.println("Cannot find dish by this name! Dish was not added.");
                LOGGER.error("Cannot find dish by name " + Arrays.toString(ex.getStackTrace()));
            }
            if (dish!=null) dishList.add(dish);
        }
        menuController.addNewMenu(nameMenu, dishList);
    }

    private void removeMenu(Scanner sc) {
        menuController.printMenuNames();
        System.out.println("Enter name of menu to delete");
        Menu menu = null;
        try {
            menu = menuController.getMenuByName(sc.next());
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Wrong menu name!");
            LOGGER.error("Cannot find menu by this name " + Arrays.toString(ex.getStackTrace()));
        }
        if (menu != null) {
            menuController.removeMenu(menu);
        } else {
            System.out.println("Cannot remove this menu");
        }
    }

    private void getMenuByName(Scanner sc) {
        menuController.printMenuNames();
        System.out.println("Enter name of menu to see");
        Menu menu = null;
        try {
            menu = menuController.getMenuByName(sc.next());
        } catch (UnexpectedRollbackException ex) {
            LOGGER.error("Cannot find menu by this name " + Arrays.toString(ex.getStackTrace()));
        }
        System.out.println(menu != null ? menu : "Cant find menu by this name.");
    }

    private void showAllMenues() {
        menuController.showAllMenus();
    }

    private void removeDishFromMenu(Scanner sc) {
        menuController.showAllMenus();
        System.out.println("Enter menu name you want to remove dish");
        Menu menu = null;
        try {
            String menuName = sc.next();
            menu = menuController.getMenuByName(menuName);
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Cannot find menu by this name " + menu);
            LOGGER.error("Cannot find menu by this name " + Arrays.toString(ex.getStackTrace()));
        }
        if (menu != null) {
            menu.getDishList().forEach(System.out::println);
            System.out.println("Enter dish name to remove");
            String dishName = sc.next();
            Dish dish = null;
            try {
                dish = dishController.getDishByName(dishName);
            } catch (UnexpectedRollbackException ex) {
                System.out.println("Cannot find dish by this name " + dishName);
                LOGGER.error("Cannot find dish by this name " + Arrays.toString(ex.getStackTrace()));
            }
            if (dish != null) {
                menuController.removeDishFromMenu(menu.getId(), dish);
            } else {
                System.out.println("Cant remove this dish.");
            }
        } else {
            System.out.println("Cant remove dish from chosen menu.");
        }
     }

    private void addDishToMenu(Scanner sc) {
        menuController.showAllMenus();
        System.out.println("Enter menu name you want to add dish");
        Menu menu = null;
        try {
            menu = menuController.getMenuByName(sc.next());
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Cannot find menu with such name.");
            LOGGER.error("Cannot find menu with such name! " + Arrays.toString(ex.getStackTrace()));
        }
        if (menu != null) {
            showAllDishNames();
            System.out.println("Enter dish name to add to menu");
            Dish dish = null;
            try {
                dish = dishController.getDishByName(sc.next());
            } catch (UnexpectedRollbackException ex) {
                System.out.println("Wrong menu name! Cannot find menu by this name.");
                LOGGER.error("Cannot find dish by this name. " + Arrays.toString(ex.getStackTrace()));
            }
            if (dish != null) {
                menuController.addDishToMenu(menu.getId(), dish);
            } else {
                System.out.println("Cannot add this dish to menu.");
            }
        } else {
            LOGGER.info("Dish was not added to chosen menu.");
            System.out.println("Cannot add dish to chosen menu.");
        }
    }
    /**
     * Stop private methods for menu page**/

    private void goToOrdersPage(Scanner sc, String selection) {
        System.out.println("Orders page. You have following options:");
        System.out.println("Create order - enter o01\nAdd dish to open order - enter o02\n" +
                "Delete order - enter o03\nTo change order status - enter o04\n" +
                "To get all open or closed orders - enter o05");
        System.out.println("To exit database - enter q");
        System.out.println("To start menu - enter 'start'");
        while (!"q".equals(selection)) {
            selection = sc.next();
            if ("o01".equals(selection)) {
                createOrder(sc);

            } else if ("o02".equals(selection)) {
                addDishToOpenOrder(sc);

            } else if ("o03".equals(selection)) {
                deleteOrder(sc);

            } else if ("o04".equals(selection)) {
                changeOrderStatus(sc);

            } else if ("o05".equals(selection)) {
                getAllOpenOrClosedOrders(sc);

            } else if ("start".equals(selection)) {
                break;

            } else if ("q".equals(selection)) {
                stopApp = true;
                break;

            } else {
                System.out.println("Wrong input!Try again!");
            }
            System.out.println("Orders page. You have following options:");
            System.out.println("Create order - enter o01\nAdd dish to open order - enter o02\n" +
                    "Delete order - enter o03\nTo change order status - enter o04\n" +
                    "To get all open or closed orders - enter o05");
            System.out.println("To exit database - enter q");
            System.out.println("To start menu - enter 'start'");
        }
    }
    /**
     * Start private methods for Order page**/
    private void addDishToOpenOrder(Scanner sc) {
        System.out.println("Enter dish name");
        showAllDishNames();
        Dish dish = null;
        try {
            dish = dishController.getDishByName(sc.next());
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Cannot find dish with such name.");
            LOGGER.error("Cannot find dish by this name " + Arrays.toString(ex.getStackTrace()));
        }
        if (dish != null) {
            for (Order order : orderController.getOpenOrClosedOrder(OrderStatus.opened)) {
                System.out.println("Order number " + order.getOrderNumber() + " ");
            }
            System.out.println("Select number of order");
            orderController.addDishToOpenOrder(dish, sc.nextInt());
        } else {
            LOGGER.info("Dish was not add to order.");
            System.out.println("Cannot add this dish.");
        }
    }

    private void createOrder(Scanner sc) {
        Order order = new Order();
        order.setOrderNumber(orderNumber++);
        employeeController.printAllEmployeesName();
        System.out.println("Enter employee second name");
        String secondName = sc.next();
        System.out.println("Enter employee firstName");
        String firstName = sc.next();
        Employee employee = null;
        try {
            employee = employeeController.getEmployeeByName(firstName, secondName);
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Cant find employee with this name");
            LOGGER.error("Cannot find employee with such name " + Arrays.toString(ex.getStackTrace()));
        }
        if (employee != null) {
            order.setEmployeeId(employee);
            System.out.println("Enter table number");
            order.setTableNumber(sc.nextInt());
            order.setOrderDate(new java.sql.Date(new Date(System.currentTimeMillis()).getTime()));
            order.setStatus(OrderStatus.opened);
            showAllDishNames();
            System.out.println("Enter name of dish to add to order, to stop - enter twice 'f'");
            List<Dish> dishForOrder = new ArrayList<>();
            String name;

            while (true) {
                name = sc.next();
                if ("f".equals(name)) break;
                System.out.println("You entered " + name);
                Dish dishByName = null;
                try {
                    dishByName = dishController.getDishByName(name);
                } catch (UnexpectedRollbackException ex) {
                    System.out.println("Cannot find dish with this name!");
                    LOGGER.error("Cannot find dish by this name " + Arrays.toString(ex.getStackTrace()));
                }
                if (dishByName != null) {
                    dishForOrder.add(dishByName);
                    LOGGER.info("Dish was add to order.");
                } else {
                    System.out.println("Dish was not add!");
                    LOGGER.info("Dish " + name + " was not add to order!");
                }
            }
            order.setDishList(dishForOrder);
            orderController.saveOrder(order);
        } else {
            System.out.println("Cannot create order with this employee.");
        }
    }

    private void getAllOpenOrClosedOrders(Scanner sc) {
        System.out.println("Select order status to find, print: 'closed' or 'opened'");
        OrderStatus orderStatus;
        String status = sc.next();
        if (status.equals("opened")) {
            orderStatus = OrderStatus.opened;
        } else {
            orderStatus = OrderStatus.closed;
        }
        orderController.printOpenOrClosedOrders(orderStatus);
    }

    private void changeOrderStatus(Scanner sc) {
        System.out.println("Enter number of order to change status to 'closed'");
        showOpenedOrdersNumb();
        try {
            orderController.changeOrderStatus(sc.nextInt());
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Order status was not changed! Error!");
            LOGGER.error("Cannot find order by this number of order " + Arrays.toString(ex.getStackTrace()));
        }
    }

    private void showOpenedOrdersNumb() {
        List<Order> openOrClosedOrder = orderController.getOpenOrClosedOrder(OrderStatus.opened);
         if (openOrClosedOrder != null) {
             for (Order order : openOrClosedOrder) {
                 System.out.println("Order #" + order.getOrderNumber());
             }
         } else {
             System.out.println("No opened order is found.");
         }
    }

    private void deleteOrder(Scanner sc) {
        System.out.println("Enter number of order to delete");
        showOpenedOrdersNumb();
        int orderNumber = sc.nextInt();
        try {
            orderController.deleteOrder(orderNumber);
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Order was not removed. Wrong order number!");
            LOGGER.error("Cannot remove order by this order number " + orderNumber + "\n" + Arrays.toString(ex.getStackTrace()));
        }
    }
    /**
     * End of private methods for Order page**/

    private void goToReadyMealPage(Scanner sc, String selection) {
        System.out.println("Ready meals page. You have following options:");
        System.out.println("Get all ready meals - enter rm01\nAdd new ready meal - enter rm02");
        System.out.println("To exit database - enter q");
        System.out.println("To start menu - enter 'start'");
        while (!"q".equals(selection)) {
            selection = sc.next();
            if ("rm01".equals(selection)) {
                getAllreadyMeals();

            } else if ("rm02".equals(selection)) {
                addNewReadyMeal(sc);

            } else if ("start".equals(selection)) {
                start();
            } else if ("q".equals(selection)) {
                stopApp = true;
                break;
            } else {
                System.out.println("Wrong input! try again!");
            }
            System.out.println("Ready meals page. You have following options:");
            System.out.println("Get all ready meals - enter rm01\nAdd new ready meal - enter rm02");
            System.out.println("To exit database - enter q");
            System.out.println("To start menu - enter 'start'");
        }
    }
    /**
     * Start private methods for ready meal page**/
    private void addNewReadyMeal(Scanner sc) {
        ReadyMeal readyMeal = new ReadyMeal();
        showAllDishNames();
        System.out.println("Enter dish name");
        Dish dish = null;
        try {
          dish = dishController.getDishByName(sc.next());
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Cant find dish with this name!");
            LOGGER.error("Cant find dish with this name " + Arrays.toString(ex.getStackTrace()));
        }

        if (dish != null) {
            if (warehouseController.validateAmount(dish.getIngredients())) {
                readyMeal.setDishId(dish);

                readyMeal.setDishNumber(dish.getId());
                employeeController.printAllEmployeesName();
                System.out.println("Enter employee second name");
                String secondName = sc.next();
                System.out.println("Enter employee first name");
                String firstName = sc.next();
                Employee employee = employeeController.getEmployeeByName(firstName, secondName);
                if (employee != null) {
                    readyMeal.setEmployeeId(employee);
                    List<Order> openOrders = orderController.getOpenOrClosedOrder(OrderStatus.opened);
                    if (openOrders != null && openOrders.size() > 0) {

                        for (Order openOrder : openOrders) {
                            System.out.println("order_id " + openOrder.getId() + " ");
                        }
                        System.out.println("Select order id");
                        Order order = orderController.getOrderById(sc.nextInt());
                        if (order != null) readyMeal.setOrderId(order);
                        readyMeal.setMealDate(new java.sql.Date(new Date(System.currentTimeMillis()).getTime()));
                        readyMealController.addReadyMeal(readyMeal);
                    } else {
                        LOGGER.info("No open orders. Cant save ready meal");
                    }
                }
            } else {
                System.out.println("Not enough ingredients on warehouse! Cannot create ready meal with these ingredients.");
            }
        } else {
            System.out.println("Cannot add dish with this name.");
        }
    }

    private void getAllreadyMeals() {
        List<ReadyMeal> readyMeals = readyMealController.getAllReadyMeals();
        if (readyMeals != null) {
            readyMeals.forEach(System.out::println);
        } else {
            System.out.println("There is no ready meal.");
        }
    }
    /**Stop private methods for ready meal page**/

    private void goToWarehousePage(Scanner sc, String selection) {
        System.out.println("Warehouse page. You have following options:");
        System.out.println("Add ingredient to warehouse - enter w01\nRemove ingredient - enter w02\n" +
                "Change amount of ingredient - enter w03\nFind ingredient by name - enter w04\n " +
                "To see all ingredients - enter w05");
        System.out.println("To exit database - enter q");
        System.out.println("To start menu - enter 'start'");
        while (!"q".equals(selection)) {
            selection = sc.next();
            if (selection.equals("w01")) {
                addNewIngredient(sc);

            } else if (selection.equals("w02")) {
                removeIngredientByName(sc);

            } else if (selection.equals("w03")) {
                changeAmountOfIngredient(sc);

            } else if (selection.equals("w04")) {
                findIngredientByName(sc);

            } else if (selection.equals("w05")) {
                getAllIngredients();

            } else if (selection.equals("start")) {
                break;

            } else if ("q".equals(selection)) {
                stopApp = true;
                break;
            }
            System.out.println("Warehouse page. You have following options:");
            System.out.println("Add ingredient to warehouse - enter w01\nRemove ingredient - enter w02\n" +
                    "Change amount of ingredient - enter w03\nFind ingredient by name - enter w04\n " +
                    "To see all ingredients - enter w05");
            System.out.println("To exit database - enter q");
            System.out.println("To start menu - enter 'start'");

        }
    }

    /**
     * Start private methods for warehouse page**/
    private void getAllIngredients() {
        List<Warehouse> warehouseList = warehouseController.getAllIngredients();
        if (warehouseList != null) {
            warehouseList.forEach(System.out::println);
        } else {
            System.out.println("No ingredient on warehouse is found.");
        }
    }

    private void findIngredientByName(Scanner sc) {
        List<Warehouse> ingredients = warehouseController.getAllIngredients();
        if (ingredients != null) {
            for (Warehouse ingredient : ingredients) {
                System.out.println(ingredient.getIngredId().getName());
            }

            System.out.println("Enter name of ingredient");
            try {
                Warehouse warehouse = warehouseController.findByName(sc.next());
                System.out.println(warehouse != null ? warehouse : "There is no ingredient on warehouse with such name.");
            } catch (UnexpectedRollbackException ex) {
                System.out.println("Cannot find ingredient on warehouse by this name");
                LOGGER.error("Cannot find ingredient on warehouse by this name " + Arrays.toString(ex.getStackTrace()));
            }

        } else {
            System.out.println("There is no ingredient on warehouse.");
        }
    }

    private void showAllIngredNames() {
        List<Warehouse> allIngredients = warehouseController.getAllIngredients();
        if (allIngredients != null) {
            allIngredients.forEach(System.out::println);
        } else {
            System.out.println("List of ingredients on warehouse is empty.");
        }
    }

    private void changeAmountOfIngredient(Scanner sc) {
        showAllIngredNames();
        System.out.println("Enter name of ingredient to change it amount");
        try {
            Ingredient ingredient = ingredientController.getIngredientByName(sc.next());
            if (ingredient != null) {
                System.out.println("Enter amount");
                int amount = sc.nextInt();
                System.out.println("If you want to increase amount enter y. If to decrease - n");
                boolean increase = sc.next().equals("y");
                warehouseController.changeAmount(ingredient, amount, increase);
            } else {
                System.out.println("There is no ingredient with such name ");
            }
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Amount was not changed. Could not get ingredient by this name.");
            LOGGER.error("Cant get ingredient by this name " + Arrays.toString(ex.getStackTrace()));
        }
    }

    private void removeIngredientByName(Scanner sc) {
        showAllIngredNames();
        System.out.println("Enter name of ingredient to remove");
        try {
            warehouseController.removeIngredient(sc.next());
        } catch (UnexpectedRollbackException ex) {
            System.out.println("Ingredient by this name was not removed!");
            LOGGER.error("Cant find ingredient by this name " + Arrays.toString(ex.getStackTrace()));
        }
    }

    private void addNewIngredient(Scanner sc) {
        List<Ingredient> allIngredients = ingredientController.getAllIngredients();
        if (allIngredients != null) {
            allIngredients.forEach(System.out::println);
            System.out.println("Enter name of ingredient");
            String ingredientName = sc.next();
            System.out.println("This ingredient is new in Ingredient department: 'y'/'n'");
            boolean newIngred = sc.next().equals("y");
            Ingredient ingredient = null;
            if (!newIngred) {
                try {
                    ingredient = ingredientController.getIngredientByName(ingredientName);
                } catch (UnexpectedRollbackException ex) {
                    System.out.println("Ingredient with this name was not saved");
                    LOGGER.error("Cant find ingredient to warehouse with this name " + Arrays.toString(ex.getStackTrace()));
                }

            } else {
                ingredient = new Ingredient();
                ingredient.setName(ingredientName);
                ingredientController.createIngredient(ingredient);
            }
            System.out.println("Enter amount of ingredient");
            int amount = sc.nextInt();
            if (ingredient != null) {
                warehouseController.addIngredient(ingredient, amount);
            } else {
                System.out.println("Cannot add ingredient to warehouse with this name.");
            }
        } else {
            System.out.println("No ingredient is available");
        }
    }
    /**
     * Stop private methods for warehouse page**/


    private void startApplication() {
        System.out.println("Hi! You entered to restaurant database");
        System.out.println("Select you next step");
        System.out.println("d - work with dish information\ne - work with info about employee");
        System.out.println("m - work with menu information\no - work with info about orders");
        System.out.println("rm - work with ready meals information\nw - work with info about ingredients in warehouse");
        System.out.println("q - to leave application");
    }

    public void setEmployeeController(EmployeeController employeeController) {
        this.employeeController = employeeController;
    }

    public void setIngredientController(IngredientController ingredientController) {
        this.ingredientController = ingredientController;
    }

    public void setDishController(DishController dishController) {
        this.dishController = dishController;
    }

    public void setOrderController(OrderController orderController) {
        this.orderController = orderController;
    }

    public void setWarehouseController(WarehouseController warehouseController) {
        this.warehouseController = warehouseController;
    }

    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    public void setReadyMealController(ReadyMealController readyMealController) {
        this.readyMealController = readyMealController;
    }
}
