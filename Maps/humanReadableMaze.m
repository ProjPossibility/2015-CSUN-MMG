function humanReadableMaze( maze )
%HUMANREADABLEMAZE Print a human-readable maze from a numerical array.
%   Detailed explanation goes here
    if nargin < 1
        maze = 60;
    end

    [rows,cols] = size(maze);
    charMatrix{rows*3,cols*3} = '';

    for m=1:rows
        for n=1:cols
            tileArray = mazeTileFromNumber(maze(m,n));
            if (tileArray(4)) % West
                charMatrix{3*m-1,3*n-2} = '|';
            else
                charMatrix{3*m-1,3*n-2} = ' ';
            end
            if (tileArray(2)) % East
                charMatrix{3*m-1,3*n} = '|';
            else
                charMatrix{3*m-1,3*n} = ' ';
            end
            if (tileArray(1)) % North
                for o=0:2
                    charMatrix{3*m-2,3*n-o} = '-';
                end
            else
                for o=0:2
                    charMatrix{3*m-2,3*n-o} = ' ';
                end
            end
            if (tileArray(3)) % South
                for o=0:2
                    charMatrix{3*m,3*n-o} = '-';
                end
            else
                for o=0:2
                    charMatrix{3*m,3*n-o} = ' ';
                end
            end
            if (tileArray(5)) % Start
                charMatrix{3*m-1,3*n-1} = 'S';
                if (tileArray(6))
                	error('Start and end points are identical.')
                end
            elseif (tileArray(6)) % Start
                charMatrix{3*m-1,3*n-1} = 'E';
            else
                charMatrix{3*m-1,3*n-1} = ' ';
            end
        end
    end
    
    [rows,cols] = size(charMatrix);
    s = '';
    for m=1:rows
        for n=1:cols
            s = sprintf('%s%s',s,charMatrix{m,n});
        end
        s=sprintf('%s\n',s);
    end
    disp(s)
end

