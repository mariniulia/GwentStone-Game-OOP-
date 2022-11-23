package main;

import box.Decks;
import box.Game;
import box.GameTable;
import box.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Input;

import java.util.ArrayList;

public final class Starter {
    //prapare players
    private Player player1 = new Player(), player2 = new Player();

    private ArrayList<Game> games = new ArrayList<>();

    public Starter(Input input) {

        //prepare players
        this.player1.setPlayerDecks(new Decks(input.getPlayerOneDecks()));
        this.player2.setPlayerDecks(new Decks(input.getPlayerTwoDecks()));

        //so we know who is who
        this.player1.playerId = 1;
        this.player2.playerId = 2;

        for (int i = 0; i < input.getGames().size(); i++) {
            games.add(new Game(input.getGames().get(i), player1, player2));
        }
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(final ArrayList<Game> games) {
        this.games = games;
    }

    /**
     *starting games
     */
    public void startBaby(ObjectMapper objectMapper, ArrayNode output) {
        //actualizam statisticile jocului
        GameTable.gamesPlayed = 0;
        GameTable.player1Wins = 0;
        GameTable.player2Wins = 0;

        for (Game game : games) {
            GameTable.gamesPlayed++;
            game.startActions(objectMapper, output);

        }
    }
}
