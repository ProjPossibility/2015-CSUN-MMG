# a slightly different implementation of the maze_read module that
# returns a dict with values that are lists, not strings

# should be used in the game_logic module

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
        
#store tiles list in dictionary
    tiles_dict = {}
    for row in tiles:    
        for tile in row:
            occurrences = [i for i,j in enumerate(row) if j == tile]
            for item in occurrences:
                tiles_dict[str(tiles.index(row))+str(item)] = tile
    return tiles_dict
