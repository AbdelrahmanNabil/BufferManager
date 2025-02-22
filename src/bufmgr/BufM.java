package bufmgr;

import java.io.IOException;
import java.util.Deque;
import java.util.Hashtable;
import java.util.LinkedList;

import java.util.Stack;


import chainexception.ChainException;


import diskmgr.Page;
import global.*;

public class BufM {
	// private DB deskMag;
	private Page[] bufPool;
	private Hashtable<Integer, Integer> hashTable;
	private Descriptor[] directory;
	private Deque<Integer> queue;
	private int counter2;
	private int counter = 0;

	/**
	 * Create the BufMgr object. Allocate pages (frames) for the buffer pool in
	 * main memory and make the buffer manage aware that the replacement policy
	 * is specified by replacerArg (i.e. Clock, LRU, MRU etc.). 1
	 * 
	 * @param numbufs
	 *            number of buffers in the buffer pool.
	 * @param replacerArg
	 *            name of the buffer replacement policy.
	 */
	public BufM(int numbufs, String replacerArg) {
		bufPool = new Page[numbufs];

		counter2= numbufs;
		hashTable = new Hashtable<Integer, Integer>();
		directory = new Descriptor[numbufs];
		queue = new LinkedList<Integer>();

	}

	/**
	 * Pin a page. First check if this page is already in the buffer pool. If it
	 * is, increment the pin_count and return a pointer to this page. If the
	 * pin_count was 0 before the call, the page was a replacement candidate,
	 * but is no longer a candidate. If the page is not in the pool, choose a
	 * frame (from the set of replacement candidates) to hold this page, read
	 * the page (using the appropriate method from {\em diskmgr} package) and
	 * pin it. Also, must write out the old page in chosen frame if it is dirty
	 * before reading new page. (You can assume that emptyPage==false for this
	 * assignment.)
	 * 
	 * @param Page_Id_in_a_DB
	 *            page number in the minibase.
	 * @param page
	 *            the pointer poit to the page.
	 * @param emptyPage
	 *            true (empty page); false (non-empty page)
	 * @throws IOException 
	 */
	public void pinPage(PageId pin_pgid, Page page, boolean emptyPage)
			throws ChainException, Exception {
		Integer i = hashTable.get(pin_pgid.pid);
		
		if (i != null) {
			
			if (bufPool[i]==null) {
				
				throw new BufferPoolExceededException(null, "bufmgr.BufferPoolExceededException");
				
			}
			int v = directory[i].getPin_counter();
			if (v == 0) {
				deleteElement(i);
				counter2--;
			}

			directory[i].setPin_counter(v+1);
			page= bufPool[i];
		} else {
			int index = 0;
			if (counter == bufPool.length) {
				if (queue.isEmpty()) {
					
					throw new BufferPoolExceededException(null ,"bufmgr.BufferPoolExceededException");
					
				}
				index = queue.removeFirst();
				if (bufPool[index] !=null){
				
					flushPage(directory[index].getPi());

				}
			} else {
				index = counter;
				counter++;
			}
			Page temp = new Page();
			SystemDefs.JavabaseDB.read_page(pin_pgid, temp);
//			if (temp ==null){
//				System.out.println("--------------------------");
//				throw new Exception();
//			}

			bufPool[index]=temp;
			page=temp;
			System.out.println(pin_pgid.pid);

			hashTable.put(pin_pgid.pid, index);
			Descriptor des = new Descriptor(pin_pgid.pid);
			directory[index] = des;
			des.setPin_counter(1);
			counter2--;
//			pinPage(pin_pgid, page, emptyPage);

		}
		
		
	};

	public void flushAllPages() throws ChainException, IOException {
		for (int i = 0; (directory[i] != null && i < directory.length); i++) {
			flushPage(directory[i].getPi());
		}
	}

	private void deleteElement(int i) {
		Stack<Integer> temp = new Stack<Integer>();
		while (queue.size() != 0 && !queue.getFirst().equals(i) ) {
			temp.add(queue.removeFirst());	
		}
		if (queue.size() ==0){
			while (!temp.isEmpty()) {
				queue.addFirst(temp.pop());
			}
			return ;
		}
		queue.removeFirst();
		
		while (!temp.isEmpty()) {
			queue.addFirst(temp.pop());
		}

		
	}

