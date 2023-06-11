//Responsible for tracking the number of cycles that have elapsed over time.
public class Clock {
	
	//The number of milliseconds that make up one cycle
	private float millisPerCycle;
	
	//The last time that the clock was updated (used for calculating the delta time)
	private long lastUpdate;
	
	//The number of cycles that have elapsed
	private int elapsedCycles;
	
	//The amount of excess time towards the next elapsed cycle
	private float excessCycles;
	
	//Checks whether the clock is paused
	private boolean isPaused;
	
	//Creates a new clock and sets it's cycles-per-second
	public Clock(float cyclesPerSecond) {
		setCyclesPerSecond(cyclesPerSecond);
		reset();
	}
	
	//Sets the number of cycles that elapse per second.
	public void setCyclesPerSecond(float cyclesPerSecond) {
		this.millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
	}
	
	//Resets the clock stats. Elapsed cycles and cycle excess will be reset to 0.
	//The last update time will be reset to the current time, and the paused flag will be set to false.
	public void reset() {
		this.elapsedCycles = 0;
		this.excessCycles = 0.0f;
		this.lastUpdate = getCurrentTime();
		this.isPaused = false;
	}
	
	//Updates the clock stats
	//The number of elapsed cycles, as well as the cycle excess will be calculated only if the clock is not paused
	public void update() {
		//Get the current time and calculate the delta time.
		long currUpdate = getCurrentTime();
		float delta = (float)(currUpdate - lastUpdate) + excessCycles;
		
		//Update the number of elapsed and excess ticks if not paused.
		if(!isPaused) {
			this.elapsedCycles += (int)Math.floor(delta / millisPerCycle);
			this.excessCycles = delta % millisPerCycle;
		}
		
		//Sets the last update time for the next update cycle.
		this.lastUpdate = currUpdate;
	}

	//Pauses or unpauses the clock
	//While paused, a clock will not update elapsed cycles or cycle excess
	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}
	
	//Checks to see if the clock is currently pause
	public boolean isPaused() {
		return isPaused;
	}
	
	//Checks to see if a cycle has elapsed for the clock.
	//If it has,the number of elapsed cycles will be decremented by one.
	public boolean hasElapsedCycle() {
		if(elapsedCycles > 0) {
			this.elapsedCycles--;
			return true;
		}
		return false;
	}

	 //Checks to see if a cycle has elapsed for this clock yet.
	 //The number of cycles will not be decremented if the number of elapsed cycles is greater than 0.
	public boolean peekElapsedCycle() {
		return (elapsedCycles > 0);
	}
	
	//Calculates the current time in milliseconds using the computer's high resolution clock
	private static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L);
	}

}