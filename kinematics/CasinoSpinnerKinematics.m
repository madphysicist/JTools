%% Constants
t0 = 0.5;
tA = 3.0;
tB = 8.0;
tF = 10.5;
tC = 2.5;
tP = 4.5;

%% Functions
format = @(txt) set(txt, 'FontSize', 12, 'Color', [0.0, 0.0, 0.0]);
grey = @(obj) set(obj, 'Color', [0.6, 0.6, 0.6]);

set(figure, 'Color', [1 1 1]);

%% Acceleration plot (Full)
ha = axes('ActivePositionProperty', 'position', ...
          'Position', [2/27 16/26 14/27 8/26], ...
          'XLim', [-2 12], 'YLim', [-4 4]);
hold(ha, 'on');
axis(ha, 'equal');
set(ha, 'Visible', 'off');

% t-axis
grey(line([0 11.5], [0 0]));
% t-ticks
grey(line([t0 t0], 0.1 * [1 -1]));
grey(line([tA tA], 0.1 * [1 -1]));
grey(line([tB tB], 0.1 * [1 -1]));
grey(line([tF tF], 0.1 * [1 -1]));
% t-arrow
grey(line(11.5 - 0.3 * [1 0 1], 0.1 * [-1 0 1]));

% a-axis
grey(line([0 0], 4 * [1 -1]));
% a-ticks
grey(line(0.1 * [-1 1], 3 * [1 1]));
grey(line(0.1 * [-1 1], [0 0]));
grey(line(0.1 * [-1 1], -3 * [1 1]));
% a-arrows
grey(line(0.1 * [-1 0 1], 4 - 0.4 * [1 0 1]));
grey(line(0.1 * [-1 0 1], 0.4 * [1 0 1] - 4));

% data
plot(ha, [t0 t0], [ 0  3], 'b:');
plot(ha, [t0 tA], [ 3  3], 'b-', 'LineWidth', 2);
plot(ha, [tA tA], [ 3  0], 'b:');
plot(ha, [tA tB], [ 0  0], 'b-', 'LineWidth', 2);
plot(ha, [tB tB], [ 0 -3], 'b:');
plot(ha, [tB tF], [-3 -3], 'b-', 'LineWidth', 2);
plot(ha, [tF tF], [-3  0], 'b:');

% title
format(text(0.5, 1.1, 'Acceleration vs. Time (Full)', ...
    'HorizontalAlignment', 'center', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'normalized'));

% t-axis labels
format(text([t0 tA tB tF] + 0.2, -0.2 * [1 1 1 1], ...
    {'t_0', 't_A', 't_B', 't_F'}, ...
    'HorizontalAlignment', 'left', ...
    'VerticalAlignment', 'top', ...
    'Units', 'data'));

% a-axis labels
format(text(-0.2 * [1 1 1], 3 * [1 0 -1], ...
    {'a_{max}', '0', '-a_{max}'}, ...
    'HorizontalAlignment', 'right', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'data'));

%% Acceleration plot (Partial)
ha = axes('ActivePositionProperty', 'position', ...
          'Position', [17/27 16/26 5/22 8/26], ...
          'XLim', [-2 6], 'YLim', [-4 4]);
hold(ha, 'on');
axis(ha, 'equal');
set(ha, 'Visible', 'off');

% t-axis
grey(line([0 5.5], [0 0]));
% t-ticks
grey(line([t0 t0], 0.1 * [1 -1]));
grey(line([tC tC], 0.1 * [1 -1]));
grey(line([tP tP], 0.1 * [1 -1]));
% t-arrow
grey(line(5.5 - 0.3 * [1 0 1], 0.1 * [-1 0 1]));

% a-axis
grey(line([0 0], 4 * [1 -1]));
% a-ticks
grey(line(0.1 * [-1 1], 3 * [1 1]));
grey(line(0.1 * [-1 1], [0 0]));
grey(line(0.1 * [-1 1], -3 * [1 1]));
% a-arrows
grey(line(0.1 * [-1 0 1], 4 - 0.4 * [1 0 1]));
grey(line(0.1 * [-1 0 1], 0.4 * [1 0 1] - 4));

% data
plot(ha, [t0 t0], [ 0  3], 'b:');
plot(ha, [t0 tC], [ 3  3], 'b-', 'LineWidth', 2);
plot(ha, [tC tC], [ 3 -3], 'b:');
plot(ha, [tC tP], [-3 -3], 'b-', 'LineWidth', 2);
plot(ha, [tP tP], [-3  0], 'b:');

% title
format(text(0.5, 1.1, 'Acceleration vs. Time (Partial)', ...
    'HorizontalAlignment', 'center', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'normalized'));

