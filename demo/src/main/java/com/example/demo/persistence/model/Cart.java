package com.example.demo.persistence.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
	private User user;
	
	@OneToMany(mappedBy = "cart")
	private List<Item> items;
	
	@Column
	private BigDecimal total;
	
	
	public void addItem(Item item) {
		if(items == null) {
			items = new ArrayList<>();
		}
		items.add(item);
		total.add(item.getPrice());
	}
	
	public void removeItem(Item item) {
		if(items != null) {
			items.remove(item);
			total.subtract(item.getPrice());
		}
	}

	public User getUser() {
		return user;
	}

	public List<Item> getItems() {
		return items;
	}

	public BigDecimal getTotal() {
		return total;
	}
}
