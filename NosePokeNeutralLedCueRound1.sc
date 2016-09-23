% PROGRAM NAME: Nose Poke Hold Training
% AUTHOR: Suman K. Guha
% DESCRIPTION: This program delivers reward from a well that is right next to the nose-poke well, as an LED backlight turns off, immediately after the animal nose pokes. The intention is to train the animals to pay attention to a neutral light cue and form an association with nose poke, Led turning on, and reward dispension.

% REGIME:
% A single epoch consists of 3 min sleep box, 7.5 min run, 3 min sleep box, 7.5 min run, and a final 3 min sleep box session. After which the animals are provided with a treat and kept back in the home cage. The animals are weighed before and after the epoch.

% CONSTANT DECLARATION
% ------------------------------------------------------------

int rewardDuration = 500 % duration of reward delivery in miliseconds

% Input Ports: indicate the ethernet port/ECU pin over which it transmits signal
int nosePokeWell = 5

% Output Ports: each indicates the ethernet port/ECU pin over which it receives signal
int ledBacklight = 1
int rewardWellPump = 3;

% VARIABLE DECLARATION
% ------------------------------------------------------------

int rewardCounter = 0 % variable counting number of times rewarded
int startTrial = 0 % variable that indicates whether trial has started or not

% CALLBACKS:EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[5] up
  if (startTrial == 0) do
    disp('trial started')
    startTrial = 1
    disp(startTrial)
  else do
    disp('successful nose poke')
    portout[ledBacklight] = 1
    portout[rewardWellPump] = 1
    rewardCounter = rewardCounter + 1
    disp(rewardCounter)
    do in rewardDuration
      portout[ledBacklight] = 0
      portout[rewardWellPump] = 0
    end
  end
end;