package box;
import fileio.CardInput;
import fileio.Coordinates;
import java.util.ArrayList;
import java.util.Objects;

 public final class Card {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    //environment, minion, tank, specialMinionFata, specialMinionRowSpate,hero
    public String type;
    public boolean alreadyAttackedThisRound = false;
    public boolean frozen = false;
    private final int tableRowNumber = 4;
    private final int tableColumnNumber = 5;
    private final int three = 3;


    public Card(final CardInput cardInput) {
        this.name = cardInput.getName();
        this.colors = cardInput.getColors();
        this.description = cardInput.getDescription();
        this.health = cardInput.getHealth();
        this.mana = cardInput.getMana();
        this.attackDamage = cardInput.getAttackDamage();
        this.type = findType();

    }

    public Card(final Card card) {
        this.name = card.getName();
        this.colors = card.getColors();
        this.description = card.getDescription();
        this.health = card.getHealth();
        this.mana = card.getMana();
        this.attackDamage = card.getAttackDamage();
        this.type = findType();
        this.frozen = card.frozen;
        this.alreadyAttackedThisRound = card.alreadyAttackedThisRound;

    }

    public Card() {

    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * finds type by name in constructor
     */
    public String findType() {
        return switch (Objects.requireNonNull(this.name)) {
            case "The Ripper", "Miraj" -> "specialMinionFata";
            case "Goliath", "Warden" -> "tank";
            case "Sentinel", "Berserker" -> "minion";
            case "The Cursed One", "Disciple" -> "specialMinionSpate";
            case "Lord Royce", "Empress Thorina", "King Mudface", "General Kocioraw" -> "hero";
            case "Winterfell", "Heart Hound", "Firestorm" -> "environment";
            default -> "Wrong name.";
        };
    }

    /**
     *
     */
    public int useHeroAbility(final Player currentTurn, final Card hero, final int rowNumber) {
        if (hero.getMana() > currentTurn.getMana()) {
            return 1;
        }

        if (this.alreadyAttackedThisRound) {
            return 2;
        }

        if (hero.name.compareTo("Lord Royce") == 0 || hero.name.compareTo("Empress Thorina") == 0) {
            if ((GameTable.getInstance().rowBelongsToPlayer(currentTurn.playerId, rowNumber))) {
                return three;
            }
        }

        if (hero.name.compareTo("King Mudface") == 0
                || hero.name.compareTo("General Kocioraw") == 0) {
            if (!GameTable.getInstance().rowBelongsToPlayer(currentTurn.playerId, rowNumber)) {
                return tableRowNumber;
            }
        }

        switch (this.name) {
            case "Lord Royce" -> this.subZero(rowNumber);
            case "Empress Thorina" -> this.lowBlow(rowNumber);
            case "King Mudface" -> this.earthBorn(rowNumber);
            case "General Kocioraw" -> this.bloodThirst(rowNumber);
            default -> System.out.println("error");
        }

        hero.alreadyAttackedThisRound = true;
        currentTurn.setMana(currentTurn.getMana() - hero.getMana());
        return 0;
    }

    /**
     *
     */
    public int cardUsesAbility(final int playerIdx, final Coordinates cardAttacker,
                               final Coordinates cardAttacked) {
        Card victim = GameTable.getInstance().table[cardAttacked.getX()][cardAttacked.getY()];

        Card attacker = GameTable.getInstance().table[cardAttacker.getX()][cardAttacker.getY()];

        if (attacker.frozen) {
            return 1;
        }

        if (attacker.alreadyAttackedThisRound) {
            return 2;
        }

        if (attacker.name.compareTo("Disciple") == 0 && !(GameTable.getInstance().
                belongsToPlayer(playerIdx, cardAttacked))) {
            return three;
        }

        if (attacker.name.compareTo("The Cursed One") == 0 || attacker.name.compareTo("Miraj") == 0
                || attacker.name.compareTo("The Ripper") == 0) {
            if (GameTable.getInstance().belongsToPlayer(playerIdx, cardAttacked)) {
                return tableRowNumber;
            }

            if (GameTable.getInstance().areThereThanksToBeAttackedBy(playerIdx)) {
                if (victim.type.compareTo("tank") != 0) {
                    return tableColumnNumber;
                }
            }

        }

        switch (attacker.name) {
            case "The Ripper" -> this.weakKnees(cardAttacked);
            case "Miraj" -> this.skyjack(cardAttacked);
            case "The Cursed One" -> this.shapeshift(cardAttacked);
            case "Disciple" -> this.godsPlan(cardAttacked);
            default -> System.out.println("error");
        }

        attacker.alreadyAttackedThisRound = true;
        return 0;
    }

    /**
     * finds needed row to be placed on
     */
    public int neededRow(final int player) {

        if (player == 1) {
            return switch (type) {
                case "specialMinionFata", "tank" -> 2;
                case "minion", "specialMinionSpate" -> three;
                default -> -1;
            };
        }

        if (player == 2) {
            return switch (type) {
                case "specialMinionFata", "tank" -> 1;
                case "minion", "specialMinionSpate" -> 0;
                default -> -1;
            };
        }
        return -1;
    }

    /**
     *
     */
    public void useEnvironment(final int rowNumber) {
        switch (this.name) {
            case "Firestorm" -> this.firestorm(rowNumber);
            case "Winterfell" -> this.winterfell(rowNumber);
            case "Heart Hound" -> this.heartHound(rowNumber);
            default -> System.out.println("error");
        }
    }

    /**
     * ability
     */
    public void skyjack(final Coordinates coordinates) {
        //face swap între viața lui și viața unui minion din tabăra adversă.
        int keepLifeLvl = this.getHealth();
        this.setHealth(GameTable.getInstance().table[coordinates.getX()][coordinates.getY()].
                getHealth());
        GameTable.getInstance().table[coordinates.getX()][coordinates.getY()].
                setHealth(keepLifeLvl);
    }

    /**
     * ability
     */
    public void shapeshift(final Coordinates coordinates) {
        //face swap între viața unui minion din tabăra adversă și atacul aceluiași minion
        Card minion = GameTable.getInstance().table[coordinates.getX()][coordinates.getY()];
        int keepLifeLvl = minion.getHealth();
        minion.setHealth(GameTable.getInstance().table[coordinates.getX()][coordinates.getY()].
                getAttackDamage());
        minion.setAttackDamage(keepLifeLvl);

        // verifica daca are viata 0 dupa asta
        if (minion.getHealth() == 0) {
            GameTable.getInstance().table[coordinates.getX()][coordinates.getY()] = null;
            GameTable.getInstance().rearrangeTable();
        }
    }

    /**
     * ability
     */
    public void godsPlan(final Coordinates coordinates) {
        //+2 viață pentru un minion din tabăra lui.
        GameTable.getInstance().table[coordinates.getX()][coordinates.getY()].
                setHealth(GameTable.getInstance().
                        table[coordinates.getX()][coordinates.getY()].getHealth() + 2);

    }

    /**
     * ability
     */
    public void weakKnees(final Coordinates coordinates) {
        //-2 atac pentru un minion din tabăra adversă.
        Card minion = GameTable.getInstance().table[coordinates.getX()][coordinates.getY()];
        if (minion.getAttackDamage() >= 2) {
            minion.setAttackDamage(minion.getAttackDamage() - 2);
        }
    }

    /**
     * ability
     */
    public void subZero(final int rowNumber) {
        //ingheata cartea cu cel mai mare attack de pe rând.
        GameTable.getInstance().getCardWithBiggestValue("attack", rowNumber).frozen = true;
    }

    /**
     * ability
     */
    public void lowBlow(final int rowNumber) {
        //distruge cartea cu cea mai mare viata de pe rând.
        Card victim = GameTable.getInstance().getCardWithBiggestValue("health", rowNumber);
        for (int i = 0; i < tableColumnNumber; i++) {
            if (GameTable.getInstance().table[rowNumber][i].getHealth() == victim.getHealth()) {
                GameTable.getInstance().table[rowNumber][i] = null;
                GameTable.getInstance().rearrangeTable();
                return;
            }
        }
    }

    /**
     * ability
     */
    public void bloodThirst(final int rowNumber) {
        // +1 atac pentru toate cărțile de pe rând.
        for (int j = 0; j < tableColumnNumber; j++) {
            if (GameTable.getInstance().table[rowNumber][j] != null) {
                GameTable.getInstance().table[rowNumber][j].setAttackDamage(GameTable.getInstance().
                        table[rowNumber][j].getAttackDamage() + 1);
            }
        }
    }

    /**
     * ability
     */
    public void earthBorn(final int rowNumber) {
        //+1 viață pentru toate cărțile de pe rând.
        for (int j = 0; j < tableColumnNumber; j++) {
            if (GameTable.getInstance().table[rowNumber][j] != null) {
                GameTable.getInstance().table[rowNumber][j].setHealth(GameTable.getInstance().
                        table[rowNumber][j].getHealth() + 1);
            }
        }
    }

    /**
     * ability
     */
    public void firestorm(final int rowNumber) {
        for (int j = 0; j < tableColumnNumber; j++) {
            if (GameTable.getInstance().table[rowNumber][j] != null) {
                Card carte = GameTable.getInstance().table[rowNumber][j];
                carte.setHealth(carte.getHealth() - 1);
                if (carte.getHealth() <= 0) {
                    GameTable.getInstance().table[rowNumber][j] = null;
                }
            }
        }

        for (int j = 0; j < tableColumnNumber; j++) {
            GameTable.getInstance().rearrangeTable();
        }
    }

    /**
     * ability
     */
    public void winterfell(final int rowNumber) {
        for (int j = 0; j < tableColumnNumber; j++) {
            if (GameTable.getInstance().table[rowNumber][j] != null) {
                GameTable.getInstance().table[rowNumber][j].frozen = true;
            }
        }
    }

    /**
     * ability
     */
    public void heartHound(final int rowNumber) {
        Card newCard = new Card(GameTable.getInstance().
                getCardWithBiggestValue("health", rowNumber));
        GameTable.getInstance().getCardWithBiggestValue("health", rowNumber).
                setName("thisOneOfficer");

        for (int i = 0; i < tableColumnNumber; i++) {
            if (GameTable.getInstance().table[three - rowNumber][i] == null) {
                GameTable.getInstance().table[three - rowNumber][i] = newCard;
                return;
            }
        }

    }


    @Override
    public String toString() {
        return "Card{"
                + "mana="
                + mana
                + ", attackDamage="
                + attackDamage
                + ", health="
                + health
                + ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                + ""
                + name
                + '\''
                + '}';
    }
}


