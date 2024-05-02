package typingTutor;

import java.util.concurrent.atomic.AtomicBoolean;

//Thread to monitor the word that has been typed.
public class CatchWord extends Thread {
	String target;
	static AtomicBoolean done; // REMOVE
	static AtomicBoolean pause; // REMOVE

	private static FallingWord[] words; // list of words
	
	private static int noWords; // how many
	private static Score score; // user score

	CatchWord(String typedWord) {
		target = typedWord;
	}

	public static void setWords(FallingWord[] wordList) {
		
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

		
		FallingWord temp;
		int i = 0;
		int num = 0;
		while (i < noWords) {
			
			int tempy = 0;
			while (pause.get()) {
			}
			;

			for (int p = 0; p < noWords; p++) {
				
				if (words[p].getWord().equals(target)) {
					
					if (words[p].getY() > tempy) {
						tempy = words[p].getY();
						temp = words[p];
						num = p;
					}
				}

			}

			if (words[num].matchWord(target)) {
				
				System.out.println(" score! '" + target); // for checking
				score.caughtWord(target.length());
				break;

			}
			
		}i++;
		
	}

}
