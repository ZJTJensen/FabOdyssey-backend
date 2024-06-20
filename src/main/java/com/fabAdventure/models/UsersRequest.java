package com.fabAdventure.models;

import java.util.ArrayList;
import jakarta.persistence.Column;


public class UsersRequest {

    @Column private String slug;
	@Column private Cards card;
	@Column private String phoneNumber; 
	@Column private Decks deck;
    @Column private ArrayList<Cards> selectCards;
    @Column private String user; 
    @Column private String originlocation; 
    @Column private String location; 
    @Column private Integer userLevel; 

    public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}

    public ArrayList<Cards> getSelectCards() {
		return selectCards;
	}
	public void setSelectCards(ArrayList<Cards> cards) {
		this.selectCards = cards;
	}
	public Cards getCard() {
        return this.card;
    }

    public void setCard(Cards card) {
        this.card = card;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Decks getDeck() {
        return this.deck;
    }

    public void setDeck(Decks deck) {
        this.deck = deck;
    }
    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public String getOriginlocation() {
        return this.originlocation;
    }

    public void setOriginlocation(String originlocation) {
        this.originlocation = originlocation;
    }
    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public Integer getUserLevel() {
        return this.userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

}
