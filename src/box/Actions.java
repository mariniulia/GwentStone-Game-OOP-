package box;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Coordinates;
import java.util.ArrayList;

public final class Actions {
    private String command;
    private int handIdx;
    private Coordinates cardAttacker;
    private Coordinates cardAttacked;
    private int affectedRow;
    private int playerIdx;
    private boolean hasOutput = true;
    private int x;
    private int y;

    private final int lastRow = 3;
    private final int tableRowNumber = 4;

    private final int tableColumnNumber = 5;


    public Actions(final ActionsInput actionsInput) {
        this.command = actionsInput.getCommand();
        this.handIdx = actionsInput.getHandIdx();
        this.cardAttacker = actionsInput.getCardAttacker();
        this.cardAttacked = actionsInput.getCardAttacked();
        this.affectedRow = actionsInput.getAffectedRow();
        this.playerIdx = actionsInput.getPlayerIdx();
        this.x = actionsInput.getX();
        this.y = actionsInput.getY();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public void setHandIdx(final int handIdx) {
        this.handIdx = handIdx;
    }

    public Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public void setCardAttacker(final Coordinates cardAttacker) {
        this.cardAttacker = cardAttacker;
    }

    public Coordinates getCardAttacked() {
        return cardAttacked;
    }

    public void setCardAttacked(final Coordinates cardAttacked) {
        this.cardAttacked = cardAttacked;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public void setAffectedRow(final int affectedRow) {
        this.affectedRow = affectedRow;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public void setPlayerIdx(final int playerIdx) {
        this.playerIdx = playerIdx;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    /**
     * method used for transitioning card into a node for output
     */
    public ObjectNode cardToNode(final ObjectNode node, final Card card) {
        node.put("mana", card.getMana());
        if (!(card.type.compareTo("environment") == 0 || card.type.compareTo("hero") == 0)) {
            node.put("attackDamage", card.getAttackDamage());
            node.put("health", card.getHealth());
            //node.put("type", card.type);
        }
        node.put("description", card.getDescription());
        ArrayNode colors = node.putArray("colors");
        for (String color : card.getColors()) {
            colors.add(color);
        }
        node.put("name", card.getName());

        if (card.type.compareTo("hero") == 0) {
            node.put("health", card.getHealth());
        }

        return node;
    }

    /**
     * method used for transitioning coordinates into a node for output
     */
    public ObjectNode coordToNode(final ObjectNode node, final Coordinates coordinates) {
        node.put("x", coordinates.getX());
        node.put("y", coordinates.getY());
        return node;
    }

    /**
     * tests to se if the command requires output
     */
    public boolean getHasOutput() {
        return hasOutput;
    }

    /**
     * method used starting each actions and generates specific output
     */
    public void start(final ObjectNode node, final Game currentGame) {
        ObjectMapper objectMapper = new ObjectMapper();
        switch (command) {
            case "cardUsesAttack" -> cardUsesAttack(node, currentGame, objectMapper);
            case "useAttackHero" -> useAttackHero(node, currentGame, objectMapper);
            case "cardUsesAbility" -> cardUsesAbility(node, currentGame, objectMapper);
            case "useHeroAbility" -> useHeroAbility(node, currentGame);
            case "placeCard" -> placeCard(node, currentGame);
            case "getCardsInHand" -> getCardsInHand(node, currentGame, objectMapper);
            case "getPlayerDeck" -> getPlayerDeck(node, currentGame, objectMapper);
            case "useEnvironmentCard" -> useEnvironmentCard(node, currentGame);
            case "getPlayerTurn" -> getPlayerTurn(node, currentGame);
            case "getPlayerHero" -> getPlayerHero(node, currentGame, objectMapper);
            case "getCardAtPosition" -> gatCardAtPosition(node, objectMapper);
            case "endPlayerTurn" -> endPlayerTurn(currentGame);
            case "getPlayerMana" -> getPlayerMana(node, currentGame);
            case "getCardsOnTable" -> getCardsOnTable(node, objectMapper);
            case "getEnvironmentCardsInHand" -> getEnvironmentCardsInHand(node,
                    currentGame, objectMapper);
            case "getFrozenCardsOnTable" -> getFrozenCardsOnTable(node, objectMapper);
            case "getPlayerOneWins" -> {
                node.put("command", "getPlayerOneWins");
                node.put("output", GameTable.player1Wins);
            }
            case "getPlayerTwoWins" -> {
                node.put("command", "getPlayerTwoWins");
                node.put("output", GameTable.player2Wins);
            }
            case "getTotalGamesPlayed" -> {
                node.put("command", "getTotalGamesPlayed");
                node.put("output", GameTable.gamesPlayed);
            }
            default -> node.put("error", "unknown command");

        }


    }

    private static void getPlayerTurn(final ObjectNode node, final Game currentGame) {
        node.put("command", "getPlayerTurn");
        node.put("output", currentGame.currentTurn);
    }

    private void getPlayerMana(final ObjectNode node, final Game currentGame) {
        node.put("command", "getPlayerMana");
        node.put("playerIdx", playerIdx);
        node.put("output", currentGame.getStartGame().getPlayer(playerIdx).getMana());
    }

    private void getPlayerHero(final ObjectNode node,
                               final Game currentGame, final ObjectMapper objectMapper) {
        node.put("command", "getPlayerHero");
        node.put("playerIdx", playerIdx);
        ObjectNode carte = objectMapper.createObjectNode();
        carte = cardToNode(carte, currentGame.getStartGame().getPlayer(playerIdx).getHero());
        node.put("output", carte);
    }

    private void endPlayerTurn(final Game currentGame) {
        if (currentGame.gameStatus.compareTo("ended") == 0) {
            return;
        }
        currentGame.endPlayerTurn();
        this.hasOutput = false;
    }

    private void gatCardAtPosition(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("command", "getCardAtPosition");
        node.put("x", x);
        node.put("y", y);
        if (GameTable.getInstance().table[x][y] != null) {
            ObjectNode carte = objectMapper.createObjectNode();
            carte = cardToNode(carte, GameTable.getInstance().table[x][y]);
            node.put("output", carte);
        } else {
            node.put("output", "No card available at that position.");
        }
    }

    private void getCardsOnTable(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("command", "getCardsOnTable");
        ArrayNode cards = objectMapper.createArrayNode();

        for (int i = 0; i < tableRowNumber; i++) {
            ArrayNode cards2 = objectMapper.createArrayNode();
            for (int j = 0; j < tableColumnNumber; j++) {
                if (GameTable.getInstance().table[i][j] != null) {
                    ObjectNode carte = objectMapper.createObjectNode();
                    carte = cardToNode(carte, GameTable.getInstance().table[i][j]);
                    cards2.add(carte);
                }
            }
            cards.add(cards2);
        }
        node.put("output", cards);
    }

    private void getEnvironmentCardsInHand(
            final ObjectNode node, final Game currentGame, final ObjectMapper objectMapper) {
        node.put("command", "getEnvironmentCardsInHand");
        node.put("playerIdx", playerIdx);
        ArrayNode cards = objectMapper.createArrayNode();
        ArrayList<Card> enviromentList =
                currentGame.getStartGame().getPlayer(playerIdx).getEnvironmentCards();
        for (Card card : enviromentList) {
            ObjectNode carte = objectMapper.createObjectNode();
            cards.add(cardToNode(carte, card));
        }
        node.put("output", cards);
    }

    private void getFrozenCardsOnTable(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("command", "getFrozenCardsOnTable");
        ArrayNode cards = objectMapper.createArrayNode();
        ArrayList<Card> frozenCards = GameTable.getInstance().getFrozenCards();
        for (Card frozenCard : frozenCards) {
            ObjectNode carte = objectMapper.createObjectNode();
            cards.add(cardToNode(carte, frozenCard));
        }
        node.put("output", cards);
    }

    private void useEnvironmentCard(final ObjectNode node, final Game currentGame) {
        if (currentGame.gameStatus.compareTo("ended") == 0) {
            return;
        }
        node.put("command", "useEnvironmentCard");
        node.put("handIdx", handIdx);
        node.put("affectedRow", affectedRow);
        Player currentPlayer = currentGame.getStartGame().getPlayer(currentGame.currentTurn);
        if (currentPlayer.getCardsInHand().get(handIdx).type.compareTo("environment") != 0) {
            node.put("error", "Chosen card is not of type environment.");
            return;
        }

        if (currentPlayer.getMana() < currentPlayer.getCardsInHand().get(handIdx).getMana()) {
            node.put("error", "Not enough mana to use environment card.");
            return;
        }

        if (GameTable.getInstance().rowBelongsToPlayer(currentPlayer.playerId, affectedRow)) {
            node.put("error", "Chosen row does not belong to the enemy.");
            return;
        }
        boolean test = currentPlayer.getCardsInHand().get(handIdx).getName().
                compareTo("Heart Hound") == 0;

        if (test && GameTable.getInstance().isThisRowFull(lastRow - affectedRow)) {
            node.put("error", "Cannot steal enemy card since the player's row is full.");
            return;
        }

        //use card
        currentPlayer.getCardsInHand().get(handIdx).useEnvironment(affectedRow);

        //set new mana
        currentPlayer.setMana(currentPlayer.getMana()
                - currentPlayer.getCardsInHand().get(handIdx).getMana());

        //delete card
        currentPlayer.getCardsInHand().remove(handIdx);

        this.hasOutput = false;
    }

    private void getPlayerDeck(final ObjectNode node, final Game currentGame,
                               final ObjectMapper objectMapper) {
        node.put("command", "getPlayerDeck");
        node.put("playerIdx", playerIdx);
        ArrayNode cards = objectMapper.createArrayNode();
        for (int i = 0; i
                < currentGame.getStartGame().getPlayer(playerIdx).getChosenDeck().size(); i++) {
            ObjectNode carte = objectMapper.createObjectNode();
            carte = cardToNode(carte,
                    currentGame.getStartGame().getPlayer(playerIdx).getChosenDeck().get(i));
            cards.add(carte);
        }
        node.put("output", cards);
    }

    private void getCardsInHand(final ObjectNode node, final Game currentGame,
                                final ObjectMapper objectMapper) {
        node.put("command", "getCardsInHand");
        node.put("playerIdx", playerIdx);
        ArrayNode cards = objectMapper.createArrayNode();
        for (int i = 0; i < currentGame.getStartGame().getPlayer(playerIdx).
                getCardsInHand().size(); i++) {

            ObjectNode carte = objectMapper.createObjectNode();
            carte = cardToNode(carte, currentGame.getStartGame().
                    getPlayer(playerIdx).getCardsInHand().get(i));
            cards.add(carte);
        }
        node.put("output", cards);
    }

    private void placeCard(final ObjectNode node, final Game currentGame) {
        if (currentGame.gameStatus.compareTo("ended") == 0) {
            return;
        }

        node.put("command", "placeCard");
        node.put("handIdx", handIdx);
        int errorType = currentGame.placeCard(currentGame.currentTurn, handIdx);
        switch (errorType) {
            case 1 -> node.put("error", "Cannot place environment card on table.");
            case 2 -> node.put("error", "Not enough mana to place card on table.");
            case lastRow -> node.put("error", "Cannot place card on table since row is full.");
            case 0 -> this.hasOutput = false;
            default -> node.put("error", "unknown command");
        }
    }

    private void useHeroAbility(final ObjectNode node, final Game currentGame) {
        if (currentGame.gameStatus.compareTo("ended") == 0) {
            return;
        }
        node.put("command", "useHeroAbility");
        node.put("affectedRow", affectedRow);

        Card attacker = currentGame.getStartGame().getPlayer(currentGame.currentTurn).getHero();

        int errorType = attacker.useHeroAbility(currentGame.getStartGame().
                        getPlayer(currentGame.currentTurn),
                attacker, affectedRow);

        switch (errorType) {
            case 1 -> node.put("error", "Not enough mana to use hero's ability.");
            case 2 -> node.put("error", "Hero has already attacked this turn.");
            case lastRow -> node.put("error", "Selected row does not belong to the enemy.");
            case tableRowNumber -> node.put("error",
                    "Selected row does not belong to the current player.");
            case 0 -> this.hasOutput = false;
            default -> node.put("error", "unknown command");
        }
    }

    private void cardUsesAbility(final ObjectNode node, final Game currentGame,
                                 final ObjectMapper objectMapper) {
        if (currentGame.gameStatus.compareTo("ended") == 0) {
            return;
        }

        node.put("command", "cardUsesAbility");
        ObjectNode coord = objectMapper.createObjectNode();
        coord = coordToNode(coord, cardAttacker);
        node.put("cardAttacker", coord);


        ObjectNode coord2 = objectMapper.createObjectNode();
        coord2 = coordToNode(coord2, cardAttacked);
        node.put("cardAttacked", coord2);

        int errorType = GameTable.getInstance().table[cardAttacker.getX()][cardAttacker.getY()].
                cardUsesAbility(currentGame.currentTurn, cardAttacker, cardAttacked);
        switch (errorType) {
            case 1 -> node.put("error", "Attacker card is frozen.");
            case 2 -> node.put("error", "Attacker card has already attacked this turn.");
            case lastRow -> node.put("error",
                    "Attacked card does not belong to the current player.");
            case tableRowNumber -> node.put("error", "Attacked card does not belong to the enemy.");
            case tableColumnNumber -> node.put("error", "Attacked card is not of type 'Tank'.");
            case 0 -> this.hasOutput = false;
            default -> node.put("error", "unknown command");
        }
    }

    private void useAttackHero(final ObjectNode node, final Game currentGame,
                               final ObjectMapper objectMapper) {
        if (currentGame.gameStatus.compareTo("ended") == 0) {
            return;
        }

        int errorType = GameTable.getInstance().attackHero(currentGame.currentTurn,
                cardAttacker, currentGame.
                        getStartGame().getPlayer(lastRow - currentGame.currentTurn));
        switch (errorType) {
            case 1 -> {
                node.put("command", "useAttackHero");

                ObjectNode coord = objectMapper.createObjectNode();
                coord = coordToNode(coord, cardAttacker);
                node.put("cardAttacker", coord);
                node.put("error", "Attacker card is frozen.");
            }
            case 2 -> {
                node.put("command", "useAttackHero");

                ObjectNode coord = objectMapper.createObjectNode();
                coord = coordToNode(coord, cardAttacker);
                node.put("cardAttacker", coord);
                node.put("error", "Attacker card has already attacked this turn.");
            }
            case lastRow -> {
                node.put("command", "useAttackHero");

                ObjectNode coord = objectMapper.createObjectNode();
                coord = coordToNode(coord, cardAttacker);
                node.put("cardAttacker", coord);
                node.put("error", "Attacked card is not of type 'Tank'.");
            }
            case -1 -> {
                if (currentGame.currentTurn == 1) {
                    GameTable.player1Wins++;
                    node.put("gameEnded", "Player one killed the enemy hero.");
                }
                if (currentGame.currentTurn == 2) {
                    GameTable.player2Wins++;
                    node.put("gameEnded", "Player two killed the enemy hero.");
                }
                currentGame.gameStatus = "ended";
            }
            case 0 -> this.hasOutput = false;
            default -> node.put("error", "unknown command");
        }
    }

    private void cardUsesAttack(final ObjectNode node, final Game currentGame,
                                final ObjectMapper objectMapper) {
        if (currentGame.gameStatus.compareTo("ended") == 0) {
            return;
        }
        node.put("command", "cardUsesAttack");

        ObjectNode coord = objectMapper.createObjectNode();
        coord = coordToNode(coord, cardAttacker);
        node.put("cardAttacker", coord);

        ObjectNode coord2 = objectMapper.createObjectNode();
        coord2 = coordToNode(coord2, cardAttacked);
        node.put("cardAttacked", coord2);

        int errorType = GameTable.getInstance().attack(currentGame.currentTurn,
                cardAttacker, cardAttacked);
        switch (errorType) {
            case 1 -> node.put("error", "Attacked card does not belong to the enemy.");
            case 2 -> node.put("error", "Attacker card has already attacked this turn.");
            case lastRow -> node.put("error", "Attacker card is frozen.");
            case tableRowNumber -> node.put("error", "Attacked card is not of type 'Tank'.");
            case 0 -> this.hasOutput = false;
            default -> node.put("error", "unknown command");
        }
    }

    @Override
    public String toString() {
        return "Actions{"
                + "command='"
                + command + '\''
                + ", handIdx="
                + handIdx
                + ", cardAttacker="
                + cardAttacker
                + ", cardAttacked="
                + cardAttacked
                + ", affectedRow="
                + affectedRow
                + ", playerIdx="
                + playerIdx
                + ", x="
                + x
                + ", y="
                + y
                + '}';
    }
}
