
/*
 * 
 * @author : Mohammad Omar Shahid
 * 
 */

public class node {
	private String fileName;
	private int counter;
	private String letter;
	private float probality;

	public node(String fn, int cntr, String ltr) {
		this.fileName = fn;
		this.counter = cntr;
		this.letter = ltr;
		this.probality = 0;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public float getProbality() {
		return probality;
	}

	public void setProbality(float probality) {
		this.probality = probality;
	}

}
