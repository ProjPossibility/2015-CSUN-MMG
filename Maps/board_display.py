# -*- coding: utf-8 -*-
"""
Created on Sun Feb 15 10:29:46 2015

@author: g
"""
import re

def readfile(item):
    with open(item, 'r') as f:
        data = f.read()

    lines = data.splitlines()

    tiles_str = [re.findall("([0-99]+)", string) for string in lines]
    tiles_bin = []
    for row in tiles_str:
        tiles_bin_row = []
        for tile in row:
            tiles_bin_row.append(bin(int(tile)))    
        tiles_bin.append(tiles_bin_row)

#split binary values from maze array to form final tileset
    tiles = []
    for row in tiles_bin:
        tiles_row = []    
        for tile in row:
            chars = []        
            string = str(tile).replace('0b','')
            if len(string) < 6:
                for num in range(6-len(string)):
                    chars.append(0)
            for char in string:
                chars.append(int(str(char)))
            tiles_row.append(chars)
        tiles.append(tiles_row)
    return tiles
data = readfile('maze10')
print data

new_data = []

def find_start(dataset):
    for row in dataset:
        for item in row:
            if item[4] == 1:
                return "Start: " + str([data.index(row),row.index(item)])
def find_end(dataset):
    for row in dataset:
        for item in row:
            if item[5] == 1:
                return "End: " + str([data.index(row),row.index(item)])
print find_start(data)
print find_end(data)
for row in data:
    text_display = []       
    for item in row:
        if item[2] == 1 and item[1] == 1:
            text_display.append('__|')
        elif item[2] == 1:
            text_display.append('___')
        elif item[1] == 1:
            text_display.append('  |')
        else:
            text_display.append('   ')
    new_data.append(text_display)
    if row[0][3] == 1 and row[0][2] == 1 and row[0][1] == 1:
        new_data[data.index(row)][0] = '|_|'
    elif row[0][3] == 1 and row[0][1] == 1:
        new_data[data.index(row)][0] = '| |'
    elif row[0][3] == 1:
        new_data[data.index(row)][0] = '|  '
    
print new_data

merged = []
for row in new_data:
    row_string = ''
    for item in row:
        row_string += item
    merged.append(row_string)

for line in merged:
    print line
