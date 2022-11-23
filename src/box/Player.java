package box;

import java.util.ArrayList;

public final class Player {

    private Decks playerDecks;
    private final ArrayList<Card> cardsInHand = new ArrayList<>();
    private ArrayList<Card> chosenDeck;
    private int mana;
    private Card hero;

    public int playerId;

    public Player(final Player player) {
        this.playerDecks = player.playerDecks;
        this.playerId = player.playerId;
    }

    public Player() {
    }

    public Decks getPlayerDecks() {
        return playerDecks;
    }

    public void setPlayerDecks(final Decks playerDecks) {
        this.playerDecks = playerDecks;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public Card getHero() {
        return hero;
    }

    public void setHero(final Card hero) {
        this.hero = hero;
    }

    public ArrayList<Card> getChosenDeck() {
        return chosenDeck;
    }

    public void setChosenDeck(final ArrayList<Card> chosenDeck) {
        this.chosenDeck = chosenDeck;
    }

    /**
     *
     */
    public void extractCard() {
        if (chosenDeck.size() == 0) {
            return;
        }
        cardsInHand.add(cardsInHand.size(), chosenDeck.get(0));
        chosenDeck.remove(0);
    }

    /**
     *
     */
    public ArrayList<Card> getEnvironmentCards() {
        ArrayList<Card> environmentList = new ArrayList<>();
        for (Card card : cardsInHand) {
            if (card.type.compareTo("environment") == 0) {
                environmentList.add(card);
            }
        }
        return environmentList;
    }
}


