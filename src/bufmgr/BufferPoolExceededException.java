package bufmgr;

import chainexception.ChainException;


// Throw a BufferPoolExceededException when try to pin a page to the buffer pool with no unpinned
// frame left.
public class BufferPoolExceededException extends ChainException{
	public BufferPoolExceededException(Exception e, String i) {
		super(e, i);
		// TODO Auto-generated constructor stub
	}
}
