package typingTutor;

import java.util.concurrent.atomic.AtomicBoolean;

//Thread to monitor the word that has been typed.
public class CatchHungryWord extends Thread {
	String target;
	static AtomicBoolean done; // REMOVE
	static AtomicBoolean pause; // REMOVE

	private static HungryWord[] words; // list of words
	
	private static int noWords; // how many
	private static Score score; // user score

	CatchHungryWord(String typedWord) {
		target = typedWord;
	}

	public static void setWords(HungryWord[] wordList) {
		
		words = wordList;
		noWords = words.length;
	}

	public static void setScore(Score sharedScore) {
		score = sharedScore;
	}

	public static void setFlags(AtomicBoolean d, AtomicBoolean p) {
		done = d;
		pause = p;
	}

	public void run() {

		
		int i = 0;
		int num = 0;
		while (i < noWords) {
			
			
			while (pause.get()) {
			}
			

			if (words[num].matchWord(target)) {
				
				System.out.println(" score! '" + target); // for checking
				score.caughtWord(target.length());
				break;

			}
			
		}i++;
		
	}

}
