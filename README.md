
git
# Tema POO  - GwentStone

<div align="center"><img src="https://tenor.com/view/witcher3-gif-9340436.gif" width="500px"></div>


- [ ] Clasa gameTable reprezinta partea fizica a jocului, tabla de joc. Este implementata de tip singleton,pentru ca vreau sa fie o singura instanta,usor de accesat din toate clasele .
  Ea contine metode ce tin de managerierea tablei cum ar fi :curatarea tablei dupa fiecare joc, gasirea cartilor cu anumite atribute, gasirea cartii la o pozitie. Tot aici am introdus si statisticile de tip static,fiind universal valabile,indiferent de joc,ca si cum ar fi un carnetel pe masa,in care se tine scorul.

- [ ] BONUS : implementarea este una tip matrice, am considerat ca e varianta mai apropiata de realitate, atunci cand ne jucam,lasam spatii goale pe masa, nu facem shiftari la stanga. Am facut o functie de shiftare a cartilor la stanga pentru a respecta cerinta,dar prin comentarea functiei de shiftare,obtinem asezarea reala a cartilor.

- [ ] Clasa Card impementeaza toate cartile prin adaugarea campului “type”, care se atribuie in constructor prin metoda getType,ce verifica numele si o incadreaza. Am considerat ca face implementarea mai intuitiva decat construirea multor clase.
- [ ] La inceputul jocurilor am construit playerii, pentru a putea retine usor cartile lor din mana,id-uri si locurile care le apartin pe tabla. Nu uitam sa le luam cartile din mana dupa fiecare joc.
- [ ] Game gestioneaza jocul,retine tura curenta a jucatorului

## Skel Structure

* src/
  * checker/ - checker files
  * fileio/ - contains classes used to read data from the json files
  * main/
      * Main - the Main class runs the checker on your implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
      * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
        to the out.txt file. Thus, you can compare this result with ref.
      * Starter - starts each game, by starting each action explicitly
* input/ - contains the tests in JSON format
* ref/ - contains all reference output for the tests in JSON format
* box/ -contains the component needed by the game(like a real game)
*     Game 
*     Cards
*     GameTable
*     Actions
*     Player
*     Decks
*     StartGame

## Teste

1. test01_game_start - 3p
2. test02_place_card - 4p
3. test03_place_card_invalid - 4p
4. test04_use_env_card - 4p
5. test05_use_env_card_invalid - 4p
6. test06_attack_card - 4p
7. test07_attack_card_invalid - 4p
8. test08_use_card_ability - 4p
9. test09_use_card_ability_invalid -4p
10. test10_attack_hero - 4p
11. test11_attack_hero_invalid - 4p
12. test12_use_hero_ability_1 - 4p
13. test13_use_hero_ability_2 - 4p
14. test14_use_hero_ability_1_invalid - 4p
15. test15_use_hero_ability_2_invalid - 4p
16. test16_multiple_games_valid - 5p
17. test17_multiple_games_invalid - 6p
18. test18_big_game - 10p


<div align="center"><img src="https://tenor.com/view/homework-time-gif-24854817.gif" width="500px"></div>
