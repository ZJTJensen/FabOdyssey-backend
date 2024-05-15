package com.fabAdventure.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fabAdventure.models.Cards;
import com.fabAdventure.models.Decks;
import com.fabAdventure.models.SelectCards;
import com.fabAdventure.models.Users;
import com.fabAdventure.models.UsersRequest;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Service
public class UserService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String INSTANCE_HOST = System.getenv("INSTANCE_HOST");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASS = System.getenv("DB_PASS");
    private HikariDataSource dataSource;


    
  public UserService() {
    try{
        System.err.println("INSTANCE_HOST: " + INSTANCE_HOST);
        System.err.println(DB_USER);
        System.err.println(DB_PASS);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + INSTANCE_HOST + ":5432/"+ System.getenv("DB_NAME"));
        System.err.println("URL: " + "jdbc:postgresql://" + INSTANCE_HOST + ":5432/"+ System.getenv("DB_NAME"));
        config.setUsername(DB_USER); 
        config.setPassword(DB_PASS);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(10000); // 10 seconds
        config.setIdleTimeout(600000); // 10 minutes
        config.setMaxLifetime(1800000);
        LOGGER.info("config: " + config);   
        this.dataSource = new HikariDataSource(config);
    } catch (Exception e) {
        LOGGER.error("Error creating HikariDataSource", e);
        throw e;
    }
}
  

    public Users doesUserExist(String slug) throws SQLException, ClassNotFoundException {
        Users user = new Users();
        try (java.sql.Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
            "select * from users where slug = ?")){
            preparedStatement.setString(1, slug);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    user.setSlug(slug);
                    user.setPhone(resultSet.getString("phoneNumber"));
                    user.setUserLevel(resultSet.getInt("userLevel"));
                    user.setUserName(resultSet.getString("userName"));
                }
            }
        }
        return user;
    }
    public void creteUser(String phone, Decks deck, String userName){
        try (java.sql.Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO users(slug, phoneNumber, username, userlevel) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, deck.getSlug());
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, userName);
            preparedStatement.setInt(4, 0);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateUserLevel(String slug, Integer newLevel) {
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "UPDATE users SET userlevel = ? WHERE slug = ?")) {
            preparedStatement.setInt(1, newLevel);
            preparedStatement.setString(2, slug);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Cards> getCards(String slug) {
        ArrayList<Cards> cards = new ArrayList<>();
        String sql = "select * from cards where slug = ?";

        try (java.sql.Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, slug);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Cards card = new Cards();
                card.setIdentifier(resultSet.getString("cardidentifier"));
                card.setSlug(resultSet.getString("slug"));
                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cards;
    }

    public void resetUserLevel(String slug) {
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "UPDATE users SET userlevel = 0 WHERE slug = ?")) {
            preparedStatement.setString(1, slug);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePhone(String slug, String newPhone) {
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "UPDATE users SET phone = ? WHERE slug = ?")) {
            preparedStatement.setString(1, newPhone);
            preparedStatement.setString(2, slug);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void changeUserName(String slug, String newUserName) {
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "UPDATE users SET username = ? WHERE slug = ?")) {
            preparedStatement.setString(1, newUserName);
            preparedStatement.setString(2, slug);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String slug) {
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "DELETE FROM users WHERE slug = ?")) {
            preparedStatement.setString(1, slug);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserCards(String slug) {
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "DELETE FROM cards WHERE slug = ?")) {
            preparedStatement.setString(1, slug);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCardToUserDeck(UsersRequest message){
          try (java.sql.Connection connection = dataSource.getConnection()) {
        // Delete existing cards for the user
        try (PreparedStatement preparedStatement = connection.prepareStatement(
            "DELETE FROM cards WHERE \"slug\" = ? AND \"toBeSelected\" = true")) {
            preparedStatement.setString(1, message.getSlug());
            preparedStatement.executeUpdate();
        }

        // Insert new card
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO cards(\"slug\", \"cardidentifier\") VALUES (?, ?)")) {
                    System.err.println("slug: " + message.getSlug());
                    System.err.println("card: " + message.getCard().getCardIdentifier());
                preparedStatement.setString(1, message.getSlug());
                preparedStatement.setString(2, message.getCard().getCardIdentifier());
                preparedStatement.executeUpdate();
        }
        // Update selectcard in users table
        try (PreparedStatement preparedStatement = connection.prepareStatement(
            "UPDATE users SET \"selectcard\" = false WHERE \"slug\" = ?")) {
            preparedStatement.setString(1, message.getSlug());
            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
    public ArrayList<Cards> getCardsChosen(UsersRequest message){
        ArrayList<Cards> chosenCards = new ArrayList<>();
        String sql = "SELECT * FROM cards WHERE \"slug\" = ? AND \"toBeSelected\" = true";

        try (java.sql.Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, message.getSlug());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Cards card = new Cards();
                card.setCardIdentifier(resultSet.getString("cardidentifier"));
                // Set other Card properties from resultSet if needed
                chosenCards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chosenCards;
    }

    public void cardsChosen(UsersRequest message){
         try (java.sql.Connection connection = dataSource.getConnection()) {
        // Prepare the statement for inserting into cards table
        try (PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO cards(\"slug\", \"cardidentifier\", \"tobeselected\") VALUES (?, ?, ?)")) {

            // Loop over each card in the cardsToShow list
            ArrayList<Cards> selectedCards = message.getSelectedCards().getCards();
            for (Cards card : selectedCards) {
                System.err.println("slug: " + message.getSlug());
                System.err.println("card: " + card.getCardIdentifier());
                preparedStatement.setString(1, message.getSlug());
                preparedStatement.setString(2, card.getCardIdentifier());
                preparedStatement.setBoolean(3, true);
                preparedStatement.executeUpdate();
            }
        }

        // Insert into users table
        try (PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO users(\"selectcard\") VALUES (?)")) {
            preparedStatement.setBoolean(1, true);
            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
    
}
