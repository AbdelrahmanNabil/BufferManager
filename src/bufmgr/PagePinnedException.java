package bufmgr;

import chainexception.ChainException;
// Throw a PagePinnedException when try to free a page that is still pinned.

public class PagePinnedException extends ChainException{
	public PagePinnedException(Exception e, String name) {
		super(e, name);
		// TODO Auto-generated constructor stub
	}

}
