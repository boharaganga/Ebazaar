package application;

public interface CleanupControl {
	/**
	 * Implementers of CleanupControl must implement
	 * the cleanUp method, which ensures that all
	 * windows owned by the implementing class are
	 * disposed when cleanUp is called
	 */
	public void cleanUp();

}
