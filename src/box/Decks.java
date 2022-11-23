package box;
import fileio.DecksInput;
import java.util.ArrayList;

 public final class Decks {
    private int nrCardsInDeck;
    private ArrayList<ArrayList<Card>> decks = new ArrayList<>();
    private int nrDecks;

    public Decks(final DecksInput decksInput) {
        this.nrDecks = decksInput.getNrDecks();
        this.nrCardsInDeck = decksInput.getNrCardsInDeck();
        for (int i = 0; i < nrDecks; i++) {
            decks.add(new ArrayList<>());
            for (int j = 0; j < nrCardsInDeck; j++) {
                decks.get(i).add(new Card(decksInput.getDecks().get(i).get(j)));
            }
        }

    }

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public int getNrDecks() {
        return nrDecks;
    }

    public void setNrDecks(final int nrDecks) {
        this.nrDecks = nrDecks;
    }

    public ArrayList<ArrayList<Card>> getDecks() {
        return decks;
    }

    public void setDecks(final ArrayList<ArrayList<Card>> decks) {
        this.decks = decks;
    }

    @Override
    public String toString() {
        return "Info{"
                + "nr_cards_in_deck="
                + nrCardsInDeck
                + ", nr_decks="
                + nrDecks
                + ", decks="
                + decks
                + '}';
    }
}
