package box;
import fileio.StartGameInput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class StartGame {
    private int shuffleSeed;
    private int startingPlayer;

    private Player player1, player2;
    private int player1Idx;
    private int player2Idx;
    private final int initialHealth = 30;


    public StartGame(final StartGameInput startGameInput, final Player player1,
                     final Player player2) {
        this.setPlayer1Idx(startGameInput.getPlayerOneDeckIdx());
        this.startingPlayer = startGameInput.getStartingPlayer();
        this.setPlayer2Idx(startGameInput.getPlayerTwoDeckIdx());
        this.shuffleSeed = startGameInput.getShuffleSeed();

        this.player1 = new Player(player1);
        this.player2 = new Player(player2);

        this.player1.setMana(1);
        this.player2.setMana(1);
        this.player1.setHero(new Card(startGameInput.getPlayerOneHero()));
        this.player2.setHero(new Card(startGameInput.getPlayerTwoHero()));
        this.player1.getHero().setHealth(initialHealth);
        this.player2.getHero().setHealth(initialHealth);
        this.player1.setChosenDeck(this.shuffleDeck(player1.getPlayerDecks().getDecks().
                get(player1Idx)));
        this.player2.setChosenDeck(this.shuffleDeck(player2.getPlayerDecks().getDecks().
                get(player2Idx)));

        //extract first cards
        this.player1.extractCard();
        this.player2.extractCard();
        GameTable.getInstance().cleanTable();
    }

    /**
     *
     */
    public Player getPlayer(final int index) {
        if (index == 1) {
            return player1;
        } else {
            return player2;
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getPlayer1Idx() {
        return player1Idx;
    }

    public void setPlayer1Idx(final int player1Idx) {
        this.player1Idx = player1Idx;
    }

    public int getPlayer2Idx() {
        return player2Idx;
    }

    public void setPlayer2Idx(final int player2Idx) {
        this.player2Idx = player2Idx;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public void setShuffleSeed(final int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    public Card getPlayerOneHero() {
        return player1.getHero();
    }
    /**
     * for coding style
     */
    public void setPlayerOneHero(final Card playerOneHero) {
        player1.setHero(playerOneHero);
    }
    /**
     * for coding style
     */
    public Card getPlayerTwoHero() {
        return player2.getHero();
    }
    /**
     * for coding style
     */
    public void setPlayerTwoHero(final Card playerTwoHero) {
        player2.setHero(playerTwoHero);
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(final int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    /**
     *
     */
    public ArrayList<Card> shuffleDeck(final ArrayList<Card> deck) {
        ArrayList<Card> deckToBeShuffled = new ArrayList<>();
        deckToBeShuffled.addAll(deck);
        Random rnd = new Random(this.getShuffleSeed());
        Collections.shuffle(deckToBeShuffled, rnd);
        return deckToBeShuffled;
    }

    @Override
    public String toString() {
        return "StartGame{"
                + "playerOneDeckIdx="
                + this.getPlayer1Idx()
                + ", playerTwoDeckIdx="
                + this.getPlayer2Idx()
                + ", shuffleSeed="
                + shuffleSeed
                + ", playerOneHero="
                + player1.getHero()
                + ", playerTwoHero="
                + player2.getHero()
                + ", startingPlayer="
                + startingPlayer
                + '}';
    }
}
