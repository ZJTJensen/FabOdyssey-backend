package com.fabAdventure.models;

import java.util.ArrayList;

import jakarta.persistence.Column;

public class SelectCards {
    @Column 
    private ArrayList<Cards> cards;

    public ArrayList<Cards> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Cards> cards) {
        this.cards = cards;
    }
}
