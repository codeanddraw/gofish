import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * @author AlexBeard, NishaChaube, SimonAnguish
 * 
 * This has all of the deck/hand methods, including: 
 * -transferring between hands
 * -initialization
 * -checking if hand has any of a symbol
 * transferring all cards of a certain symbol 
 * etc
 * ------------------------------------------
 * Work Distribution:
 * 
 * This class was initially written by Simon and Nisha, with Alex doing general bug fixing 
 * (mainly to clearCardsBySymbol and getCardCountBySymbol) 
 * and several methods added (getHandSize and printHand)
 */
public class Run {
    public static HashMap<String, Integer> deck = new HashMap<String, Integer>();
    public static HashMap<String, Integer> player_hand = new HashMap<String, Integer>();
    public static HashMap<String, Integer> computer_hand = new HashMap<String, Integer>();
     
    /**
		initializeDeck
		Creates a full "deck", interpreted as a HashMap of "A"-"K" with value:4
 	*/

 	static void initializeDeck() {
    		for (int i=1;i<14;i++) {
 			deck.put(determineSymbolFromInt(i), 4);
 		}
 	}

 	/**
 		shuffleDeckToHands
		Shuffles the deck into the player and computers hands.
		Removes the card from the deck once it is shuffled.
 	*/

 	static void shuffleDeckToHands() {
 	    String card_symbol;
 	    for (int i=0;i<7;i++) {
            card_symbol = getRandomCardSymbol(deck);
            reduceCardBySymbol(card_symbol, deck);
            dealCardBySymbol(card_symbol, player_hand);
                        
			
	    card_symbol = getRandomCardSymbol(deck);
 	    reduceCardBySymbol(card_symbol, deck);
	    dealCardBySymbol(card_symbol, computer_hand);
 	    }
 	}

        
    /**
		transferCards
		Moves the cards from the first hand to the second hand
		@param	card_symbol	A string interpretation of a card
		@param	out_hand	the hand to take the cards from
		@param	in_hand		the hand to put the cards into
 	*/
        
	public static void transferCards(String card_symbol, HashMap<String, Integer> out_hand, HashMap<String, Integer> in_hand) {
		reduceCardBySymbol(card_symbol, out_hand);
        	addCardsToHandBySymbol(card_symbol, in_hand);
	}
	
	/**
	 * gets hand size - used to get computers hand size
	 * Method was needed because keyList ignores multiple cards of same rank
	 * @param hand
	 * @return size of hand
	 */
	public static int getHandSize(HashMap<String, Integer> hand) {
		int size = 0;
		for (String value : hand.keySet()) {
			size += hand.get(value);
		}
		return size;
	}
	
	/**
	   Prints cards from a specified hand 
	   Method was needed because keyList ignores multiple cards of same rank
	   @param hand
	*/
	public static void printHand(HashMap<String, Integer> hand) {	
		//horizontal print
		for (String value : hand.keySet()) {
   	 		for (int i = 0; i < hand.get(value); i++) {
   		 		System.out.print(value + " ");
   	 		}
   	 }
	 System.out.println("");

	}

 	/**
		getCardCountBySymbol
		@param	card_symbol	A string interpretation of a card
		@param	hand 		the specific hand to add the cards to
		@return				the int count of the specified card symbol
 	*/

	public static int getCardCountBySymbol(String card_symbol, HashMap<String, Integer> hand) {
		return hand.get(card_symbol);
	}

 	/**
		addCardsToHandBySymbol
		@param	card_symbol	A string interpretation of a card
		@param	card_count 	the amount of a certain card to add to the hand
		@param	hand 		the specfic hand to add the cards to
 	*/

 	public static void addCardsToHandBySymbol(String card_symbol, HashMap<String, Integer> hand) {
 		int current_card_count;
 		
 		if (hand.get(card_symbol) == null) {
 			current_card_count = 0;
 		}
 		else {
 	 		current_card_count = hand.get(card_symbol);
 		}
 		hand.put(card_symbol, current_card_count + 1);
 	}

 	/**
		hasCardBySymbol
		Sees if a hand has a card by the symbol
		@param	card_symbol	A string interpretation of a card
		@param	hand 		The specific hand to check
		@return 			Boolean if the card is in the hand
 	*/

 	public static boolean hasCardBySymbol(String card_symbol, HashMap<String, Integer> hand) {
                return hand.containsKey(card_symbol);
 	}
        
        
   /**
        cardsRemainCount
        calculates the total cards left
        @param hand         Specific hashmap to check
        @return             total cards remaining

*/
    public static int cardsRemainCount(HashMap<String, Integer> hand) {
            int sum = 0;
            for (float value : hand.values()) {
                sum += value;
            }
            return sum;
}
        

 	/**
		clearCardsBySymbol
		Removes all cards from a specified hand of a specified symbol
		@param	card_symbol	a string interpretation of a card type
		@param	hand 		a hand to remove the cards from
 	*/

 	public static void clearCardsBySymbol(String card_symbol, HashMap<String, Integer> hand) {
 		hand.put(card_symbol, 0);
                hand.remove(card_symbol);
 	}

 	
 	/**
		getRandomCardSymbol
		generates a random string interpretation of a card
		@return 	a random card symbol in String form
 	*/

 	public static String getRandomCardSymbol(HashMap<String, Integer> hand) {
 		List<String> keyList = new ArrayList<String>(hand.keySet());
                int randomIndex = new Random().nextInt(keyList.size());                
                String randomKey = keyList.get(randomIndex);
                return randomKey;
 	}
               
   

 	/**
 		reduceCardBySymbol
		Reduces the count of the specified card in the deck by one.
		Essentially the first half of dealing a card, before it is places in a hand.
		@param 	card_symbol	A string interpretation of a card
 	*/

 	private static void reduceCardBySymbol(String card_symbol, HashMap<String, Integer> remove_from) {
 		int card_count = remove_from.get(card_symbol);
 		remove_from.put(card_symbol, card_count-1);
 		if (remove_from.get(card_symbol) == 0) {
                    remove_from.remove(card_symbol);
               }
 	}

 	/**
		dealCardBySymbol
		The second half of dealing a card.
		Increases the count of a specified symbol in a specified hand by 1
		@param	card_symbol	a string interpretation of a card value
		@param	hand 		the hand to increase the value of the card in
 	*/

 	private static void dealCardBySymbol(String card_symbol, HashMap<String, Integer> hand) {
 		if(hand.containsKey(card_symbol)==true)
                {
                 int card_count = hand.get(card_symbol);
 		 hand.put(card_symbol, card_count+1);
                }
                else
                    hand.put(card_symbol, 1);
 	}

 	/**
 		determineSymbolFromInt
 		Determines the symbol for the given card value.
		@param	card_value	a set value within a certain range (1-13)
		@return				The string notation of the integer value
 	*/
 	public static String determineSymbolFromInt(int card_value) {
 		String card_symbol;

 		switch (card_value) {
			case 1:		card_symbol = "A";
						break;
			case 11:	card_symbol = "J";
						break;
			case 12:	card_symbol = "Q";
						break;
			case 13:	card_symbol = "K";
						break;
			default:	card_symbol = Integer.toString(card_value);
						break;
		}

		return card_symbol;
 	}
        

}
    
 