package com.foodrescue.userverwaltung.domain.queries;

import com.foodrescue.userverwaltung.domain.valueobjects.Adresse;
import com.foodrescue.userverwaltung.domain.valueobjects.AnbieterProfilId;
import com.foodrescue.userverwaltung.domain.valueobjects.GeoStandort;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftsname;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftstyp;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;

public class AnbieterProfilDetailsQuery {

    private final AnbieterProfilId id;
    private final UserId userId;
    private final Geschaeftsname geschaeftsname;
    private final Geschaeftstyp geschaeftstyp;
    private final Adresse adresse;
    private final GeoStandort geoStandort; // darf null sein

    public AnbieterProfilDetailsQuery(
            AnbieterProfilId id,
            UserId userId,
            Geschaeftsname geschaeftsname,
            Geschaeftstyp geschaeftstyp,
            Adresse adresse,
            GeoStandort geoStandort) {
        this.id = id;
        this.userId = userId;
        this.geschaeftsname = geschaeftsname;
        this.geschaeftstyp = geschaeftstyp;
        this.adresse = adresse;
        this.geoStandort = geoStandort;
    }

    public AnbieterProfilId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public Geschaeftsname getGeschaeftsname() {
        return geschaeftsname;
    }

    public Geschaeftstyp getGeschaeftstyp() {
        return geschaeftstyp;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public GeoStandort getGeoStandort() {
        return geoStandort;
    }
}
