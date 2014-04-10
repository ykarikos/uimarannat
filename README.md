# Uimarannat

Yle [etsi Suomen parasta uimarantaa](http://yle.fi/uutiset/nyt_etsitaan_suomen_parasta_uimarantaa__kerro_kartalla_missa_kannattaa_pulahtaa/6707163). Tämän datan voisi esittää nätimmin kartalla ja luoda käyttöliittymän rantatietojen muokkaamiseen.

## Import-työkalu

Ylen keräämät datat löytyvät avoimena datana: http://datajournalismi.blogspot.fi/p/avoin-data_257.html

Importoi uimarantadatat näin:

1. Hae tsv-tiedosto [Uimapaikkakysely-spreadsheetistä](https://docs.google.com/spreadsheet/ccc?key=0AtSvRKpOIo3OdDZRdk01MTVzMUs2THotdlc0emtMaHc#gid=0) valitsemalla *File (Tiedosto) → Download as (Lataa tiedostona) → Tab separated values (Sarkaimilla erotetut arvot)* vaikkapa tiedostoon *uimapaikkakysely.tsv*
2. Alusta MongoDB-tietokanta
```
$ mongo
> use uimarannat
> db.rannat.ensureIndex( { location: "2dsphere" })
```
3. Importoi datat tietokantaan
```
$ lein run -m uimarannat.import uimarannat uimapaikkakysely.tsv
```

