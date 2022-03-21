package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class ListWrapper {

	private List<RawStrategy> list1 = new ArrayList<>();
	private List<RawStrategy> list2 = new ArrayList<>();
	public List<RawStrategy> getList1() {
		return list1;
	}
	public void setList1(List<RawStrategy> list1) {
		this.list1 = list1;
	}
	public List<RawStrategy> getList2() {
		return list2;
	}
	public void setList2(List<RawStrategy> list2) {
		this.list2 = list2;
	}


	
}