	/**
	 * Unpin a page specified by a pageId. This method should be called with
	 * dirty==true if the client has modified the page. If so, this call should
	 * set the dirty bit for this frame. Further, if pin_count>0, this method
	 * should decrement it. If pin_count=0 before this call, throw an exception
	 * to report error. (For testing purposes, we ask you to throw 2 an
	 * exception named PageUnpinnedException in case of error.)
	 * 
	 * @param globalPageId_in_a_DB
	 *            page number in the minibase.
	 * @param dirty
	 *            the dirty bit of the frame
	 */
	public void unpinPage(PageId PageId_in_a_DB, boolean dirty)
			throws ChainException {

//		if (PageId_in_a_DB ==null){
//			PageUnpinnedException PageUnpinnedException=new PageUnpinnedException (null, "PageUnpinnedException");
//			throw(PageUnpinnedException);
//		}
		Integer i = hashTable.get(PageId_in_a_DB.pid);
		if (i == null) {
			throw new HashEntryNotFoundException(null , "bufmgr.HashEntryNotFoundException");
		}
		Descriptor p = directory[i];
		p.setDirty(dirty);
		if(p.getPin_counter()==0)
			throw new PageUnpinnedException(null, "PageUnpinnedException");
		p.setPin_counter(p.getPin_counter() - 1);
		if (p.getPin_counter() == 0) {
			queue.addLast(i);
			counter2++;
		}
	}

	/**
	 * Allocate new pages. Call DB object to allocate a run of new pages and
	 * find a frame in the buffer pool for the first page and pin it. (This call
	 * allows a client of the Buffer Manager to allocate pages on disk.) If
	 * buffer is full, i.e., you can’t find a frame for the first page, ask DB
	 * to deallocate all these pages, and return null.
	 * 
	 * @param firstpage
	 *            the address of the first page.
	 * @param howmany
	 *            total number of allocated new pages.
	 * 
	 * @return the first page id of the new pages. null, if error.
	 * @throws ChainException 
	 * @throws IOException 
	 */
	public PageId newPage(Page firstpage, int howmany) throws ChainException, Exception {
		PageId pgid = new PageId();
		Page temp = new Page();
		SystemDefs.JavabaseDB.allocate_page(pgid, howmany);
		SystemDefs.JavabaseDB.read_page(pgid, temp);
		Integer index=null;
		if (counter == bufPool.length) {
			if (queue.isEmpty()) {
				SystemDefs.JavabaseDB.deallocate_page(pgid, howmany);
				return null;
			}
			index = queue.removeFirst();
			if (bufPool[index] != null){
				flushPage(directory[index].getPi());
			}
		} else {
			index = counter;
			counter++;
		}
		firstpage=temp;
		//
		bufPool[index]=temp;
		counter2--;
		hashTable.put(pgid.pid, index);
		directory[index]=new Descriptor(pgid.pid);
		pinPage(pgid, bufPool[index], false);
		return pgid;
	}

	/**
	 * This method should be called to delete a page that is on disk. This
	 * routine must call the method in diskmgr package to deallocate the page.
	 * 
	 * @param globalPageId
	 *            the page number in the data base.
	 * @throws Exception 
	 * 
	 * 
	 */
	public void freePage(PageId globalPageId) throws Exception {
		Integer i = hashTable.get(globalPageId.pid);
		if (i == null) {
			throw new HashEntryNotFoundException(null , "bufmgr.HashEntryNotFoundException");
		}
		counter2++;
		Descriptor p = directory[i];
		int x = p.getPin_counter();
//		if (x > 1) {
//			throw new PagePinnedException(null ,"PagePinnedException");
//		}
		if (x == 0) {
			deleteElement(i);

		}
		
		queue.addFirst(i);
//		System.out.println("Q "+queue.size());
		bufPool[i] = null;

		directory[i].setPin_counter(0);
		directory[i].setDirty(false);

		hashTable.remove(globalPageId.pid);

		SystemDefs.JavabaseDB.deallocate_page(globalPageId);
		
		
	}

	/**
	 * Used to flush a particular page of the buffer pool to disk. This method
	 * calls the write_page method of the diskmgr package.
	 * 
	 * @param pageid
	 *            the page number in the database.
	 * @throws IOException 
	 */
	public void flushPage(PageId pageid) throws ChainException, IOException {
		Integer index = hashTable.get(pageid.pid);
		if (index == null) {
			throw new HashEntryNotFoundException(null , "bufmgr.HashEntryNotFoundException");
		}
		Page p = bufPool[index];
		Descriptor d = directory[index];
		if (d.isDirty()) {
			d.setDirty(false);
			SystemDefs.JavabaseDB.write_page(pageid, p);
			//		deskMag.write_page(pageid,p);
		}

	}

	public int getNumUnpinnedBuffers() {
//		int unPin=0;
//		for (int i=0 ; i <bufPool.length; i++){
//			if ((directory[i] != null && directory[i].getPin_counter() ==0)|| bufPool[i] ==null){
//				
//				unPin++;
//			}
//		}
		System.out.println(counter2 +" ++++++++++++++");
		return counter2;
	}

}