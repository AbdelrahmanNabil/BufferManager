package bufmgr;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import chainexception.ChainException;

import global.PageId;
import diskmgr.Page;

public class BufMgr2 {
	private Page [] bufPool;
	private Descriptor[] des;
	private Queue<Integer> queue;
	private Hashtable<Integer, Integer> hashTable;
	private int counter;
	
/**
* Create the BufMgr object.
* Allocate pages (frames) for the buffer pool in main memory and
* make the buffer manage aware that the replacement policy is
* specified by replacerArg (i.e. Clock, LRU, MRU etc.).
1
*
* @param numbufs number of buffers in the buffer pool.
* @param replacerArg name of the buffer replacement policy.
*/
public BufMgr2(int numbufs, String replacerArg) {
	bufPool =new Page[numbufs];
	des=new Descriptor[numbufs];
	hashTable =new Hashtable<Integer, Integer>();
	queue =new LinkedList<Integer>();
}

/**
* Pin a page.
* First check if this page is already in the buffer pool.
* If it is, increment the pin_count and return a pointer to this
* page. If the pin_count was 0 before the call, the page was a
* replacement candidate, but is no longer a candidate.
* If the page is not in the pool, choose a frame (from the
* set of replacement candidates) to hold this page, read the
* page (using the appropriate method from {\em diskmgr} package) and pin it.
* Also, must write out the old page in chosen frame if it is dirty
* before reading new page. (You can assume that emptyPage==false for
* this assignment.)
*
* @param Page_Id_in_a_DB page number in the minibase.
* @param page the pointer poit to the page.
* @param emptyPage true (empty page); false (non-empty page)
*/
public void pinPage(PageId pin_pgid, Page page, boolean emptyPage) {}
/**
* Unpin a page specified by a pageId.
* This method should be called with dirty==true if the client has
* modified the page. If so, this call should set the dirty bit
* for this frame. Further, if pin_count>0, this method should
* decrement it. If pin_count=0 before this call, throw an exception
* to report error. (For testing purposes, we ask you to throw
2
* an exception named PageUnpinnedException in case of error.)
*
* @param globalPageId_in_a_DB page number in the minibase.
* @param dirty the dirty bit of the frame
*/
public void unpinPage(PageId PageId_in_a_DB, boolean dirty) throws ChainException{
	Integer i = hashTable.get((Integer)PageId_in_a_DB.pid);
	if (i ==null){
		throw new PageUnpinnedException(null, "PageUnpinnedException");
	}
	if (dirty){
		
	}
	
}
/**
* Allocate new pages.
* Call DB object to allocate a run of new pages and
* find a frame in the buffer pool for the first page
* and pin it. (This call allows a client of the Buffer Manager
* to allocate pages on disk.) If buffer is full, i.e., you
* can’t find a frame for the first page, ask DB to deallocate
* all these pages, and return null.
*
* @param firstpage the address of the first page.
* @param howmany total number of allocated new pages.
*
* @return the first page id of the new pages. null, if error.
*/
public PageId newPage(Page firstpage, int howmany) {
	
	return null;
};
/**
* This method should be called to delete a page that is on disk.
* This routine must call the method in diskmgr package to
* deallocate the page.
*
* @param globalPageId the page number in the data base.
*/
public void freePage(PageId globalPageId) {};

/**
* Used to flush a particular page of the buffer pool to disk.
* This method calls the write_page method of the diskmgr package.
*
* @param pageid the page number in the database.
*/
public void flushPage(PageId pageid) {};
};