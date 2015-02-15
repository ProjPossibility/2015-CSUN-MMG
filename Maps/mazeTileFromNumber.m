function tileArray = mazeTileFromNumber (x) 
    tileArray(6) = 0;
    for m=1:6
        tileArray(m) = floor(x/2^(5-(m-1)));
        x = mod(x,2^(5-(m-1)));
    end
end