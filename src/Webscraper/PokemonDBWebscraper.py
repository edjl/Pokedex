# Edward Lee
# December 21, 2023


from bs4 import BeautifulSoup
import requests
import sqlite3


def nameFix(name):
    parts = name.split(' ')
    count = len(parts)
    for i in range(1, count // 2 + 1):
        base_name = " ".join(parts[:i])
        spec_name = " ".join(parts[i:])
        if base_name in spec_name:
            return spec_name
    return name


def scrapePokemon():

    scrape_page = requests.get("https://pokemondb.net/pokedex/all")
    soup = BeautifulSoup(scrape_page.text, "html.parser")
    rows = soup.findAll("tr")
    rows = [row for row in rows if len(row.findAll("td")) == 10]

    conn = sqlite3.connect('databases/pokemon.db')
    cursor = conn.cursor()
    
    for row in rows:
        cells = row.findAll("td")
        row_data = [cell.text.strip() for cell in cells]

        # Name fix
        row_data[1] = nameFix(row_data[1])

        # Single Type vs Dual Type
        types = row_data[2].split(' ')
        row_data.pop(2)
        if (len(types) == 1):
            types.append(None)
        row_data = row_data[:2] + types + row_data[2:]
            
        cursor.execute("INSERT OR REPLACE INTO pokemon (id, name, type1, type2, total, hp, attack, defense, spatk, spdef, speed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
            (row_data))

    conn.commit()
    conn.close()



def scrapeMove():
    scrape_page = requests.get("https://www.pokemonpets.com/Pokemon-Moves")
    soup = BeautifulSoup(scrape_page.text, "html.parser")

    table = soup.findAll("table")[0]
    rows = table.findAll("tr")
    rows = rows[1:]

    conn = sqlite3.connect('databases/pokemon.db')
    cursor = conn.cursor()

    for row in rows:
        cells = row.findAll("td")
        row_data = [cell.text.strip() for cell in cells]
        row_data = [row_data[1], row_data[3], row_data[4], row_data[5], row_data[6]]
        if row_data[3] == "1":
            row_data[3] = None
        if row_data[4] == "1":
            row_data[4] = None
        elif (int)(row_data[4]) > 100:
            row_data[4] = "100"
        cursor.execute("INSERT OR REPLACE INTO moves (name, type, category, power, accuracy) VALUES (?, ?, ?, ?, ?)", 
            (row_data))

    conn.commit()
    cursor.execute("SELECT * FROM moves")
    rows = cursor.fetchall()

    # Print the retrieved data
    for row in rows:
        print(row)
    conn.close()


def scrapeNature():
    scrape_page = requests.get("https://www.pokemonpets.com/Natures")
    soup = BeautifulSoup(scrape_page.text, "html.parser")

    table = soup.findAll("table")[0]
    rows = table.findAll("tr")
    rows = rows[1:]

    conn = sqlite3.connect('databases/pokemon.db')
    cursor = conn.cursor()
    cursor.execute("INSERT OR REPLACE INTO nature (name, hp, attack, defense, spatk, spdef, speed) VALUES (?, ?, ?, ?, ?, ?, ?)",
        ["???", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0])

    for row in rows:
        cells = row.findAll("td")
        row_data = [cell.text.strip() for cell in cells][1:]
        row_data = [((int(str[:-1]) / 100.0 + 1)) if str.endswith('%') else str for str in row_data]
        cursor.execute("INSERT OR REPLACE INTO nature (name, hp, attack, defense, spatk, spdef, speed) VALUES (?, ?, ?, ?, ?, ?, ?)", 
            (row_data))

    conn.commit()
    conn.close()


def scrapeType():

    scrape_page = requests.get("https://pokemondb.net/type")
    soup = BeautifulSoup(scrape_page.text, "html.parser")

    table = soup.findAll("table")[0]
    rows = table.findAll("tr")
    rows = rows[1:]

    conn = sqlite3.connect('databases/pokemon.db')
    cursor = conn.cursor()

    for row in rows:
        cells = row.findAll("th") + row.findAll("td")
        row_data = [cell.text.strip() for cell in cells]
        for i in range(len(row_data)):
            if row_data[i] == "½":
                row_data[i] = "0.5"
            elif row_data[i] == "":
                row_data[i] = 1
        cursor.execute("INSERT OR REPLACE INTO types (name, normal, fire, water, electric, grass, ice, fighting, poison, ground, flying, psychic, bug, rock, ghost, dragon, dark, steel, fairy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
            (row_data))

    conn.commit()
    conn.close()



def createTables():
    conn = sqlite3.connect('databases/pokemon.db')
    cursor = conn.cursor()
    with open('src/Webscraper/PokemonDB_TableMaker.sql', 'r') as sql_file:
        sql_script = sql_file.read()
        cursor.executescript(sql_script)
    conn.commit()
    conn.close()



def main():
    import sys
    function_name = sys.argv[1]

    if function_name == "Tables":
        createTables()
    elif function_name == "Pokemon":
        scrapePokemon()
    elif function_name == "Move":
        scrapeMove()
    elif function_name == "Nature":
        scrapeNature()
    elif function_name == "Type":
        scrapeType()

if __name__ == "__main__":
    main()
    