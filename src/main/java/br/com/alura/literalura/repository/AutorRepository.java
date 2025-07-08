package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

	@Query("""
			    SELECT a FROM Autor a
			    WHERE a.birthYear <= :fim
			      AND (a.deathYear IS NULL OR a.deathYear >= :inicio)
			""")
	List<Autor> autoresVivosEntre(@Param("inicio") Integer anoInicio, @Param("fim") Integer anoFim);


}
