package org.lagonette.app.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import org.lagonette.app.room.entity.Category;
import org.lagonette.app.room.entity.CategoryMetadata;

import java.util.List;

@Dao
public interface CategoryDao {

	@Query("SELECT count(1) FROM category")
	int getCategorieCount();

	@Query("DELETE FROM category WHERE id = :id")
	void deleteCategory(long id);

	@Query("DELETE FROM category")
	void deleteCategories();

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertCategory(Category category);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	long insertCategoryMetadata(CategoryMetadata categoryMetadata);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertCategories(List<Category> categories);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertCategoriesMetadatas(List<CategoryMetadata> categoriesMetadatas);

	@Query("UPDATE category_metadata SET is_visible = :isVisible WHERE category_id = :id")
	int updateCategoryVisibility(long id, boolean isVisible);

	@Query("UPDATE category_metadata SET is_visible = category_id == :id")
	int makeVisibleOneCategory(long id);

	@Query("UPDATE category_metadata SET is_visible = :isVisible")
	void updateCategoryVisibilities(boolean isVisible);

	@Query("UPDATE category_metadata SET is_collapsed = :isCollapsed WHERE category_id = :id")
	int updateCategoryCollapsed(long id, boolean isCollapsed);

	@Query("UPDATE category_metadata SET is_collapsed = :isCollapsed")
	int updateAllCategoriesCollapsed(boolean isCollapsed);

	@Query("UPDATE category_metadata SET is_collapsed = 1, is_visible = 1")
	void resetMetadata();

	@Query("DELETE FROM category_metadata WHERE category_id NOT IN (SELECT id FROM category)")
	void cleanOrphanCategoryMetadata();
}
