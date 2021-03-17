

int deliverPeriod1 =500 % reward duration- adjust this based on pump

int deliverPeriod2 =500% reward duration for middle wells- adjust this based on pump
int delayPeriod = 20000  % 3 sec delay, can be changed
%VARIABLES

% vars for tracking behavior in maze
int lastSideWell= 0           % 1 if left, 3 if right ... this variable tracks the previously activated side well.
int lastWell=0		 	% 1 if left, 3 if right, 2 if center ... this variable trackes the previously activated well
int currWell= 0            	% current well 	... this variable keeps track of when a well was made active.

% vars for tracking where and how much reward
int rewardWell= 0       	% reward well
int nowRewarding = 0 	% variable that keeps tabs on the reward being dispensed .. when reward is being dispensed and the system is in the midst of executing a reward function, this number hops up to a 1, and then relaxes to 0 when reward is finished.

int homedoor = 30%17

int count= 0                	% blink count


int rewardPump1 = 5
int rewardPump2 = 6
int rewardPump3 = 7

updates off 16

function 1
	nowRewarding = 1 							% nowRewarding
		portout[rewardWell]=1 					% reward
		do in deliverPeriod1 						% do after waiting deliverPeriod milliseconds
			portout[rewardWell]=0 				% reset reward
			nowRewarding=0 					% no longer rewarding
		end
end;


function 4
	nowRewarding = 1 							% nowRewarding
		portout[rewardWell]=1 					% reward
		do in deliverPeriod2 						% do after waiting deliverPeriod milliseconds
			portout[rewardWell]=0 				% reset reward
			nowRewarding=0 					% no longer rewarding
		end
            portout[homedoor] = 1
            do in delayPeriod
                      portout[homedoor] = 0
            end
end;

function 2
	if lastWell==0 do
		rewardWell=rewardPump3
		trigger(4)
	end
end;

function 3
	if lastSideWell == 0 && (currWell==1 || currWell == 3) do
		if currWell == 1 do
		rewardWell= rewardPump1
 		end
		if currWell == 3 do
		rewardWell= rewardPump2
 		end
		trigger(1)
	end

end;


callback portin[1] up
	disp('Portin1 up - Left well on') 		% Print state of port to terminal

	% Set current well
	currWell=1							 % Left/1 well active

	% Should we reward?
	trigger(3)							% Reward if first sidewell
	
	if lastWell == 2 do					% Check if previous well = center
		if lastSideWell == 3	do			% Check if side last visited = right
			disp('Poke 1 rewarded')
			rewardWell=rewardPump1 	% dispense reward from here
			trigger(1)					% trigger reward
		else do
			disp('Poke 1 wrong choice - same side')
		end
      else do 
            if lastWell == 3 do
                disp('Poke 1 wrong choice - skip home')
            end
	end
end


callback portin[1] down
	disp('Portin1 down - Left well off') 	% Print state of port to terminal
	lastWell = 1 						% Well left, now last well
	lastSideWell  = 1
end



callback portin[8] up
	disp('portin8 up') 	% Print state of port 2

	% Set current well
	currWell = 2

	% Should we reward?
	trigger(2) 							% Reward if first poke
	
	if lastWell == 1 || lastWell == 3 do 	% Did the animal previously visit left/right arm?
		disp('Home poke rewarded')
		rewardWell = rewardPump3
		trigger(4)
%           do in 300  % delay of triggering home port door
%	else do
%		disp('Poke 2 not rewarded - center')
	end

end

callback portin[8] down
	disp('portin8 down')		% Print state of port 2
	lastWell=2								% Well center is now the last wel
end

callback portin[2] up
	disp('portin2 up')					% Print state of port to terminal
	
	% Set current well
	currWell = 3 						% Set currently active well

	% Should we reward?
	trigger(3)							% Reward if first sidewell
	
	if lastWell == 2 do					% Did animal last visit center arm?				
		if lastSideWell == 1	do			% Was previous side arm left?
			disp('Poke 2 rewarded')
			rewardWell=rewardPump2	% Dispense reward from here
			trigger(1) 					% Trigger reward
		else do
			disp('Poke 2 wrong choice - same side')
		end
      else do 
            if lastWell == 1 do
                disp('Poke 2 wrong choice - skip home')
            end
	end

end


callback portin[2] down
	disp('portin2 down')
	lastWell=3 							% Well right, now last well
	lastSideWell = 3
end;



