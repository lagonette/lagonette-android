package org.lagonette.android.room.entity.special;

import org.lagonette.android.room.embedded.Address;
import org.lagonette.android.room.entity.Partner;

public class OfficePartner extends Partner {

    public static final long ID = Long.MAX_VALUE; // TODO warn you with firebase if another partner as this ID

    // TODO Avoid crash when insert partner or category in DB (crash when office catgeory id = 1)

    public OfficePartner() {
        this.id = ID;
        name = "La Gonette";
        description = "Les bureaux de la Gonette.";
        latitude = 45.771283;
        longitude = 4.832055;
        clientCode = "Nope.";
//        logo;
        phone = "09 51 57 91 33";
        website = "lagonette.org";
        email = "co@lagonette.org";
        openingHours = "Du lundi au samedi, de 10 à 18h";
        isExchangeOffice = true;
        shortDescription = "Les bureaux de la Gonette.";
        mainCategoryId = OfficeCategory.ID;
        address = new Address();
        address.street = "4 Rue Imbert Colomes";
        address.city = "Lyon";
        address.zipCode = "69001";
    }
}
