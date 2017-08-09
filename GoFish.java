import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author AlexBeard, NishaChaube, SimonAnguish
 * 
 * This method runs the game,and includes: 
 * all of the gameplay logic
 * the different logic for each mode
 * Statistics / result of game (win/loss/tie) at end of game
 * 
 * ----------------------------------------------------------
 * Work Distribution:
 * 
 * This class was initially written by Nisha and Simon (the backbone of the simple mode,
 * including the exit at any time option).
 * Alex added end of game statistics/charts, integration of different modes, and general bug fixing
 * (mainly of transferring cards that were asked for when there were more than one of them to give, among other things).
 *
 */
public class GoFish {

 public static final String SIMPLE_MODE = "0";
 public static final String SMART_MODE = "1";
 public static final String DEVIOUS_MODE = "2";

 public static String mode;	
 public static int possible_lie_counter = 1;
 public static boolean computer_go_fish;
 public static String last_new_symbol_drawn;
	
 public static int book = 0;
 public static List<String> computer_book = new ArrayList<String>();
 public static List<String> player_book = new ArrayList<String>();
 public static java.util.Scanner stdin = new java.util.Scanner(System.in);
 public static Set handSet;
 public static Iterator handIterator;
 
 //Statistics:
 //public static double number_turns = 0;
 //public static double cards_handed_over = 0;
 public static List<Integer> computer_recieved = new ArrayList<Integer>();
 public static List<Integer> player_recieved = new ArrayList<Integer>();

 
 /**
     Asks a human player for a valid rank in his hand.
     Prompts user again if input is invalid.
     @param the hashmap of players hand
     @return a rank from the hand
  */
 public static String getValidRank(Run r, HashMap < String, Integer > hand) {
  while (true) {
   System.out.println("Please enter the value of the card you'd like to ask for:");
   System.out.printf("To exit the game at any time: type 'exit'\n\n");
   String line = stdin.nextLine();
   try {
    if (r.hasCardBySymbol(line, hand)|| line.equals("exit"))
     return line;
    else{
      System.out.println("Invalid! Enter again");
   }
   } catch (Exception e) {}
  }
 }

  /**
     Iterate over hashmap .
     Iterates and calculates the number of books.
     @param the hahsmap of computer/player hand
     @return total books from the computer/player hand
  */
 public static List<String> findBooks(Run r, HashMap < String, Integer > hand) {  
  List<String> book_values = new ArrayList<String>();
  handSet = hand.entrySet();
  handIterator = handSet.iterator();
  //find books
  while (handIterator.hasNext()) {
   Map.Entry playerEntry = (Map.Entry) handIterator.next();
   if (Integer.valueOf(playerEntry.getValue().toString()) == 4){
    book_values.add(playerEntry.getKey().toString());
   }
  }
  
  //clear values
  for (String value : book_values) {
	  r.clearCardsBySymbol(value, hand);
  }
  
  return book_values;
 }

