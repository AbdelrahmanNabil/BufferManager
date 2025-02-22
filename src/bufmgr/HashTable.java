
package bufmgr;

public class HashTable  {
	private int leangth;
	private Node [] array;
	
	public HashTable() {
		leangth =43;
		array =new Node[leangth];
	}
	private int hashFunction(int pageNumber){
		return  (13* pageNumber +29)%leangth;	
	}
	
	public void add(int pageNumber ,int frameNum){
		int h =hashFunction(pageNumber);
		if (array[h] != null){
			Node pointer =array[h];
			
			while (pointer != null){
				if (pointer.getKey() ==pageNumber){
					pointer.setValue(frameNum);
					return ;
				}
				pointer =pointer.getNext();
			}
			
			pointer.setNext(new Node(frameNum, pageNumber));
			return ;
		}
		
		if (array[h] ==null){
			array[h] =new Node(frameNum, pageNumber);
			return ;
		}
		
	}
	
	public int getFrameNumber(int pageNumber){
		int v =hashFunction(pageNumber);
		if (array[v]==null){
			return -1;
		}
		Node pointer =array[v];
		while (pointer !=null){
			if (pointer.getKey() ==pageNumber){
				return pointer.getValue();
			}
			pointer =pointer.getNext();
		}
		return -1;
	}
	
	public int delete (int key){
		int h= hashFunction(key);
		
		Node pointer =array[h];
		if (pointer != null){
			if (pointer.getKey() ==key&& pointer.getNext() ==null){
				int v= pointer.getValue();
				array[h] =null;
				pointer =null;
				return v;
			}
			while (pointer.getNext() !=null){
				if (pointer.getNext().getKey()==key){
					Node p=pointer.getNext().getNext();
					
					pointer.getNext().setNext(null);
					int v =pointer.getNext().getValue();
					pointer.setNext(p);
					return v;
				}
			}
			return 0;
		
		}
		else {
			return 0;
		}
		
		
		
		
	}
	
	public void print(){
		for (int i=0 ; i<leangth ;i++){
			if (array[i] != null){
//				System.out.print(array[i].getValue());
				Node pointer =array[i].getNext();
				while (pointer != null){
//					System.out.print(" -> "+pointer.getValue());
					pointer =pointer.getNext();
				}
//				System.out.println("");
			}
			
		}
	}
	
//	public static void main(String[] args) {
//		HashTable k =new HashTable();
//		k.add(4, 700);
//		k.add(4, 800);
//		k.add(8, 400);
//		k.add(20, 500);
//		k.add(40, 780);
////		k.add(10, 700);
////		k.add(10, 700);
////		k.add(10, 700);
////		k.add(10, 700);
////		k.add(20, 100);
////		k.add(10, 800);
//		
//		k.delete(10);
//		k.delete(4);
//		k.delete(10);
////		k.delete(10);
////		k.delete(10);
////		k.delete(10);
////		k.delete(10);
////		k.delete(20);
//		
//
//		
//		
//		k.add(20, 1000);
////		k.delete(20);
//		k.print();
//	}
//	
//	
	
	private class Node {
		private Node next;
		private int value;
		private int key;
		
		
		public Node(int v, int k) {
			value =v;
			key= k;
			next =null;
		}
		public int getKey(){
			return key;
		}
		
		
		public Node getNext() {
			return next;
		}
		public void setNext(Node n) {
			next = n;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int v) {
			value = v;
		}
		
	}
}
