package pl.edu.pw.elka.frak1.proz.network;

/**
 * simple heartbeat timer class used to count time periods it is resetable,
 * executes some instruction if given time (delay) will pass
 */
public abstract class HeartbeatTimer {
    private boolean isRunning = false;
    private Thread timerThread;
    private Runnable timerRunnable;

    public HeartbeatTimer(long delay) {
	this.timerRunnable = new Runnable() {

	    @Override
	    public void run() {
		while (isRunning) {
		    try {
			Thread.sleep(delay);
			execute();
		    } catch (InterruptedException e) {
			System.out.println("timer interrupted");
		    }
		}
	    }
	};

	timerThread = new Thread(timerRunnable);
	timerThread.setName("HeartbeatTimerThread");
    }

    public void start() {
	isRunning = true;
	timerThread.start();
    }

    public void reset() {
	timerThread.interrupt();
    }

    public void stop() {
	isRunning = false;
	timerThread.interrupt();
    }

    public abstract void execute();
}
