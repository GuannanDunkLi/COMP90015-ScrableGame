package wo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import wo.client.userlist;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		ArrayList a = new ArrayList();
//		a.add("a");
//		a.add("b");
//		System.out.println(a.get(0));
//		Set s = new HashSet();
//		s.add("a");
//		s.add("b");
//		s.add("a");
//		s.add("c");
//		s.add("d");
//		s.add("e");
//		System.out.println(s.size());
//		s.clear();
//		System.out.println(s.toString());
//		StringBuffer s1 = new StringBuffer();
//		s1.append("aa ");
//		s1.append("aa ");
//		s1.append("aa ");
//		System.out.println(s1);
//		Set s1 = new HashSet();
//		if (s1.isEmpty()) {
//			System.out.println(s1.iterator().next());
//			System.out.println("s1 null");
//		}
//		Iterator iterator = s.iterator();
//		while (iterator.hasNext()) {
//			s1.add(iterator.next());
//		}
//		Set s2=null;
//		if (s2 == null) {
//		System.out.println("s2 null");
//		}
//		System.out.println(s1.toString());
//		System.out.println(s1.iterator().next());
//		s1.remove("b");
//		s1.remove("a");
//		s1.remove("aaaaa");
//		s1.remove("hhh");
////		System.out.println(s1.iterator().next());
////		if (s1.isEmpty()) {
////			System.out.println("s1 null");
////		}
//		System.out.println(s1.toString());
//		System.out.println(s.toString());
		userlist u1 = userlist.getInstance();
		u1.getUserlist().add("a");
//		u1.getUserlist().add("b");
		System.out.println(u1.getUserlist().toString());
		userlist u2 = userlist.getInstance();
		if (u1.getUserlist().contains("a")) {
			System.out.println("bao cuo");
		}
//		u1.getUserlist().add("a");
////		u1.getUserlist().add("d");
//		System.out.println(u1.getUserlist().toString());
		System.out.println(u1.getUserlist().contains("mei cuo"));
	}

}
