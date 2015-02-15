
import lxml.etree as et
import lxml.builder
import maze_read_xml
tiles = maze_read_xml.readfile('maze10')
print tiles
boards = []
boards.append(tiles)
num_boards = len(boards)
E = lxml.builder.ElementMaker()
RESOURCES = E.resource
ARRAY = E.array
ITEM = E.item
def gen_strings(board_num, row_num):
    return '\"'+str(row_num)+'0\"' + ':' + '\"' + boards[board_num][str(row_num)+'0'] + '\"'+ ','+ '\"'+str(row_num)+'1\"' + ':' + '\"' + boards[board_num][str(row_num)+'1'] + '\"'+ ','+ '\"'+str(row_num)+'2\"' + ':' + '\"' + boards[board_num][str(row_num)+'2'] + '\"'+ ','+ '\"'+str(row_num)+'3\"' + ':' + '\"' + boards[board_num][str(row_num)+'3'] + '\"'+ ','+ '\"'+str(row_num)+'4\"' + ':' + '\"' + boards[board_num][str(row_num)+'4'] + '\"'+ ','+ '\"'+str(row_num)+'5\"' + ':' + '\"' + boards[board_num][str(row_num)+'5'] + '\"'+ ','+ '\"'+str(row_num)+'6\"' + ':' + '\"' + boards[board_num][str(row_num)+'6'] + '\"'+ ','+ '\"'+str(row_num)+'7\"' + ':' + '\"' + boards[board_num][str(row_num)+'7'] + '\"'+','     
                   
def rows_loop(board_num, num_rows):
    board_info = ''    
    for num in range(num_rows):
        board_info += gen_strings(board_num, num)
    return '{'+ '\"ID\"'+ ':'+ '\"'+str(board_num)+'\"' + ',' + board_info[:len(board_info)-1]


tree = RESOURCES(
    ARRAY(ITEM('numBoards=' + str(num_boards)),
          ITEM('numRows='+str(8)),
          ITEM('numCols='+str(8)),
          name='boardInfo'),
    ARRAY(ITEM(rows_loop(0,8)),
         name='boardData')
        

)

print lxml.etree.tostring(tree, pretty_print = True)
print rows_loop(0,8)
