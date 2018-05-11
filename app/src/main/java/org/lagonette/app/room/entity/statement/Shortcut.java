package org.lagonette.app.room.entity.statement;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;

public class Shortcut {

	@ColumnInfo(name = "visible_partner_count")
	public final int visiblePartnerCount;

	@ColumnInfo(name = "partner_count")
	public final int partnerCount;

	@ColumnInfo(name = "visible_exchange_office_count")
	public final int visibleExchangeOfficeCount;

	@ColumnInfo(name = "exchange_office_count")
	public final int exchangeOfficeCount;

	@ColumnInfo(name = "headquarter_location_id")
	public final Long headquarterLocationId;

	@NonNull
	@ColumnInfo(name = "headquarter_icon")
	public final String headquarterIcon;

	@ColumnInfo(name = "is_all_category_collapsed")
	public final boolean isAllCategoryCollapsed;

	@ColumnInfo(name = "is_all_category_visible")
	public final boolean isAllCategoryVisible;

	@ColumnInfo(name = "there_is_some_partners")
	public boolean thereIsSomePartners;

	public Shortcut(
			boolean thereIsSomePartners,
			int visiblePartnerCount,
			int partnerCount,
			int visibleExchangeOfficeCount,
			int exchangeOfficeCount,
			Long headquarterLocationId,
			@NonNull String headquarterIcon,
			boolean isAllCategoryCollapsed,
			boolean isAllCategoryVisible) {
		this.thereIsSomePartners = thereIsSomePartners;
		this.visiblePartnerCount = visiblePartnerCount;
		this.partnerCount = partnerCount;
		this.visibleExchangeOfficeCount = visibleExchangeOfficeCount;
		this.exchangeOfficeCount = exchangeOfficeCount;
		this.headquarterLocationId = headquarterLocationId;
		this.headquarterIcon = headquarterIcon;
		this.isAllCategoryCollapsed = isAllCategoryCollapsed;
		this.isAllCategoryVisible = isAllCategoryVisible;
	}

	public boolean isPartnerShortcutSelected() {
		return visiblePartnerCount == partnerCount;
	}

	public boolean isExchangeOfficeShortcutSelected() {
		return visibleExchangeOfficeCount == exchangeOfficeCount;
	}
}
