package commascounter;

public class CommaCounter extends Thread {

	String input;
	 int count;

	public CommaCounter(String input) {
		this.input = input;
	}

	public int getCount() {
		return count;
	}

	public void count() {

		int count = (int) input.chars().filter(s -> s == ',').count();
		this.count += count;
	}

	@Override
	public void run() {
		this.count();
	}
}
