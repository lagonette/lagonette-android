package org.lagonette.app.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.lagonette.app.room.entity.Category;
import org.lagonette.app.room.entity.CategoryMetadata;

import java.util.List;

@Dao
public interface CategoryDao {

    //TODO Use foreign key cascade
    @Query("DELETE FROM category")
    void deleteCategories();

    @Insert
    long insertCategory(Category category);

    @Insert
    long[] insertCategories(List<Category> categories);

    @Insert
    long[] insertCategoriesMetadatas(List<CategoryMetadata> categoriesMetadatas);

    @Query("UPDATE category_metadata SET is_visible = :isVisible WHERE category_id = :id AND category_type = :type")
    int updateCategoryVisibility(long id, long type, boolean isVisible);

    @Query("UPDATE category_metadata SET is_visible = :isVisible")
    int updateCategoryVisibilities(boolean isVisible);

    @Query("UPDATE category_metadata SET is_collapsed = :isCollapsed WHERE category_id = :id AND category_type = :type")
    int updateCategoryCollapsed(long id,  long type, boolean isCollapsed);
}
