import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Clock {
	private static Date date;
	private static SimpleDateFormat dFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
	private static Scanner stdin = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		//Initializing the time updating thread
		TimeUpdateThread updateThread = new TimeUpdateThread();
		updateThread.start();
		updateThread.setPriority(Thread.NORM_PRIORITY - 1);
		
		//Initializing the output thread
		TimePrintThread printThread = new TimePrintThread();
		printThread.start();
		
		//Waiting for user input to stop the program
		stdin.nextLine();
		System.exit(0);
	}
	
	/*Both setDate and getDate are static and synchronized on the Clock class.
	 *Therefore both are synchronized together.
	 */
	synchronized public static void setDate(Date d) {
		date = d;
	}
	synchronized public static Date getDate() {
		return date;
	}
	
	//Threads classes
	private static class TimeUpdateThread extends Thread {
		@Override
		public void run() {
			Date d;
			while (true) {
				d = new Date();
				setDate(d);
			}
		}
	}
	
	private static class TimePrintThread extends Thread {
		@Override
		public void run() {
			Date d;
			while (true) {
				d = getDate();
				System.out.println(dFormat.format(d));
				try {
					sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}

