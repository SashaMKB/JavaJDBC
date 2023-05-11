package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;



public class ConsoleLibrary {
    static Connection connection;
    static BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    //static Statement statement;

    public static void main(String[] args) {
        try {
            connection = DatabaseConnector.connectToDataBase();
            //statement = connection.createStatement();
            while (true) {
                int action = getActionInMainMenu();
                if (action == MenuActions.EXIT.getAction())
                    break;
                doActionInDataBase(action);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Ошибка подключения к базе данных!");
        }
    }

    private static int getActionInMainMenu() {
        try {
            System.out.print(
                    "Выберите действие для Домашней библиотеки\n" +
                            "1. Добавить новое место для книг.\n" +
                            "2. Добавить новую книгу.\n" +
                            "3. Выйти.\n" +
                            "4. Посмотреть все места для книг.\n" +
                            "5. Посмотреть все книги.\n" +
                            "6. Изменить место по id.\n" +
                            "7. Изменить книгу по id\n" +
                            "8. Удалить место по id\n" +
                            "9. Удалить книгу по id\n" +
                            "10 Показать 2 поле во всех книгах\n" +
                            "11 Показать 2 поле во всех книгах в лексикографическом порядке\n" +
                            "12 Вернуть данные по умолчанию из файла\n" +
                            "13 Вывести авторов в указанном шкафу в лексикографическом порядке\n" +
                            "14 вывести суммарный вес изданий в указанном шкафу\n" +
                            ">> ");
            return Integer.parseInt(consoleReader.readLine());
        } catch (Exception e) {
            System.out.println("Ошибка ввода, попробуйте еще раз!");
            return getActionInMainMenu();
        }
    }

    private static void doActionInDataBase(int action) {
        try {
            if (action == MenuActions.ADD_PLACE.getAction()) {
                addNewPlaceInTableDB();
            } else if (action == MenuActions.ADD_BOOK.getAction()) {
                addNewBookInTableDB();
            } else if (action == MenuActions.SHOW_ALL_PLACES.getAction()) {
                showAllPlaces();
            } else if (action == MenuActions.SHOW_ALL_BOOKS.getAction()) {
                showAllBooks();
            } else if (action == MenuActions.UPDATE_PLACE.getAction()) {
                updatePlace();
            } else if (action == MenuActions.UPDATE_BOOK.getAction()) {
                updateBook();
            } else if (action == MenuActions.DELETE_PLACE.getAction()) {
                deletePlace();
            } else if (action == MenuActions.DELETE_BOOK.getAction()) {
                deleteBook();
            } else if (action == MenuActions.SHOW_1_FIELD_IN_ALL_BOOKS.getAction()) {
                showFirstFieldInAllBooks();
            } else if (action == MenuActions.SHOW_2_FIELD_IN_ALL_BOOKS_IN_LEXIC_ORDER.getAction()) {
                showSecondFieldInAllBooksByLexicOrder();
            } else if (action == MenuActions.DEFAULT_DATA_FROM_FILE.getAction()) {
                replaceWithDefaultDataFromFile();
            } else if (action == MenuActions.SHOW_AUTHORS_IN_WARDROBE.getAction()) {
                showAuthorsInWardrobe();
            } else if (action == MenuActions.SHOW_SUMM_WEIGHT_IN_WARDROBE.getAction()) {
                showSummWeightInWardrobe();
            } else {
                throw new IOException();
            }
        } catch (Exception e) {
            System.out.println("Ошибка ввода!");
        }
    }

    private static void showSummWeightInWardrobe() throws IOException {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.print("Введите этаж\n>> ");
        int floor = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите шкаф\n>> ");
        int wardrobe = Integer.parseInt(consoleReader.readLine());
        int sum = sqlRequests.showSummWeightInWardrobe(floor, wardrobe);
        if (sum != -1) {
            System.out.println("Суммарный вес книг: " + sum);
        } else {
            System.out.println("Произошла ошибка!");
        }
    }

    private static void showAuthorsInWardrobe() throws IOException {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.print("Введите этаж\n>> ");
        int floor = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите шкаф\n>> ");
        int wardrobe = Integer.parseInt(consoleReader.readLine());
        System.out.println(sqlRequests.showAuthorsInWardrobe(floor, wardrobe));
    }

    private static void replaceWithDefaultDataFromFile() {
        SQLRequests sqlRequests = new SQLRequests(connection);
        FileDataHandler fileHandler = new FileDataHandler("src/main/java/org/example/DataFile.txt");
        System.out.println(fileHandler.readData());
        if (!fileHandler.isError()) {
            sqlRequests.deleteAllBook();
            sqlRequests.deleteAllPlace();
            for (Place place : fileHandler.getPlaceList()) {
                sqlRequests.createNewRowInPlaceTable(place.getFloor(), place.getWardrobe(), place.getShelf());
            }
            for (Book book : fileHandler.getBookList()) {
                sqlRequests.createNewRowInBookTableWOPlaceId(
                        book.getAuthor(), book.getPublication(),
                        book.getPublishing_house(), book.getYear_public(),
                        book.getPages(), book.getYear_write(),
                        book.getWeight());
            }
        }
    }

    private static void showFirstFieldInAllBooks() {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.println(sqlRequests.showFirstFieldInAllBooks());
    }

    private static void showSecondFieldInAllBooksByLexicOrder() {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.println(sqlRequests.showSecondFieldInAllBooksByLexicOrder());
    }

    private static void deleteBook() throws IOException {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.print("Введите id книги\n>> ");
        int id = Integer.parseInt(consoleReader.readLine());
        System.out.println(sqlRequests.deleteBook(id));
    }

    private static void deletePlace() throws IOException {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.print("Введите id места\n>> ");
        int id = Integer.parseInt(consoleReader.readLine());
        System.out.println(sqlRequests.deletePlace(id));
    }

    private static void updateBook() throws IOException {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.print("Введите id книги\n>> ");
        int id = Integer.parseInt(consoleReader.readLine());
        if (!sqlRequests.checkExistsBookById(id)) {
            System.out.println("Книга с таким id не существует!");
            return;
        }
        System.out.print("Введите id места, где будет находиться книга\n>> ");
        int placeId = Integer.parseInt(consoleReader.readLine());
        if (!sqlRequests.checkExistsPlaceById(placeId)) {
            System.out.println("Места с таким id не существует!");
            return;
        }
        System.out.print("Введите нового автора книги\n>> ");
        String author = consoleReader.readLine();
        System.out.print("Введите новое издание\n>> ");
        String publication = consoleReader.readLine();
        System.out.print("Введите новое издательство\n>> ");
        String publishingHouse = consoleReader.readLine();
        System.out.print("Введите новый год публикации\n>> ");
        int yearPublic = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите новое кол-во страниц\n>> ");
        int pages = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите новый год написания\n>> ");
        int yearWrite = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите новый вес в граммах\n>> ");
        int weight = Integer.parseInt(consoleReader.readLine());
        System.out.println(sqlRequests.updateBook(id,
                author, publication, publishingHouse, yearPublic,
                pages, yearWrite, weight, placeId));

    }

    private static void updatePlace() throws IOException {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.print("Введите id места\n>> ");
        int id = Integer.parseInt(consoleReader.readLine());
        if (!sqlRequests.checkExistsPlaceById(id)) {
            System.out.println("Места с таким id не существует!");
            return;
        }
        System.out.print("Введите новый номер этажа\n>> ");
        int floor = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите новый номер шкафа\n>> ");
        int wardrobe = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите новый номер полки\n>> ");
        int shelf = Integer.parseInt(consoleReader.readLine());
        System.out.println(sqlRequests.updatePlace(id, floor, wardrobe, shelf));

    }

    private static void showAllBooks() {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.println(sqlRequests.showAllBooks());
    }

    private static void addNewBookInTableDB() throws IOException {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.print("Введите автора книги\n>> ");
        String author = consoleReader.readLine();
        System.out.print("Введите издание\n>> ");
        String publication = consoleReader.readLine();
        System.out.print("Введите издательство\n>> ");
        String publishingHouse = consoleReader.readLine();
        System.out.print("Введите год публикации\n>> ");
        int yearPublic = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите кол-во страниц\n>> ");
        int pages = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите год написания\n>> ");
        int yearWrite = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите вес в граммах\n>> ");
        int weight = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите id места, где будет находиться книга\n>> ");
        int placeId = Integer.parseInt(consoleReader.readLine());
        System.out.println(sqlRequests.createNewRowInBookTable(
                author, publication, publishingHouse, yearPublic,
                pages, yearWrite, weight, placeId));
    }

    private static void showAllPlaces() {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.println(sqlRequests.showAllPlaces());
    }

    private static void addNewPlaceInTableDB() throws IOException {
        SQLRequests sqlRequests = new SQLRequests(connection);
        System.out.print("Введите номер этажа\n>> ");
        int floor = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите номер шкафа\n>> ");
        int wardrobe = Integer.parseInt(consoleReader.readLine());
        System.out.print("Введите номер полки\n>> ");
        int shelf = Integer.parseInt(consoleReader.readLine());
        System.out.println(sqlRequests.createNewRowInPlaceTable(floor, wardrobe, shelf));
    }


}
