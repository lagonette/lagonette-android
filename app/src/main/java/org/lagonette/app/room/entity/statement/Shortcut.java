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
	public final long headquarterLocationId;

	@NonNull
	@ColumnInfo(name = "headquarter_icon")
	public final String headquarterIcon;

	public Shortcut(
			int visiblePartnerCount,
			int partnerCount,
			int visibleExchangeOfficeCount,
			int exchangeOfficeCount,
			long headquarterLocationId,
			@NonNull String headquarterIcon) {
		this.visiblePartnerCount = visiblePartnerCount;
		this.partnerCount = partnerCount;
		this.visibleExchangeOfficeCount = visibleExchangeOfficeCount;
		this.exchangeOfficeCount = exchangeOfficeCount;
		this.headquarterLocationId = headquarterLocationId;
		this.headquarterIcon = headquarterIcon;
	}

	public boolean isPartnerShortcutSelected() {
		return visiblePartnerCount == partnerCount;
	}

	public boolean isExchangeOfficeShortcutSelected() {
		return visibleExchangeOfficeCount == exchangeOfficeCount;
	}
}
