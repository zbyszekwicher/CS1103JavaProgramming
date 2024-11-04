import java.util.Scanner;

public class Main {
	private static Scanner stdin = new Scanner(System.in);
	
	public static void main(String[] args) {
		//Getting user input
		System.out.print("Enter text input: ");
		String userInput = stdin.nextLine().trim();
		
		//Text analysis
		int wordCount = getWordCount(userInput);
		int charCount = getCharCount(userInput);
		char mostCommonChar = getMostCommonChar(userInput.toLowerCase());
		int uniqueWords = getUniqueWords(userInput.toLowerCase());
		
		//Printing out the results
		System.out.print("\nCharacter count: ");
		System.out.print(charCount );
		
		System.out.print("\nWord count: ");
		System.out.print(wordCount);
		
		System.out.print("\nMost common character: ");
		System.out.print(mostCommonChar);
		
		System.out.print("\nNumber of unique words: ");
		System.out.print(uniqueWords);
		
		//Asking for a character
		String userChar = "";
		while(userChar.length() != 1) {
			System.out.print("\nEnter a character: ");
			userChar = stdin.nextLine().trim();
		}
		int userCharFrequency = getCharFrequency(userChar.toLowerCase(), userInput.toLowerCase());
		System.out.println(String.format("Character %s occurs %d times.", userChar, userCharFrequency));
		
		//Asking for a word
		String userWord = " ";
		while(userWord.contains(" ")) {
			System.out.print("\nEnter a word: ");
			userWord = stdin.nextLine().trim();
		}
		int userWordFrequency = getWordFrequency(userWord.toLowerCase(), userInput.toLowerCase());
		System.out.println(String.format("Word %s occurs %d times.", userWord, userWordFrequency));
	}
	
	//Finding the word frequency
	static int getWordFrequency(String word, String s) {
		int counter = 0;
		for (String w : s.split(" ")) {
			if (word.equals(w)) {
				counter++;
			}
		}
		return counter;
	}
	
	//Finding the character frequency
	static int getCharFrequency(String character, String s) {
		int counter = 0;
		for (String c : s.split("")) {
			if (character.equals(c)) {
				counter++;
			}
		}
		return counter;
	}
		
	//Calculating word count
	static int getWordCount(String s) {
		String[] wordList = s.split(" ");
		int unwantedDoubleSpacesCount = 0; 
		for (int i = 0; i < wordList.length; i++) {
			if (wordList[i].equals("")) {
				unwantedDoubleSpacesCount++;
			}
		}
		return wordList.length - unwantedDoubleSpacesCount;
	}
	
	//Calculating character count
	static int getCharCount(String s) {
		int spaceCount = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ' ') {
				spaceCount++;
			}
		}
		return s.length() - spaceCount;
	}
	
	//Finding the most common character
	static char getMostCommonChar(String s) {
		char mostCommonChar = ' ';
		int mostCommonCharCount = 0;
		String[] CharList = s.split("");
		int counter;
		
		for (int i = 0; i < CharList.length; i++) {
			if (!CharList[i].equals(" ")) {
				counter = 0;
				for (int j = i; j < CharList.length; j++) {
					if (CharList[i].equals(CharList[j])) {
						counter++;
					}
				}
				if (counter > mostCommonCharCount) {
					mostCommonChar = CharList[i].charAt(0);
					mostCommonCharCount = counter;
				}
			}
		}
		return mostCommonChar;
	}
	
	//Looking for a number of unique words in a string
	static int getUniqueWords(String s) {
		String[] wordList = s.split(" ");
		String[] uniqueWords = new String[wordList.length];
		int uniqueWordsCount = 0;
		for (int i = 0; i < wordList.length; i++) {
			boolean isUnique = true;
			for (int j = 0; j < uniqueWordsCount; j++) {
				if (wordList[i].equals(uniqueWords[j])) {
					isUnique = false;
					break;
				}
			}
			if (isUnique) {
				uniqueWords[uniqueWordsCount] = wordList[i];
				uniqueWordsCount++;
			}
		}
		return uniqueWordsCount;
	}
}