function MazeGen
clear
validMazes = 0;
invalidMazes = 0;
mazeSize = 8;
maze(mazeSize,mazeSize) = 0;
maxValid = 10;
maxInvalid = 10000;

while ((validMazes < maxValid) & (invalidMazes < maxInvalid))
    [maze,isValid] = Generate;
    if (isValid) 
        disp('Valid maze found:')
        maze
        pause(1)
        validMazes = validMazes + 1;
        mazeName = sprintf('SampleMaze%g',validMazes);
        %save(sprintf('%s.mat',mazeName),'maze')
        dlmwrite(sprintf('%s.txt',mazeName),maze,'Delimiter',' ','NewLine','PC')
    else
        invalidMazes = invalidMazes + 1;
        %disp(sprintf('Invalid maze number %g found:',invalidMazes))
    end
    
end


    function [maze, isValid] = Generate
        rng('shuffle');
        
        maze(mazeSize,mazeSize)=0;
        for m=1:mazeSize %rows
            for n=1:mazeSize %cols
                tile = 0;
                % N -> 2^5
                if (m == 1)
                    tile = tile + 2^5;
                else
                    % for things that aren't in the first row, they need to
                    % pull their northern wall from the southern wall of
                    % the tile above them - pull coefficient of 2^3.
                    neighbor = maze(m-1,n);
                    tile = tile + floor(mod(mod(neighbor,2^5),2^4)/2^3)*2^5;
                end
                % E -> 2^4
                if (n == mazeSize)
                    tile = tile + 2^4;
                else
                    tile = tile + round(rand(1))*2^4;
                end
                % S -> 2^3
                if (m == mazeSize)
                    tile = tile + 2^3;
                else
                    tile = tile + round(rand(1))*2^3;
                end
                % W -> 2^2
                if (n == 1)
                    tile = tile + 2^2;
                else
                    % for things that aren't in the first column, they need to
                    % pull their western wall from the eastern wall of
                    % the tile left them - pull coefficient of 2^4.
                    neighbor = maze(m,n-1);
                    tile = tile + floor(mod(neighbor,2^5)/2^4)*2^2;
                end
                maze(m,n)=tile;
            end
        end
        
        % assign maze start and end at random
        mazeStartRow = round(rand(1)*(mazeSize/2-1))+1;
        mazeStartCol = round(rand(1)*(mazeSize/2-1))+1;
        maze(mazeStartRow,mazeStartCol) = maze(mazeStartRow,mazeStartCol) + 2;
        
        mazeEndCol = round(rand(1)*mazeSize/2 + mazeSize/2);
        mazeEndRow = round(rand(1)*mazeSize/2 + mazeSize/2);
        maze(mazeEndRow,mazeEndCol) = maze(mazeEndRow,mazeEndCol) + 1;
        
        isValid = isValidMaze (maze,[mazeStartRow,mazeStartCol],...
            [mazeEndRow,mazeEndCol]);
    end

    function isValid = isValidMaze(maze, mazeStart, mazeEnd)
        position = mazeStart;
        facing =  [0 0 1 0]; % facing south by default
        counter = 0;
        escaped = 0;
        while (~escaped)
            % parse tile code into an array for human readability
            tileCode = maze(position(1),position(2));
            tileFull = mazeTileFromNumber(tileCode);
            tileArray = tileFull(1:4);
            if ((position ~= mazeStart) & ...
                    (tileArray(mod(find(facing == 1)+1,4)+1) == 1) & ...
                    counter > 0)
                % back is not clear = made an invalid move!
                display('Debugging output for invalid move:')
                counter
                facing
                tileArray
                maze
                position
                save('invalidMaze.mat','maze')
                error('Moved through an unduplicated wall!')
            end
            
            % try to position yourself so you have a wall on your right and
            % your front is clear.
            
            positionGood = 0; 
            
            % can turn up to 4 times?prevents twirling
            clearFaceFound = 0;
            for turnCounter = 0:4
                facingIndex = find(facing == 1);
                if (tileArray(facingIndex) == 0) 
                    % front is clear - record this state was found
                    % disp('Front is clear.')
                    clearFaceFound = 1;
                    
                    % OK to move in this position if we are in it at end of
                    % loop
                    positionGood = 1;
                    
                    if (tileArray(mod(facingIndex+1,4)+1) ~= 1)
                        % no wall on right, so turn
                        facing = turnRight(facing);
                        facingIndex = find(facing == 1);
                        turnCounter = turnCounter + 1;
                        
                        % reset positionGood - no idea if we are facing a wall
                        positionGood = 0; 
                    else
                        % there is also a wall on your right--success!
                        break
                    end
                else % front is blocked--turn.
                    % disp('Front is blocked.')
                    facing = turnRight(facing);
                    facingIndex = find(facing == 1);
                    turnCounter = turnCounter + 1;

                    % reset positionGood - no idea if we are facing a wall
                    positionGood = 0; 
                end
            end
            
            if (positionGood == 0)
                if (clearFaceFound == 0)
                    % no way out! no clear face found! invalid maze if this
                    % is the starting position.
                    if position == mazeStart
                        isValid = 0;
                        return
                    end
                    display('Debugging output for island:')
                    facing
                    tileArray
                    position
                    warning('No clear face found at this position.')
                    break
                else % a clear face was found--find it again
                    for p=1:4
                        facing = turnRight(facing);
                        % reset positionGood
                        positionGood = 0;
                        
                        facingIndex = find(facing == 1);
                        if (tileArray(facingIndex) == 0) 
                            positionGood = 1;
                            break
                        end
                    end
                end
            end
                
            if (positionGood)
                % OK to move
                if (tileArray(facingIndex) == 0)
                    facingIndex = find(facing == 1);
                    
%                     disp('Debugging output before move:')
%                     position
%                     facing
%                     tileArray
%                     facing
%                     disp('Moving...')

                    switch (facingIndex)
                        case 1
                            position(1) = position(1) - 1;
                        case 2
                            position(2) = position(2) + 1;
                        case 3
                            position(1) = position(1) + 1;
                        otherwise
                            position(2) = position(2) - 1;
                    end
                else
                    disp('Debugging output:')
                    tileArray
                    facing
                    position
                    positionGood
                    error('Error encountered with assigning positionGood.')
                end
            else 
                disp('Debugging output:')
                tileArray
                facing
                position
                positionGood
                error('Position is bad--no available move detected.')
            end
            
            % check for the end tile
            escaped = (mod(tileCode,2) == 1);
            if (escaped) 
                disp ('Escaped!')
            end
            
            % emergency break: limit number of iterations during testing
            if counter > 10
                isValid = 0;
                return
            end
        counter = counter + 1; % used for emergency brake on looping
        end
        isValid = escaped;
    end

    function facing = turnRight (facing)
        %disp('Turn.');
        facing = [facing(4) facing(1:3)];
    end
end


