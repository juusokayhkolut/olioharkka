# Tietoa sovelluksesta

Näyttö layout:
-> Main Layout on haku näyttö
--> Painamalla hae käyttäjä heitetään TabActivity näytölle jossa on tabit ja kolme näyttöä (fragmenttia)
---> MunicipalityDetailsFragment, CompareFragment ja QuizFragment

DataManager:
-> DataManager on luokka mihin voi tallentaa (set) ja lukea (get) tietoja avaimen perusteella

CityCodeLookup:
-> Stafin rajapinta on ärsyttävä joten CityCodeLookup on luokka joka palauttaa 'kuntakoodin'
kaupungin nimen perusteella (esim. 'Helsinki' -> 'KU091') jonka avulla saa oikean tiedon rajapinnasta

ApiClient:
-> ApiClient on luokka missä on erilaisia funktioita rajapintojen käyttöön. Tällä hetkellä
Statfin ja OpenWeather.

# Muistetaan
- katotaa et ohjelma on koodattu olioparadigman mukaisesti
- vaatimuslista yleisesti
- kaupungin nimi mitää olla valid ettei crashaa xd