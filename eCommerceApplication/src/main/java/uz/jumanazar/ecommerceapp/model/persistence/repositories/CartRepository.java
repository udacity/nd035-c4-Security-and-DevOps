package uz.jumanazar.ecommerceapp.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.jumanazar.ecommerceapp.model.persistence.Cart;
import uz.jumanazar.ecommerceapp.model.persistence.User;

import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
