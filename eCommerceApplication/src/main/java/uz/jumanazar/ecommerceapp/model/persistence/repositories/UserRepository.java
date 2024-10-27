package uz.jumanazar.ecommerceapp.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.jumanazar.ecommerceapp.model.persistence.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
