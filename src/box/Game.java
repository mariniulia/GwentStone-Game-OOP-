package box;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.GameInput;

import java.util.ArrayList;

public final class Game {
    private StartGame startGame;
    private ArrayList<Actions> actions = new ArrayList<>();

    public int currentTurn;
    public int roundNumber;

    public String gameStatus = "ongoing";

    public Game(final GameInput gameInput, final Player player1, final Player player2) {
        this.startGame = new StartGame(gameInput.getStartGame(), player1, player2);

        for (int i = 0; i < gameInput.getActions().size(); i++) {
            actions.add(new Actions(gameInput.getActions().get(i)));

        }
        this.currentTurn = startGame.getStartingPlayer();
        this.roundNumber = 1;

    }

    public StartGame getStartGame() {
        return startGame;
    }

    public void setStartGame(final StartGame startGame) {
        this.startGame = startGame;
    }

    public ArrayList<Actions> getActions() {
        return actions;
    }

    public void setActions(final ArrayList<Actions> actions) {
        this.actions = actions;
    }

    /**
     *
     */
    public void endRound() {
        roundNumber++;

        //Adaugam mana conforma rundei
        if (roundNumber < 10) {
            startGame.getPlayer1().setMana(startGame.getPlayer1().getMana() + roundNumber);
            startGame.getPlayer2().setMana(startGame.getPlayer2().getMana() + roundNumber);
        }

        //Extragem cate o carte
        startGame.getPlayer1().extractCard();
        startGame.getPlayer2().extractCard();

    }

    /**
     *
     */
    public void endPlayerTurn() {
        GameTable.getInstance().clearCorpses();
        GameTable.getInstance().defrozeCards(currentTurn);
        startGame.getPlayer(currentTurn).getHero().alreadyAttackedThisRound = false;
        currentTurn = currentTurn % 2 + 1;
        if (startGame.getStartingPlayer() == currentTurn) {
            endRound();
        }
    }

    /**
     *
     */
    public int placeCard(final int currentTurn, final int handIdx) {
        Player currentPlayer;
        if (currentTurn == 1) {
            currentPlayer = startGame.getPlayer1();
        } else
            currentPlayer = startGame.getPlayer2();

        Card card = new Card(currentPlayer.getCardsInHand().get(handIdx));

        //check is card is environment card
        if (card.type.compareTo("environment") == 0) {
            return 1;
        }
        //check if card has lower cost than player's mana
        if (card.getMana() > currentPlayer.getMana()) {
            return 2;
        }
        //check if the row is full
        if (GameTable.getInstance().isThisRowFull(card.neededRow(currentTurn))) {
            return 2 + 1;
        }
        //placeCard
        GameTable.getInstance().placeCardOnTable(card.neededRow(currentTurn), card);

        //update player mana
        currentPlayer.setMana(currentPlayer.getMana() - card.getMana());

        //remove card from hand
        currentPlayer.getCardsInHand().remove(handIdx);
        return 0;
    }

    /**
     * starts each action in game
     */
    public void startActions(final ObjectMapper objectMapper, final ArrayNode output) {
        for (Actions action : actions) {
            ObjectNode node = objectMapper.createObjectNode();
            action.start(node, this);
            if (action.getHasOutput()) {
                output.add(node);
            }
        }
        GameTable.getInstance().cleanTable();

    }

    @Override
    public String toString() {
        return "Game{"
                + "startGame="
                + startGame
                + ", actions="
                + actions
                + '}';
    }

}