% t-axis labels
format(text([t0 tC tP] + 0.2, -0.2 * [1 1 1], ...
    {'t_0', 't_C', 't_F'}, ...
    'HorizontalAlignment', 'left', ...
    'VerticalAlignment', 'top', ...
    'Units', 'data'));

% a-axis labels
format(text(-0.2 * [1 1 1], 3 * [1 0 -1], ...
    {'a_{max}', '0', '-a_{max}'}, ...
    'HorizontalAlignment', 'right', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'data'));

%% Velocity plot (Full)
ha = axes('ActivePositionProperty', 'position', ...
          'Position', [2/27 11/26 14/27 4/26], ...
          'XLim', [-2 12], 'YLim', [-1 3]);
hold(ha, 'on');
axis(ha, 'equal');
set(ha, 'Visible', 'off');

% t-axis
grey(line([0 11.5], [0 0]));
% t-ticks
grey(line([t0 t0], 0.1 * [1 -1]));
grey(line([tA tA], 0.1 * [1 -1]));
grey(line([tB tB], 0.1 * [1 -1]));
grey(line([tF tF], 0.1 * [1 -1]));
% t-arrow
grey(line(11.5 - 0.3 * [1 0 1], 0.1 * [-1 0 1]));

% v-axis
grey(line([0 0], [3 -1]));
% v-ticks
grey(line(0.1 * [-1 1], 2 * [1 1]));
grey(line(0.1 * [-1 1], [0 0]));
% v-arrows
grey(line(0.1 * [-1 0 1], 3 - 0.4 * [1 0 1]));
grey(line(0.1 * [-1 0 1], 0.4 * [1 0 1] - 1));

% data
plot(ha, [t0 tA tB tF], [0 2 2 0], 'b-', 'LineWidth', 2);

% title
format(text(0.5, 1.1, 'Velocity vs. Time (Full)', ...
    'HorizontalAlignment', 'center', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'normalized'));

% t-axis labels
format(text([t0 tA tB tF], -0.2 * [1 1 1 1], ...
    {'t_0', 't_A', 't_B', 't_F'}, ...
    'HorizontalAlignment', 'center', ...
    'VerticalAlignment', 'top', ...
    'Units', 'data'));

% v-axis labels
format(text(-0.2 * [1 1], 2 * [1 0], ...
    {'v_{max}', '0'}, ...
    'HorizontalAlignment', 'right', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'data'));

%% Velocity plot (Partial)
ha = axes('ActivePositionProperty', 'position', ...
          'Position', [17/27 11/26 5/22 4/26], ...
          'XLim', [-2 6], 'YLim', [-1 3]);
hold(ha, 'on');
axis(ha, 'equal');
set(ha, 'Visible', 'off');

% t-axis
grey(line([0 5.5], [0 0]));
% t-ticks
grey(line([t0 t0], 0.1 * [1 -1]));
grey(line([tC tC], 0.1 * [1 -1]));
grey(line([tP tP], 0.1 * [1 -1]));
% t-arrow
grey(line(5.5 - 0.3 * [1 0 1], 0.1 * [-1 0 1]));

% v-axis
grey(line([0 0], [3 -1]));
% v-ticks
grey(line(0.1 * [-1 1], 2 * [1 1]));
grey(line(0.1 * [-1 1], 1.6 * [1 1]));
grey(line(0.1 * [-1 1], [0 0]));
% v-arrows
grey(line(0.1 * [-1 0 1], 3 - 0.4 * [1 0 1]));
grey(line(0.1 * [-1 0 1], 0.4 * [1 0 1] - 1));

% data
dt = (2 - 1.6) * (2 / 1.6);
plot(ha, [t0 tC tP], [0 1.6 0], 'b-', 'LineWidth', 2);
plot(ha, [t0 tC+dt*[-1 0 1] tP], [2 2 1.6 2 2], 'b:');

% title
format(text(0.5, 1.1, 'Velocity vs. Time (Partial)', ...
    'HorizontalAlignment', 'center', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'normalized'));

% t-axis labels
format(text([t0 tC tP], -0.2 * [1 1 1], ...
    {'t_0', 't_C', 't_F'}, ...
    'HorizontalAlignment', 'center', ...
    'VerticalAlignment', 'top', ...
    'Units', 'data'));

% v-axis labels
format(text(-0.2 * [1 1 1], [2 1.6 0], ...
    {'v_{max}', 'v_{top}', '0'}, ...
    'HorizontalAlignment', 'right', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'data'));

%% Position plot (Full)
ha = axes('ActivePositionProperty', 'position', ...
          'Position', [2/27 2/26 14/27 8/26], ...
          'XLim', [-2 12], 'YLim', [-1 7]);
