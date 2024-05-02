package typingTutor;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HungryWordMover extends Thread {
	private HungryWord myWord;
	private AtomicBoolean done;
	private AtomicBoolean pause; 
	private Score score;
	CountDownLatch HungrystartLatch; //so all can start at once
	
	HungryWordMover( HungryWord word) {
		myWord = word;
	}
	
	HungryWordMover( HungryWord word,WordDictionary dict, Score score,
			CountDownLatch HungrystartLatch, AtomicBoolean d, AtomicBoolean p) {
		this(word);
		this.HungrystartLatch = HungrystartLatch;
		this.score=score;
		this.done=d;
		this.pause=p;
	}
	
	
	
	public void run() {

		

		
		try {
			
            Thread.sleep((int)Math.random()*800);// to make hungry word appear during game
			//System.out.println(myWord.getWord() + " waiting to start hungry word " );
			HungrystartLatch.await();
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //wait for other threads to start
		//System.out.println(myWord.getWord() + " started hungry word" );
		while (!done.get()) {				
			//animate the word
			while (!myWord.dropped() && !done.get()) {
				    myWord.drop(10);
					//erase falling word at intersection
					for (int i = 0;i<TypingTutorApp.noWords;i++){

						if (myWord.getY() >= TypingTutorApp.words[i].getX() - 3 && myWord.getY() <= TypingTutorApp.words[i].getX() + 3){
							
							if (myWord.getX() >= TypingTutorApp.words[i].getY() -1 || myWord.getX() <= TypingTutorApp.words[i].getY() +1){

							TypingTutorApp.words[i].resetWord();
							TypingTutorApp.score.missedWord();
						}
					} }



					try {
						sleep(myWord.getSpeed());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};		
					while(pause.get()&&!done.get()) {};
			}
			if (!done.get() && myWord.dropped()) {
				score.missedWord();
				myWord.resetWord();
			}
			myWord.resetWord();
		}
	}
	
}
