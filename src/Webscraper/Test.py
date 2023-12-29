import sqlite3



def pokemon_table(cursor):
    cursor.execute('''CREATE TABLE IF NOT EXISTS pokemon (
                        id INTEGER NOT NULL,
                        name TEXT PRIMARY KEY,
                        type1 TEXT NOT NULL,
                        type2 TEXT,
                        total INTEGER NOT NULL,
                        hp INTEGER NOT NULL,
                        attack INTEGER NOT NULL,
                        defense INTEGER NOT NULL,
                        spatk INTEGER NOT NULL,
                        spdef INTEGER NOT NULL,
                        speed INTEGER NOT NULL
                    )''')
    cursor.execute("INSERT OR REPLACE INTO pokemon (id, name, type1, type2, total, hp, attack, defense, spatk, spdef, speed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", (0, 'Alice', 'Grass', None, 23, 18, 1, 1, 1, 1, 1))

def database_update():
    conn = sqlite3.connect('databases/pokemon_database.db')
    cursor = conn.cursor()
    pokemon_table(cursor)
    conn.commit()
    conn.close()

database_update()
