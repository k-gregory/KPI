import os
import django

from bs4 import BeautifulSoup
import requests

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "tourism.settings")
django.setup()

from hotels.models import Country, Hotel


def parse_simple(url):
    result = requests.get(url)
    soup = BeautifulSoup(result.content, "html.parser")
    paragraphs = soup.find_all("p")

    if len(paragraphs) < 2:
        raise RuntimeError("No paragraph")

    paragraphs.sort(key=lambda p: -len(p.text))

    if not paragraphs[0].text:
        raise RuntimeError("Empty paragraph")

    return paragraphs[0].text

base_url = "https://en.wikipedia.org"

result = requests.get(base_url + "/wiki/List_of_largest_hotels")
soup = BeautifulSoup(result.content, "html.parser")

tables = soup.find_all('table')
tables.sort(key=lambda t: len(t.find_all('tr')))
table = tables[-1]
table_rows = table.find_all('tr')[1:]

countries = {}
country_hotels = {}

for idx, row in enumerate(table_rows):
    try:
        print((idx, len(table_rows)))
        tds = row.find_all('td')
        name_td = tds[1]
        country_td = tds[2]

        hotel_name = name_td.a.string
        country_name = country_td.a.string

        if country_name not in countries:
            countries[country_name] = parse_simple(base_url + country_td.a['href'])

        if country_name not in country_hotels:
            country_hotels[country_name] = []

        country_hotels[country_name].append({
            'name': hotel_name,
            'description': parse_simple(base_url + name_td.a['href'])
        })
    except Exception as e:
        print(e)
        print(row)

final_hotels = [(country_name, hotel)
                for country_name, hotels in country_hotels.items()
                for hotel in hotels[:15] if len(hotels) > 0]


saved_countries = {}

for country_name, description in countries.items():
    saved_countries[country_name] = Country.objects.create(name = country_name, description = description[:2000])

for country_name, hotel in final_hotels:
    Hotel(name=hotel['name'],
          brief_description=hotel['description'][:150],
          description = hotel['description'][:2000],
          country = saved_countries[country_name]
          ).save()
