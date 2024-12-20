/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.moviebookingsystem;

/**
 *
 * @author SoftLaptop
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Factory Pattern for User Roles
abstract class User {
    public abstract void displayRole();
}

class Admin extends User {
    @Override
    public void displayRole() {
        System.out.println("Admin access granted.");
    }
}

class Customer extends User {
    @Override
    public void displayRole() {
        System.out.println("Customer access granted.");
    }
}

class UserFactory {
    public static User createUser(String role) {
        if (role.equalsIgnoreCase("admin")) {
            return new Admin();
        } else if (role.equalsIgnoreCase("customer")) {
            return new Customer();
        }
        throw new IllegalArgumentException("Invalid role");
    }
}

// Builder Pattern for Movie
class Movie {
    private final String title;
    private final String genre;
    private final int duration;

    private Movie(MovieBuilder builder) {
        this.title = builder.title;
        this.genre = builder.genre;
        this.duration = builder.duration;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public static class MovieBuilder {
        private String title;
        private String genre;
        private int duration;

        public MovieBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public MovieBuilder setGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public MovieBuilder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Movie build() {
            return new Movie(this);
        }
    }
}

// Proxy Pattern for Booking Service
interface BookingService {
    void book(String movie, int seats);
}

class RealBookingService implements BookingService {
    @Override
    public void book(String movie, int seats) {
        System.out.println("Successfully booked " + seats + " seats for " + movie);
        JOptionPane.showMessageDialog(null, "Successfully booked " + seats + " seats for " + movie);
    }
}

class BookingProxy implements BookingService {
    private final BookingService realService = new RealBookingService();
    private final boolean isAdmin;

    public BookingProxy(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public void book(String movie, int seats) {
        if (!isAdmin) {
            System.out.println("Access Denied: Only admins can book tickets.");
            JOptionPane.showMessageDialog(null, "Access Denied: Only admins can book tickets.");
        } else {
            realService.book(movie, seats);
        }
    }
}

// Main Class with GUI Integration
public class MovieBookingSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieBookingSystem::showGUI);
    }

    private static void showGUI() {
        JFrame frame = new JFrame("Movie Ticket Booking System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JLabel roleLabel = new JLabel("Select Role:");
        String[] roles = {"Admin", "Customer"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        JButton proceedButton = new JButton("Proceed");

        frame.add(roleLabel);
        frame.add(roleComboBox);
        frame.add(proceedButton);

        proceedButton.addActionListener(e -> {
            String selectedRole = (String) roleComboBox.getSelectedItem();
            if (selectedRole != null) {
                if (selectedRole.equals("Admin")) {
                    openAdminPanel();
                } else {
                    openCustomerPanel();
                }
            }
        });

        frame.setVisible(true);
    }


private static void openAdminPanel() {
        JFrame adminFrame = new JFrame("Admin Panel");
        adminFrame.setSize(400, 400);
        adminFrame.setLayout(new FlowLayout());

        JLabel movieTitleLabel = new JLabel("Movie Title:");
        JTextField movieTitleField = new JTextField(20);
        JLabel movieGenreLabel = new JLabel("Genre:");
        JTextField movieGenreField = new JTextField(20);
        JLabel movieDurationLabel = new JLabel("Duration (mins):");
        JTextField movieDurationField = new JTextField(20);
        JButton addMovieButton = new JButton("Add Movie");

        adminFrame.add(movieTitleLabel);
        adminFrame.add(movieTitleField);
        adminFrame.add(movieGenreLabel);
        adminFrame.add(movieGenreField);
        adminFrame.add(movieDurationLabel);
        adminFrame.add(movieDurationField);
        adminFrame.add(addMovieButton);

        addMovieButton.addActionListener(e -> {
            String title = movieTitleField.getText();
            String genre = movieGenreField.getText();
            int duration;

            try {
                duration = Integer.parseInt(movieDurationField.getText());
                // Builder Pattern to create a movie
                Movie movie = new Movie.MovieBuilder()
                        .setTitle(title)
                        .setGenre(genre)
                        .setDuration(duration)
                        .build();
                JOptionPane.showMessageDialog(adminFrame, "Movie Added: " + movie.getTitle());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(adminFrame, "Invalid duration. Please enter a number.");
            }
        });

        adminFrame.setVisible(true);
    }

    private static void openCustomerPanel() {
        JFrame customerFrame = new JFrame("Customer Panel");
        customerFrame.setSize(400, 300);
        customerFrame.setLayout(new FlowLayout());

        JLabel movieLabel = new JLabel("Available Movies:");
        JComboBox<String> movieComboBox = new JComboBox<>(new String[]{"Inception", "Titanic", "Interstellar"});
        JLabel seatLabel = new JLabel("Number of Seats:");
        JTextField seatField = new JTextField(10);
        JButton bookButton = new JButton("Book Ticket");

        customerFrame.add(movieLabel);
        customerFrame.add(movieComboBox);
        customerFrame.add(seatLabel);
        customerFrame.add(seatField);
        customerFrame.add(bookButton);

        bookButton.addActionListener(e -> {
            String movie = (String) movieComboBox.getSelectedItem();
            int seats;
            try {
                seats = Integer.parseInt(seatField.getText());
                // Proxy Pattern for booking
                BookingService bookingService = new BookingProxy(false); // Customer access
                bookingService.book(movie, seats);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(customerFrame, "Invalid number of seats. Please enter a number.");
            }
        });

        customerFrame.setVisible(true);
    }
}









