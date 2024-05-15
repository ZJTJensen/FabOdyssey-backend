package com.fabAdventure.models;

import java.util.ArrayList;

import jakarta.persistence.Column;

public class SelectCards {
    @Column 
    private ArrayList<Cards> selectCards;

    public ArrayList<Cards> getCards() {
        return selectCards;
    }

    public void setCards(ArrayList<Cards> cards) {
        this.selectCards = cards;
    }
}