hold(ha, 'on');
axis(ha, 'equal');
set(ha, 'Visible', 'off');

% t-axis
grey(line([0 11.5], [0 0]));
% t-ticks
grey(line([t0 t0], 0.1 * [1 -1]));
grey(line([tA tA], 0.1 * [1 -1]));
grey(line([tB tB], 0.1 * [1 -1]));
grey(line([tF tF], 0.1 * [1 -1]));
% t-arrow
grey(line(11.5 - 0.3 * [1 0 1], 0.1 * [-1 0 1]));

% p-axis
grey(line([0 0], [7 -1]));
% p-ticks
grey(line(0.1 * [-1 1], 6 * [1 1]));
grey(line(0.1 * [-1 1], 5 * [1 1]));
grey(line(0.1 * [-1 1], [1 1]));
grey(line(0.1 * [-1 1], [0 0]));
% p-arrows
grey(line(0.1 * [-1 0 1], 7 - 0.4 * [1 0 1]));
grey(line(0.1 * [-1 0 1], 0.4 * [1 0 1] - 1));

% data
plot(ha, [t0:0.1:tA], ([t0:0.1:tA] - t0).^2 / (tA - t0).^2, 'b-', 'LineWidth', 2);
plot(ha, [tA tB], [1 5], 'b-', 'LineWidth', 2);
plot(ha, [tB:0.1:tF], 5 + 2 / (tF - tB) * ([tB:0.1:tF] - tB) - ([tB:0.1:tF] - tB).^2 / (tF - tB).^2, 'b-', 'LineWidth', 2);

% title
format(text(0.5, 1.1, 'Position vs. Time (Full)', ...
    'HorizontalAlignment', 'center', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'normalized'));

% t-axis labels
format(text([t0 tA tB tF], -0.2 * [1 1 1 1], ...
    {'t_0', 't_A', 't_B', 't_F'}, ...
    'HorizontalAlignment', 'center', ...
    'VerticalAlignment', 'top', ...
    'Units', 'data'));

% p-axis labels
format(text(-0.2 * [1 1 1 1], [0 1 5 6], ...
    {'0', 'p_A', 'p_B', 'p_F'}, ...
    'HorizontalAlignment', 'right', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'data'));

%% Position plot (Partial)
ha = axes('ActivePositionProperty', 'position', ...
          'Position', [17/27 2/26 5/22 8/26], ...
          'XLim', [-2 6], 'YLim', [-1 7]);
hold(ha, 'on');
axis(ha, 'equal');
set(ha, 'Visible', 'off');

% t-axis
grey(line([0 5.5], [0 0]));
% t-ticks
grey(line([t0 t0], 0.1 * [1 -1]));
grey(line([tC tC], 0.1 * [1 -1]));
grey(line([tP tP], 0.1 * [1 -1]));
% t-arrow
grey(line(5.5 - 0.3 * [1 0 1], 0.1 * [-1 0 1]));

% p-axis
grey(line([0 0], [2.6 -1]));
% p-ticks
grey(line(0.1 * [-1 1], 1.6 * [1 1]));
grey(line(0.1 * [-1 1], 0.8 * [1 1]));
grey(line(0.1 * [-1 1], [0 0]));
% p-arrows
grey(line(0.1 * [-1 0 1], 2.6 - 0.4 * [1 0 1]));
grey(line(0.1 * [-1 0 1], 0.4 * [1 0 1] - 1));

% data
plot(ha, [t0:0.1:tC], 0.8 * ([t0:0.1:tC] - t0).^2 / (tC - t0).^2, 'b-', 'LineWidth', 2);
plot(ha, [tC:0.1:tP], 0.8 + 1.6 / (tP - tC) * ([tC:0.1:tP] - tC) - 0.8 * ([tC:0.1:tP] - tC).^2 / (tP - tC).^2, 'b-', 'LineWidth', 2);

% title
format(text(0.5, 1.1, 'Position vs. Time (Partial)', ...
    'HorizontalAlignment', 'center', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'normalized'));

% t-axis labels
format(text([t0 tC tP], -0.2 * [1 1 1], ...
    {'t_0', 't_C', 't_F'}, ...
    'HorizontalAlignment', 'center', ...
    'VerticalAlignment', 'top', ...
    'Units', 'data'));

% p-axis labels
format(text(-0.2 * [1 1 1], [0 0.8 1.6], ...
    {'0', 'p_C', 'p_F'}, ...
    'HorizontalAlignment', 'right', ...
    'VerticalAlignment', 'middle', ...
    'Units', 'data'));
