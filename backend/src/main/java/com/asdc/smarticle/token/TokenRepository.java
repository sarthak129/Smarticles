package com.asdc.smarticle.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository of token entity .
 * 
 * @author Vivekkumar Patel
 * @version 1.0
 * @since 2022-02-19
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	Token  findByToken(String token);
	
}
