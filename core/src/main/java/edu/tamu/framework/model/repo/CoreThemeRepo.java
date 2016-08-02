/* 
 * CoreThemeRepo.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.framework.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.tamu.framework.model.CoreTheme;

/**
 * Application User repository.
 * 
 * @author
 *
 */
@Repository
public interface CoreThemeRepo extends JpaRepository<CoreTheme, Long>, CoreThemeRepoCustom {
	
	public CoreTheme getByName(String name);
	
	public CoreTheme getById(Long id);
	
	public CoreTheme findByActiveTrue();

	public List<CoreTheme> findByPropertiesId(Long propertiesId);
	
	@Override
    public void delete(CoreTheme theme);

}