 public static void main(String str[]) {
	 
  System.out.println("Go Fish Game");
  System.out.println("Rules of the game:");
  System.out.println("You can only ask for a card that you have at least one of already.");
  System.out.println("When you collect 4 of the same card (K, 9, etc) you lay it down as a book.");
  System.out.println("At the end of the game (when the deck is empty), whoever has the most books wins!");
  System.out.println("------------------------------------------");
  System.out.println("What game mode would you like to play?");
  System.out.println("Simple Mode (Press 0)");
  System.out.println("Smart Mode (Press 1)");
  System.out.println("Devious Mode (Press 2)");
  System.out.println();
  mode = stdin.nextLine();
  if(mode.equals("exit"))
  {
	System.out.println("Bye Bye. Play Again!");
	System.exit(0);
  }
  while (!mode.equals(SIMPLE_MODE) && !mode.equals(SMART_MODE) && !mode.equals(DEVIOUS_MODE)) {
	  System.out.println("Invalid - enter valid mode: ");
	  mode = stdin.nextLine();
  }

  
  //Run has all the methods
  Run r = new Run();
  r.initializeDeck();
  r.shuffleDeckToHands();
  
  //setting the turn
  boolean human_turn = true;
  boolean conti=true;

  while(conti==true){
  if((r.player_hand.isEmpty()) && (r.computer_hand.isEmpty()) == true)   
      conti=false;
  
  if(human_turn){
     if(r.player_hand.isEmpty()== false){
     System.out.println("");
     System.out.println("Card values in your hand are:");
     r.printHand(r.player_hand);
     

     System.out.print("You have " + player_book.size() + " book(s): ");
     for (String rank : player_book) { System.out.print(rank + " "); }
     System.out.println("");
     
    //display computer information; Player's(human) turn
     System.out.print("The computer has " + computer_book.size() + " book(s): ");
     for (String rank : computer_book) { System.out.print(rank + " "); }
     System.out.println("");
     
     System.out.println("Computer Cards: " + r.getHandSize(r.computer_hand));
     
     //ask human player to enter a valid rank 
	 System.out.printf("\n------------------------------------------\n");
     System.out.println("Player's(human) turn:");
     String rank = getValidRank(r, r.player_hand);
     
     //to exit game 
     if(rank.equals("exit")){
	break;
     }	
     System.out.println("\n \"Hey! Computer do you have a card of value: " + rank + "?\"");
     
     //computer checks if it has that rank or not
     //if computer doesn't have that rank it says GO FISH (except lie counter possibility)
     //if not devious mode: possible_lie_counter should stay stuck on 1 (and never be divisible by 3)
     if (r.hasCardBySymbol(rank,r.computer_hand) && mode.equals(DEVIOUS_MODE)) {
         possible_lie_counter++;
     }
     
     
     if (!r.hasCardBySymbol(rank,r.computer_hand) || possible_lie_counter % 3 == 0) {
      player_recieved.add(0);
      System.out.println("Computer says \"Go Fish\"");
      
      //player will draw card from the original deck
      //check if the deck has cards or not
      if (r.deck.isEmpty() == false) {
       //pull first card in deck   
          int player_random = (int) (Math.random() * r.deck.keySet().size());
          String key = r.deck.keySet().iterator().next();
          for (int i = 0; i < player_random; i++) {
       	   key = r.deck.keySet().iterator().next();
          }
       //transfer card from deck to player's hand(card deletion from deck upon transfer handled in trasferCard()) 
       System.out.println("You draw card of rank: " + key);
       r.transferCards(key, r.deck, r.player_hand);
       System.out.println(r.cardsRemainCount(r.deck) + " card(s) remaining in deck.");
      }
     }
     //if computer DOES have that rank:
     //remove from computers hand -> put into players hand
     else {
    	 
    	 System.out.println("Computer gives you " + r.getCardCountBySymbol(rank,r.computer_hand) + " cards of rank " + rank);
    System.out.println();
         player_recieved.add(r.getCardCountBySymbol(rank,r.computer_hand));
    	 for (int i = r.getCardCountBySymbol(rank,r.computer_hand); i > 0; i--) 
       	  	r.transferCards(rank, r.computer_hand, r.player_hand);
     }
     human_turn=false;
     
     //check for human books (only time it is possible to add a new one):
     //check player books
     for (String value : findBooks(r,r.player_hand)) {
   	  System.out.println("You placed a book of rank " + value);
   	  player_book.add(value);
     }
    }
     
     else 
         break;
  }
 
  //computer turn
  else{
   if(r.computer_hand.isEmpty()== false){
     System.out.printf("\n------------------------------------------\n");
     System.out.println("Computer's turn");
     String compRank;
     if (mode.equals(SMART_MODE) && r.hasCardBySymbol(last_new_symbol_drawn, r.computer_hand)) {
    	 compRank = last_new_symbol_drawn;
  	     System.out.println("last symbol " + compRank);

     }
     else {
         compRank = r.getRandomCardSymbol(r.computer_hand);

     }
     System.out.println("Computer says \"Do you have card of rank " + compRank + "?\"");
     
    //if player doesn't have the card; GO FISH
     if (r.hasCardBySymbol(compRank, r.player_hand) == false) {
     computer_recieved.add(0);
     System.out.println("You say \"Go Fish\"");

     //check the deck; Not empty
      if (!r.deck.isEmpty()) {
       //pull out first card from deck   
       int comp_random = (int) (Math.random() * r.deck.keySet().size());
       String key = r.deck.keySet().iterator().next();
       for (int i = 0; i < comp_random; i++) {
    	   key = r.deck.keySet().iterator().next();
       }
       
       //smart mode strategy: keeps track of last new card drawn (that wasnt previously in hand)
       if (mode.equals(SMART_MODE) && r.hasCardBySymbol(key,r.computer_hand)) {
    	   last_new_symbol_drawn = key;
       }
       
       r.transferCards(key, r.deck, r.computer_hand);
       System.out.println(r.cardsRemainCount(r.deck) + " card(s) remaining in deck.");
      }

      //if deck is empty
      if (r.deck.isEmpty())
            break;
     }
    
    //player gives computer all cards of the asked rank
    else {
      System.out.println("You give "+ r.getCardCountBySymbol(compRank,r.player_hand) +" cards of rank "+compRank);
      computer_recieved.add(r.getCardCountBySymbol(compRank,r.player_hand));
      for (int i = r.getCardCountBySymbol(compRank,r.player_hand); i > 0; i--) 
    	  r.transferCards(compRank, r.player_hand, r.computer_hand);
     }
     
     human_turn=true;
     //check for computer books (only time it is possible for them to add a new one)
     //check computer books
     for (String value : findBooks(r,r.computer_hand)) {
   	  System.out.println("The computer placed a book of rank " + value);
   	  computer_book.add(value);
     }
  }
  } 
  }
  
	System.out.printf("\n------------------------------------------\n");
	System.out.println("Your books: " + player_book.size());
	System.out.println("Computer books: " + computer_book.size()); 
  
  //output winner
  System.out.println("Final Scores are: ");
  if (player_book.size() > computer_book.size())
	  System.out.println("....................You win!...................");
  else if (player_book.size() == computer_book.size())
	  System.out.println(".............It's a tie!....................");
  else
	  System.out.println("..................Computer wins!...................");
  
  System.out.println("\n***************Statistics*********************");
  
  //total handed over calculate:
  double player_recieved_total = 0;
  for (int amt : player_recieved) { player_recieved_total += amt; }
  
  double computer_recieved_total = 0;
  for (int amt : computer_recieved) { computer_recieved_total += amt; }
  
  System.out.println(player_recieved.size() + " turns");
  //player
  System.out.println("Player recieved " + player_recieved_total + " cards total");
  System.out.println("Average of " + String.format( "%.2f", (player_recieved_total/ (double) player_recieved.size()) ) + " cards recieved each turn");
  //computer
  System.out.println("Computer recieved " + computer_recieved_total + " cards total");
  System.out.println("Average of " + String.format( "%.2f", (computer_recieved_total/ (double) computer_recieved.size()) ) + " cards recieved each turn");
  if (mode == DEVIOUS_MODE) { 
  System.out.println("Computer lied a total of " + (possible_lie_counter/3) + " times");
  }
  
  System.out.println("");
  
  //chart
  if (player_recieved.size()  < 5) {
	  System.out.println("Chart not shown (minimum of 5 rounds necessary)");
  }
  else {
	  int diff = (player_recieved.size() + (5 - (player_recieved.size() % 5)))/5;

	  int[] player_chart_values = new int[5];	  
	  int index1 = 0;
	  for (int i = 0; i < player_recieved.size(); i++) {
		  player_chart_values[index1] += player_recieved.get(i);
		  if (i % diff == 0 && i != 0) { index1++; }
	  }
	  
	  int[] comp_chart_values = new int[5];	  
	  int index2 = 0;
	  for (int i = 0; i < computer_recieved.size(); i++) {
		  comp_chart_values[index2] += computer_recieved.get(i);
		  if (i % diff == 0 && i != 0) { index2++; }
	  }
	  
	  //print chart
	
	  System.out.println("Player Chart:");
	  for (int i = 0; i < player_chart_values.length; i++) {
		  System.out.print(i+1 + " ");
		  for (int j = 0; j < player_chart_values[i]; j++) {
			  System.out.print("x");
		  }
		  System.out.println("");

	  }
	  System.out.println("Computer Chart:");
	  for (int i = 0; i < comp_chart_values.length; i++) {
		  System.out.print(i+1 + " ");
		  for (int j = 0; j < comp_chart_values[i]; j++) {
			  System.out.print("x");
		  }
		  System.out.println("");
	  }

  }
  
  
 }
 

}
  
 
  