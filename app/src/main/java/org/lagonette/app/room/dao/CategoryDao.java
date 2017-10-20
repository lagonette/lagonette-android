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

    //TODO Improve type
    @Query("SELECT * FROM category WHERE id = 0 AND type = 9999")
    Category getHiddenCategory();

    //TODO Improve type
    @Query("SELECT * FROM category_metadata WHERE category_id = 0 AND category_type = 9999")
    CategoryMetadata getHiddenCategoryMetadata();

    //TODO Use foreign key cascade
    @Query("DELETE FROM category")
    void deleteCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCategoryMetadata(CategoryMetadata categoryMetadata);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategories(List<Category> categories);

    //TODO Maybe use @relation and @transaction for insertion
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategoriesMetadatas(List<CategoryMetadata> categoriesMetadatas);

    @Query("UPDATE category_metadata SET is_visible = :isVisible WHERE category_id = :id AND category_type = :type")
    int updateCategoryVisibility(long id, long type, boolean isVisible);

    @Query("UPDATE category_metadata SET is_visible = :isVisible")
    void updateCategoryVisibilities(boolean isVisible);

    @Query("UPDATE category_metadata SET is_collapsed = :isCollapsed WHERE category_id = :id AND category_type = :type")
    int updateCategoryCollapsed(long id,  long type, boolean isCollapsed);
}
