package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class Person {
    String name;
    String town;
    int age;

    public Person(String name, String town, int age) {
        this.name = name;
        this.town = town;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Name: " + name + ". Town: " + (town.isEmpty() ? "unknown" : town) + ". Age: " + (age == 0 ? "unknown" : age);
    }
}

class InvalidLineFormatException extends Exception {
    public InvalidLineFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class Main {
    public static List<Person> readPeopleFromFile(String filePath) throws IOException, InvalidLineFormatException {
        List<Person> people = new ArrayList<>();
        List<String> lines = Files.readAllLines(Path.of(filePath));

        for (String line : lines) {
            String[] fields = line.split(":");
            if (fields.length >= 1) {
                String name = fields[0].trim();
                String town = fields.length >= 2 ? fields[1].trim() : "";
                int age = fields.length >= 3 ? Integer.parseInt(fields[2].trim()) : 0;

                if (name.isEmpty()) {
                    throw new InvalidLineFormatException("El nombre es necesario.", null);
                }

                people.add(new Person(name, town, age));
            } else {
                throw new InvalidLineFormatException("El formato no es válido: " + line, null);
            }
        }

        return people;
    }

    public static void main(String[] args) {
        try {
            List<Person> people = readPeopleFromFile("./people.csv");

            // Filtrar personas menores de 25 años y mostrar en la consola
            List<Person> under25 = people.stream()
                    .filter(person -> person.age < 25)
                    .collect(Collectors.toList());

            System.out.println("Personas menores de 25:");
            under25.forEach(System.out::println);
            //Uso de Streams
            // Obtener el primer elemento cuya ciudad sea Madrid
            Optional<Person> firstPersonFromMadrid = people.stream()
                    .filter(person -> person.town.equalsIgnoreCase("Madrid"))
                    .findFirst();

            firstPersonFromMadrid.ifPresentOrElse(
                    person -> System.out.println("Primer elemento de Madrid: " + person),
                    () -> System.out.println("No hay persona de Madrid.")
            );

            // Obtener el primer elemento cuya ciudad sea Barcelona
            Optional<Person> firstPersonFromBarcelona = people.stream()
                    .filter(person -> person.town.equalsIgnoreCase("Barcelona"))
                    .findFirst();

            firstPersonFromBarcelona.ifPresentOrElse(
                    person -> System.out.println("Primer elemento de Barcelona: " + person),
                    () -> System.out.println("No hay persona de Barcelona.")
            );

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidLineFormatException e) {
            System.out.println("Formato de línea no válido: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        }
    }
}