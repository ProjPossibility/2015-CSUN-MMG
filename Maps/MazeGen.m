function MazeGen
    clear
    validMazes = 0;
    
    %while validMazes < 1 
        maze = Generate;
    %end
    
    function maze = Generate
        rng('shuffle');
        mazeSize = 8;
        
        maze(mazeSize,mazeSize)=0
        for m=1:mazeSize
            for n=1:mazeSize
                tile = 0;
                for o=2:5
                    tile = tile + round(rand(1))*2^o
                end
                maze(m,n)=tile
            end
        end
        
        % assign maze start and end at random
        mazeStartX = round(rand(1)*mazeSize/2)+1;
        mazeStartY = round(rand(1)*mazeSize/2)+1;
        maze(mazeStartX,mazeStartY) = maze(mazeStartX,mazeStartY) + 2;
        
        mazeEndX = round(rand(1)*mazeSize/2 + mazeSize/2)+1;
        mazeEndY = round(rand(1)*mazeSize/2 + mazeSize/2)+1;
        maze(mazeEndX,mazeEndY) = maze(mazeEndX,mazeEndY) + 1;
        
        isValid = isValidMaze (maze,[mazeStartX,mazeStartY],...
            [mazeEndX,mazeEndY])
    end

    function isValid = isValidMaze(maze, mazeStart, mazeEnd)
       	isValid = 1
    end
end


        