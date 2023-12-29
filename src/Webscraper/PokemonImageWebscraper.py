# Edward Lee
# December 21, 2023

from bs4 import BeautifulSoup
from concurrent.futures import ThreadPoolExecutor
import csv
import requests
import os
import threading


def download_image(pokemon_name, image_url):
    response = requests.get(image_url)
    if response.status_code == 200:
        if not os.path.exists('images'):
            os.makedirs('images')
        with open(f"images/pokemon/{pokemon_name}.png", 'wb') as f:
            f.write(response.content)


def scrapeImages():
    pokemon_names = []
    with open("tables/Pokemon.csv", newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            pokemon_names.append(row['Name'])

    base_url = "https://pokemondb.net/pokedex/"
    special_url_names = ["Deoxys", "Burmy", "Wormadam", "Giratina", "Shaymin", "Basculin", "Darmanitan", "Tornadus", "Thundurus", \
        "Landorus", "Kyurem", "Keldeo", "Meloetta", "Meowstic", "Aegislash", "Pumpkaboo", "Gourgeist", "Zygarde", "Hoopa", \
        "Oricorio", "Lycanroc", "Wishiwashi", "Minior", "Toxtricity", "Eiscue", "Indeedee", "Morpeko", "Zacian", \
        "Zamazenta", "Urshifu", "Basculegion", "Enamorus", "Oinkologne", "Maushold", "Squawkabilly", "Palafin", \
        "Tatsugiri", "Dudunsparce", "Gimmighoul"]

    def scrapeImage(index):
        url_name = pokemon_names[index]
        if url_name.split()[0] in special_url_names:
            url_name = url_name.split()[0]
        url_name = url_name.replace("'", "").replace(".", "").replace(":", "").replace(" ", "-").replace("é", "e")
        if url_name[-1] == '♂':
            url_name = url_name[:-1] + "-m"
        elif url_name[-1] == '♀':
            url_name = url_name[:-1] + "-f"

        url = f"{base_url}{url_name}"
        response = requests.get(url)

        if response.status_code == 200:
            soup = BeautifulSoup(response.text, 'html.parser')

            temp = soup.findAll("div", {"class": "grid-col span-md-6 span-lg-4 text-center"})
            for i in range(len(temp)):
                if pokemon_names[index + i] == "Ursaluna Bloodmoon":
                    first_a_href = temp[0].findAll('img')[0]['src']
                else:
                    first_a_href = temp[i].findAll('img')[0]['src']
                download_image(pokemon_names[index + i], first_a_href)
            index += len(temp)

    
    index = 0
    threadCount = 20
    threads = []
    while index < len(pokemon_names):
        for i in range(threadCount):
            thread = threading.Thread(target=scrapeImage, args=(index,))
            threads.append(thread)
            thread.start()
            index += 1
            if index >= len(pokemon_names):
                break
        for thread in threads:
            thread.join()
        threads.clear()


def main():
    scrapeImages()

if __name__ == "__main__":
    main()
