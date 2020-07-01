package Coursework;

import java.util.Comparator;

public class stockCompare implements Comparator<Admin>{

	@Override
	public int compare(Admin o1, Admin o2) {
		// TODO Auto-generated method stub
		return o2.retStock() - o1.retStock(); //compares the stock quantity of 2 items
	}
 
}

