% plotter.m - A script to calculate and plot the cosine function for a given range of x values.
%
% This script prompts the user for input parameters, calculates the cosine values for a specified
% range of x, plots the resulting graph, and stores the data in a CSV file.
%
% Features:
% - User input for range bounds, increment, and output file name
% - Calculation of cos(x) for the specified range
% - Plotting of the cos(x) graph
% - Storage of x and cos(x) values in a CSV file
%
% Author: Arnab Das Utsa
%
% Usage:
% 1. Run the script in Octave or MATLAB.
% 2. Follow the prompts to enter the lower bound, upper bound, increment, and file name.
% 3. The script will generate a plot of cos(x) and save the data to the specified CSV file.

% ask user for parameters
%Calculating cos(x)
low = input('Enter the lower range of x :');
upp = input('Enter the upper range of x :');
incr = input('Enter the increment of x :');
filename = input('Enter file name to store data: ', 's');
if ~endsWith(filename, '.csv', 'IgnoreCase', true)
filename = strcat(filename, '.csv');
end

% now plot the the data
x = (low:incr:upp);
y = cos(x);
xy_data = [x, y];
xy_col = [x(:), y(:)];

figure;
plot(x, y);
title('cos(x) graph');
xlabel('value of x');
ylabel('cos(x)');
grid on;

% Store data in csv file
csvwrite(filename, xy_col);
disp(['Data stored in ', filename]);
