package com.example.demo.model.requests;

public class ModifyCartRequest {
	
	private Long cartId;
	private Long itemId;
	
	public ModifyCartRequest(Long cartId, Long itemId) {
		this.cartId = cartId;
		this.itemId = itemId;
	}
	
	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
}
