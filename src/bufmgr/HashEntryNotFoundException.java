package bufmgr;

import chainexception.ChainException;

// Throw a HashEntryNotFoundException when page specified by PageId is not found in the buffer
// pool.
public class HashEntryNotFoundException extends ChainException{
	public HashEntryNotFoundException(Exception e, String name) {
		super(e, name);
	}
}
