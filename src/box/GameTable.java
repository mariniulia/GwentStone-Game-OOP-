package box;

import fileio.Coordinates;

import java.util.ArrayList;

public final class GameTable {
    private final int tableRowNumber = 4;
    private final int tableColumnNumber = 5;
    Card[][] table = new Card[tableRowNumber][tableColumnNumber];
    static public int player1Wins = 0;
    static public int player2Wins = 0;
    static public int gamesPlayed = 0;
    private final static GameTable GameTable = new GameTable();

    private GameTable() {
    }

    public static GameTable getInstance() {
        return GameTable;
    }

    /**
     *
     */
    public void cleanTable() {
        for (int i = 0; i < tableRowNumber; i++) {
            for (int j = 0; j < tableColumnNumber; j++) {
                table[i][j] = null;
            }
        }
    }

    /**
     *
     */
    public void rearrangeTable() {
        for (int i = 0; i < tableRowNumber; i++) {
            for (int j = 0; j < tableRowNumber; j++) {
                if (table[i][j] == null && table[i][j + 1] != null) {
                    table[i][j] = new Card(table[i][j + 1]);
                    table[i][j + 1] = null;
                }
            }
        }
    }

    /**
     *
     */
    public void clearCorpses() {
        for (int i = 0; i < tableRowNumber; i++) {
            for (int j = 0; j < tableColumnNumber; j++) {
                if (table[i][j] != null && table[i][j].getName().
                        compareTo("thisOneOfficer") == 0) {
                    table[i][j] = null;
                    rearrangeTable();
                    return;
                }
            }
        }
    }

    /**
     *
     */
    public boolean isThisRowFull(final int rowNumber) {
        for (int i = 0; i < tableColumnNumber; i++) {
            if (table[rowNumber][i] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     */
    public boolean areThereThanksToBeAttackedBy(final int indexAttacker) {
        for (int i = 0; i < tableColumnNumber; i++) {
            if (table[indexAttacker][i] != null
                    && table[indexAttacker][i].type.compareTo("tank") == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    public int getBiggestValue(final String characteristic, final int rowNumber) {
        int biggestValue = 0;
        if (characteristic.compareTo("health") == 0) {
            for (int i = 0; i < tableColumnNumber; i++) {
                if (table[rowNumber][i] != null
                        && table[rowNumber][i].getHealth() >= biggestValue) {
                    biggestValue = table[rowNumber][i].getHealth();
                }
            }
            return biggestValue;
        }
        if (characteristic.compareTo("attack") == 0) {
            for (int i = 0; i < tableColumnNumber; i++) {
                if (table[rowNumber][i] != null
                        && table[rowNumber][i].getAttackDamage() >= biggestValue) {
                    biggestValue = table[rowNumber][i].getAttackDamage();
                }
            }
            return biggestValue;
        }
        return -1;
    }

    /**
     *
     */
    public Card getCardWithBiggestValue(final String characteristic, final int rowNumber) {
        int searchFor = getBiggestValue(characteristic, rowNumber);
        if (characteristic.compareTo("health") == 0) {
            for (int i = 0; i < tableColumnNumber; i++) {
                if (table[rowNumber][i] != null && searchFor == table[rowNumber][i].getHealth()) {
                    return table[rowNumber][i];
                }
            }

        }
        if (characteristic.compareTo("attack") == 0) {
            for (int i = 0; i < tableColumnNumber; i++) {
                if (table[rowNumber][i] != null
                        && searchFor == table[rowNumber][i].getAttackDamage()) {
                    return table[rowNumber][i];
                }
            }

        }
        return null;
    }

    /**
     *
     */
    public boolean belongsToPlayer(final int playerIndex, final Coordinates card) {
        if (card.getX() >= 2 && playerIndex == 1) {
            return true;
        }
        return card.getX() < 2 && playerIndex == 2;
    }

    /**
     *
     */
    public boolean rowBelongsToPlayer(final int playerIndex, final int row) {
        if (row >= 2 && playerIndex == 1) {
            return true;
        }
        return row < 2 && playerIndex == 2;
    }

    /**
     *
     */
    public int attackHero(final int attackerIndex, final Coordinates cardAttacker,
                          final Player attackedPlayer) {
        Card victim = attackedPlayer.getHero();
        Card attacker = table[cardAttacker.getX()][cardAttacker.getY()];

        if (attacker == null) {
            return 1;
        }
        if (attacker.frozen) {
            return 1;
        }

        if (attacker.alreadyAttackedThisRound) {
            return 2;
        }

        if (areThereThanksToBeAttackedBy(attackerIndex)) {
            if (victim.type.compareTo("tank") != 0) {
                return 2 + 1;
            }
        }

        victim.setHealth(victim.getHealth() - attacker.getAttackDamage());
        attacker.alreadyAttackedThisRound = true;

        if (victim.getHealth() <= 0) {
            return -1;
        }

        rearrangeTable();
        return 0;
    }

    /**
     *
     */
    public int attack(final int playerIndex, final Coordinates cardAttacker,
                      final Coordinates cardAttacked) {
        Card victim = table[cardAttacked.getX()][cardAttacked.getY()];
        Card attacker = table[cardAttacker.getX()][cardAttacker.getY()];

        if (attacker == null) {
            return 1;
        }

        if (belongsToPlayer(playerIndex, cardAttacked)) {
            return 1;
        }

        if (attacker.alreadyAttackedThisRound) {
            return 2;
        }

        if (attacker.frozen) {
            return 2 + 1;
        }

        if (areThereThanksToBeAttackedBy(playerIndex)) {
            if (victim.type.compareTo("tank") != 0) {
                return tableRowNumber;
            }
        }

        table[cardAttacked.getX()][cardAttacked.getY()].setHealth(table[cardAttacked.
                getX()][cardAttacked.getY()].getHealth() - attacker.getAttackDamage());
        table[cardAttacker.getX()][cardAttacker.getY()].alreadyAttackedThisRound = true;
        if (victim.getHealth() <= 0) {
            table[cardAttacked.getX()][cardAttacked.getY()] = null;
        }

        rearrangeTable();
        return 0;
    }

    /**
     *
     */
    public void defrozeCards(final int player) {
        if (player == 1) {
            for (int i = 2; i < tableRowNumber; i++) {
                resetCards(i);
            }

        }
        if (player == 2) {
            for (int i = 0; i < 2; i++) {
                resetCards(i);
            }
        }
    }

    /**
     * after round cards are not frozen , and have not already attacked
     */
    private void resetCards(final int i) {
        for (int j = 0; j < tableColumnNumber; j++) {
            if (table[i][j] != null && table[i][j].frozen) {
                table[i][j].frozen = false;
            }
            if (table[i][j] != null) {
                table[i][j].alreadyAttackedThisRound = false;
            }

        }
    }

    /**
     *
     */
    public void placeCardOnTable(final int rowNumber, final Card card) {
        for (int i = 0; i < tableColumnNumber; i++) {
            if (table[rowNumber][i] == null) {
                table[rowNumber][i] = new Card(card);
                return;
            }
        }
    }

    /**
     *
     */
    public ArrayList<Card> getFrozenCards() {
        ArrayList<Card> frozenList = new ArrayList<>();
        for (int i = 0; i < tableRowNumber; i++) {
            for (int j = 0; j < tableColumnNumber; j++) {
                if (table[i][j] != null && table[i][j].frozen) {
                    frozenList.add(table[i][j]);
                }
            }
        }
        return frozenList;
    }

}
