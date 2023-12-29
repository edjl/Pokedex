/*
 * Edward Lee
 * December 21, 2023
 */

CREATE TABLE IF NOT EXISTS moves (
                        name TEXT PRIMARY KEY,
                        type TEXT NOT NULL,
                        category TEXT NOT NULL,
                        power INTEGER,
                        accuracy INTEGER
                    );

CREATE TABLE IF NOT EXISTS nature (
                        name TEXT PRIMARY KEY,
                        hp REAL NOT NULL,
                        attack REAL NOT NULL,
                        defense REAL NOT NULL,
                        spatk REAL NOT NULL,
                        spdef REAL NOT NULL,
                        speed REAL NOT NULL
                    );

CREATE TABLE IF NOT EXISTS pokemon (
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
                    );

CREATE TABLE IF NOT EXISTS types (
                        name TEXT PRIMARY KEY,
                        normal REAL NOT NULL,
                        fire REAL NOT NULL,
                        water REAL NOT NULL,
                        electric REAL NOT NULL,
                        grass REAL NOT NULL,
                        ice REAL NOT NULL,
                        fighting REAL NOT NULL,
                        poison REAL NOT NULL,
                        ground REAL NOT NULL,
                        flying REAL NOT NULL,
                        psychic REAL NOT NULL,
                        bug REAL NOT NULL,
                        rock REAL NOT NULL,
                        ghost REAL NOT NULL,
                        dragon REAL NOT NULL,
                        dark REAL NOT NULL,
                        steel REAL NOT NULL,
                        fairy REAL NOT NULL
                    );