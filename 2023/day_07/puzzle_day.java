import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	public final static List<String> cardType = Arrays.asList("A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2");
	public final static List<String> cardTypeWithJoker = Arrays.asList("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J");
	public final static List<String> handTypes = Arrays.asList("Five", "Foor", "Full-house", "Three", "Two-pair", "Pair", "High");

	public static class Hands {
		public String cards;
		public String cardsJoker;
		public int winning;
		public String type;
		public int rank;

		public Hands(String cards, String winning) {
			this.cards = cards;
			this.cardsJoker = cards;
			this.winning = Integer.valueOf(winning);
			this.type = "";
			this.rank = 0;
		}
	}

    public static void main(String[] args) {
		int res_1 = solvePuzzle1();
		System.out.println("Result for puzzle 1 = " + res_1);

		int res_2 = solvePuzzle2();
		System.out.println("Result for puzzle 2 = " + res_2);
    }

	// Transform file into ArrayList
	private static ArrayList<Hands> fileToArrayListOfHands(String fileName) {
		BufferedReader reader;

		ArrayList<Hands> lines = new ArrayList<Hands>();
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();

			while (line != null) {
				Hands h = new Hands(line.split(" ")[0], line.split(" ")[1]);
				h.type = findTypeHand(h.cards);
				lines.add(h);
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

	// Find the type of hand
	private static String findTypeHand(String cards) {
		String typeFind = "";
		ArrayList<Integer> nbType = new ArrayList<Integer>();
		
		// Count cards
		for (char c : cards.toCharArray()) {
			if (typeFind.contains(c + "")) {
				int cIndex = typeFind.indexOf(c);
				nbType.set(cIndex, nbType.get(cIndex)+1);
			}
			else {
				typeFind += c + "";
				nbType.add(1);
			}
		}
		Collections.sort(nbType, Collections.reverseOrder());

		if (nbType.get(0) == 5) {
			return handTypes.get(0);
		} 
		else if (nbType.get(0) == 4) {
			return handTypes.get(1);
		} 
		else if (nbType.get(0) == 3) {
			if (nbType.get(1) == 2) {
				return handTypes.get(2);
			}
			else {
				return handTypes.get(3);
			}
		} 
		else if (nbType.get(0) == 2) {
			if (nbType.get(1) == 2) {
				return handTypes.get(4);
			}
			else {
				return handTypes.get(5);
			}
		} 
		else {
			return handTypes.get(6);
		}
	}

	/********************/
	/** The first part **/
	/********************/

	private static int solvePuzzle1() {
		int res = 0;
		ArrayList<Hands> reader = fileToArrayListOfHands("puzzle_data.txt");

		List<Hands> sorted = reader.stream()
			.sorted((Hands h1, Hands h2) -> {
				int compare = Integer.compare(handTypes.indexOf(h2.type), handTypes.indexOf(h1.type));
				
				int cardIndex = 0;
				while (compare == 0 && cardIndex < 5) {
					int h1Card = cardType.indexOf(h1.cards.charAt(cardIndex) + "");
					int h2Card = cardType.indexOf(h2.cards.charAt(cardIndex) + "");
					compare = Integer.compare(h2Card, h1Card);
					cardIndex++;
				}

				return compare;
			})
			.collect(Collectors.toList());

		for (Hands h : sorted) {
			h.rank = sorted.indexOf(h) + 1;
			res += h.rank * h.winning;
		}

		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static int solvePuzzle2() {
 		int res = 0;
		ArrayList<Hands> reader = fileToArrayListOfHands("puzzle_data.txt");

		for (Hands h : reader) {
			jokerTreatment(h);
		}

		List<Hands> sorted = reader.stream()
			.sorted((Hands h1, Hands h2) -> {
				int compare = Integer.compare(handTypes.indexOf(h2.type), handTypes.indexOf(h1.type));
				
				int cardIndex = 0;
				while (compare == 0 && cardIndex < 5) {
					int h1Card = cardTypeWithJoker.indexOf(h1.cards.charAt(cardIndex) + "");
					int h2Card = cardTypeWithJoker.indexOf(h2.cards.charAt(cardIndex) + "");
					compare = Integer.compare(h2Card, h1Card);
					cardIndex++;
				}

				return compare;
			})
			.collect(Collectors.toList());

		for (Hands h : sorted) {
			h.rank = sorted.indexOf(h) + 1;
			res += h.rank * h.winning;
		}

		return res;
   	}

	private static void jokerTreatment(Hands h) {
		String cards = h.cards;

		String typeFind = "";
		ArrayList<Integer> nbType = new ArrayList<Integer>();
		
		// Count cards
		for (char c : cards.toCharArray()) {
			if (typeFind.contains(c + "")) {
				int cIndex = typeFind.indexOf(c);
				nbType.set(cIndex, nbType.get(cIndex)+1);
			}
			else {
				typeFind += c + "";
				nbType.add(1);
			}
		}

		int jokerIndex = typeFind.indexOf("J");
		if (jokerIndex >= 0) {
			if (nbType.get(jokerIndex) == 5) {
				h.cardsJoker = h.cardsJoker.replaceAll("J", cardType.get(0));
			} else {
				typeFind = typeFind.replace("J", "");
				nbType.remove(jokerIndex);
				List<Integer> sortedNbType = nbType.stream()
					.sorted(Comparator.reverseOrder())
					.collect(Collectors.toList());
				
				String replaceCard = "_";
				if (sortedNbType.size() == 1 || sortedNbType.get(0) > sortedNbType.get(1)) {
					int maxIndex = nbType.indexOf(sortedNbType.get(0));
					replaceCard = typeFind.charAt(maxIndex) + "";
				} else {
					replaceCard = typeFind.charAt(nbType.indexOf(sortedNbType.get(0))) + "";
					for (int i=0; i<nbType.size(); i++) {
						if (sortedNbType.get(0) == nbType.get(i)) {
							String iCard = typeFind.charAt(i) + "";
							if (cardType.indexOf(iCard) < cardType.indexOf(replaceCard)) {
								replaceCard = iCard;
							}
						}
					}
				}
				h.cardsJoker = h.cardsJoker.replaceAll("J", replaceCard);
			}
		}

		h.type = findTypeHand(h.cardsJoker);
   	}
}