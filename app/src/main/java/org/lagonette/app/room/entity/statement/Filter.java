package org.lagonette.app.room.entity.statement;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.support.v7.util.DiffUtil;

import org.lagonette.app.room.embedded.Address;
import org.lagonette.app.room.statement.FilterStatement;

public class Filter {

	public static class DiffCallback
			extends DiffUtil.ItemCallback<Filter> {

		@Override
		public boolean areItemsTheSame(Filter oldItem, Filter newItem) {
			if (oldItem == null && newItem == null) return true;
			if (oldItem == null) return false;
			if (newItem == null) return false;
			if (oldItem.rowType != newItem.rowType) return false;
			if (oldItem.categoryId != newItem.categoryId) return false;
			return oldItem.locationId == newItem.locationId;
		}

		@Override
		public boolean areContentsTheSame(Filter oldItem, Filter newItem) {
			if (oldItem == null && newItem == null) return true;
			if (oldItem == null) return false;
			if (newItem == null) return false;
			return oldItem.hashCode() == newItem.hashCode();
		}
	}

	public static final DiffCallback DIFF_CALLBACK = new DiffCallback();

	@FilterStatement.RowType
	@ColumnInfo(name = "row_type")
	public final int rowType;

	@ColumnInfo(name = "category_id")
	public final long categoryId;

	@ColumnInfo(name = "category_label")
	public final String categoryLabel;

	@ColumnInfo(name = "category_icon")
	public final String categoryIcon;

	@ColumnInfo(name = "category_display_order")
	public final int displayOrder;

	@ColumnInfo(name = "category_is_visible")
	public final boolean isCategoryVisible;

	@ColumnInfo(name = "category_is_collapsed")
	public final boolean isCategoryCollapsed;

	@ColumnInfo(name = "category_is_partners_visible")
	public final boolean isCategoryPartnersVisible;

	@ColumnInfo(name = "location_id")
	public final long locationId;

	@Embedded(prefix = "location_")
	public final Address address;

	@ColumnInfo(name = "location_is_exchange_office")
	public final boolean isLocationExchangeOffice;

	@ColumnInfo(name = "location_is_visible")
	public final boolean isLocationVisible;

	@ColumnInfo(name = "partner_name")
	public final String partnerName;

	private transient final int mHashCode;

	public Filter(
			int rowType,
			long categoryId,
			String categoryLabel,
			String categoryIcon,
			int displayOrder,
			boolean isCategoryVisible,
			boolean isCategoryCollapsed,
			boolean isCategoryPartnersVisible,
			long locationId,
			Address address,
			boolean isLocationExchangeOffice,
			boolean isLocationVisible,
			String partnerName) {
		this.rowType = rowType;
		this.categoryId = categoryId;
		this.categoryLabel = categoryLabel;
		this.categoryIcon = categoryIcon;
		this.displayOrder = displayOrder;
		this.isCategoryVisible = isCategoryVisible;
		this.isCategoryCollapsed = isCategoryCollapsed;
		this.isCategoryPartnersVisible = isCategoryPartnersVisible;
		this.locationId = locationId;
		this.address = address;
		this.isLocationExchangeOffice = isLocationExchangeOffice;
		this.isLocationVisible = isLocationVisible;
		this.partnerName = partnerName;

		mHashCode = createHashCode();
	}

	@Override
	public int hashCode() {
		return mHashCode;
	}

	private int createHashCode() {
		int result = rowType;
		result = 31 * result + (int) (categoryId ^ (categoryId >>> 32));
		result = 31 * result + (categoryLabel != null ? categoryLabel.hashCode() : 0);
		result = 31 * result + (categoryIcon != null ? categoryIcon.hashCode() : 0);
		result = 31 * result + displayOrder;
		result = 31 * result + (isCategoryVisible ? 1 : 0);
		result = 31 * result + (isCategoryCollapsed ? 1 : 0);
		result = 31 * result + (isCategoryPartnersVisible ? 1 : 0);
		result = 31 * result + (int) (locationId ^ (locationId >>> 32));
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (isLocationExchangeOffice ? 1 : 0);
		result = 31 * result + (isLocationVisible ? 1 : 0);
		result = 31 * result + (partnerName != null ? partnerName.hashCode() : 0);
		return result;
	}
}
