package org.lagonette.app.room.entity.special;

import org.lagonette.app.room.entity.PartnerMetadata;

public class OfficePartnerMetadata extends PartnerMetadata {

    public OfficePartnerMetadata() {
        partnerId = OfficePartner.ID;
        isVisible = true;
    }
}
