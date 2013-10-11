package bufmgr;

import global.PageId;

public class Descriptor {
	private int pin_counter;
	private boolean dirty;
	private PageId pi;
	public Descriptor(int i) {
		dirty=false;
		pin_counter=0;
		pi =new PageId();
		pi.pid=i;
	}
	public int getPin_counter() {
		return pin_counter;
	}
	public void setPin_counter(int pin_counter) {
		this.pin_counter = pin_counter;
	}
	public boolean isDirty() {
		return dirty;
	}
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	public PageId getPi() {
		return pi;
	}
	
	
}
