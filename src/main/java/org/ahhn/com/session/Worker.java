package org.ahhn.com.session;

/**
 * Created by XJX on 2016/3/8.
 */
public class Worker {
	private Integer id;
	public String name;
	private Pay pay;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Pay getPay() {
		return pay;
	}

	public void setPay(Pay pay) {
		this.pay = pay;
	}
}
