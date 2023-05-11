package org.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileDataHandler {

    private String pathToFile;
    private List<Book> bookList = new ArrayList<>();
    private List<Place> placeList = new ArrayList<>();

    public boolean isError() {
        return error;
    }

    private boolean error = false;

    public FileDataHandler(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public String readData() {
        try (Scanner scanner = new Scanner(new File(pathToFile))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty())
                    continue;
                String[] arrayElements = line.split("\\|");
                if (arrayElements.length == 3) {
                    addPlaceToListFromFileArrayElements(arrayElements);
                }
                else if (arrayElements.length == 7) {
                    addBookToListFromFileArrayElements(arrayElements);
                }
            }
            return "Данные из файлы получены!";
        }
        catch (NumberFormatException e) {
            error = true;
            return "Проблема с аргументами!";
        }
        catch (IOException e) {
            error = true;
            return "Проблема с файлом!";
        }
    }

    private void addBookToListFromFileArrayElements(String[] arrayElements) {
        Book book = new Book(arrayElements[0], arrayElements[1], arrayElements[2],
                Integer.parseInt(arrayElements[3]),
                Integer.parseInt(arrayElements[4]),
                Integer.parseInt(arrayElements[5]),
                Integer.parseInt(arrayElements[6]));
        bookList.add(book);
    }

    private void addPlaceToListFromFileArrayElements(String[] arrayElements) throws NumberFormatException {
        Place place = new Place(
                Integer.parseInt(arrayElements[0]),
                Integer.parseInt(arrayElements[1]),
                Integer.parseInt(arrayElements[2]));
        placeList.add(place);
    }


    public List<Book> getBookList() {
        return bookList;
    }

    public List<Place> getPlaceList() {
        return placeList;
    }
}

