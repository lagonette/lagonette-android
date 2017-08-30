package org.lagonette.android.room.entity.special;

import org.lagonette.android.room.entity.PartnerMetadata;

public class OfficePartnerMetadata extends PartnerMetadata {

    public OfficePartnerMetadata() {
        partnerId = OfficePartner.ID;
        isVisible = true;
    }
}
